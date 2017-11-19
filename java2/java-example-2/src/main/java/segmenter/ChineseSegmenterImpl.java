package segmenter;


import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.recognition.impl.StopRecognition;
import vo.StockInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChineseSegmenterImpl implements ChineseSegmenter {

    /**
     * this func will get chinese word from a list of stocks. You need analysis stocks' answer and get answer word.
     * And implement this interface in the class : ChineseSegmenterImpl
     * Example: 我今天特别开心 result : 我 今天 特别 开心
     *
     * @param stocks stocks info
     * @return chinese word
     * @see ChineseSegmenterImpl
     */
    //对data的answer字段使用anjs库分词
    @Override
    public List<String> getWordsFromInput(StockInfo[] stocks) throws IOException {
        List<String> list = new ArrayList<String>();
        //去除停用词
        HashMap<String, String> strHashMap = new HashMap<String, String>();
        String stopWordTable = this.getClass().getClassLoader().getResource(".").getPath() + "stopWordTable.txt";
        File f = new File(stopWordTable);
        FileInputStream fileInputStream = new FileInputStream(f);
        //读入停用词文件
        InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader StopWordFileBr = new BufferedReader(isr);
        String stopWord = null;
        for(; (stopWord = StopWordFileBr.readLine()) != null;){
            strHashMap.put(stopWord , "_stop");
        }
        StopWordFileBr.close();
        FilterModifWord.setUpdateDic(strHashMap);
        for (int i =0;i<stocks.length;i++){
            Result result = ToAnalysis.parse(stocks[i].getANSWER());
            List<Term> terms= FilterModifWord.modifResult(result.getTerms());;

            //FilterModifWord.modifResult(terms);
            for (int j = 0;j<terms.size();j++) {
                //System.out.println(terms.get(j).getName());
                list.add(terms.get(j).getName());
            }
        }

        return list;
    }

}
