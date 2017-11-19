package recommend;

import javafx.util.Pair;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import segmenter.FilterModifWord;
import util.StockSorterImpl;
import vo.StockInfo;
import vo.UserInterest;
import tf_idf.*;

import java.io.*;
import java.util.*;

public class RecommenderImpl implements Recommender {

//分词计算tf idf并且排序取前20个
    public List<String> get20WordsFromContent(StockInfo temp,StockInfo[] stocks) throws IOException {

        List<String> list = new ArrayList<String>();
        //anjs分词处理
        Result result = ToAnalysis.parse(temp.getCONTENT());
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
        List<Term> terms= FilterModifWord.modifResult(result.getTerms());
        for (int i=0;i<terms.size();i++) list.add(terms.get(i).getName());

        Pair<String, Double>[] pairs = new Pair[list.size()];
        TF_IDFImpl tf_idf = new TF_IDFImpl();
        pairs=tf_idf.getResult(list, stocks);

        //取前20个
        List<String> Result = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            Result.add(pairs[i].getKey());//开始复制一个list的内容到另外一个list
        }
        return Result;
    }
    //合并两个并且统计词频
    public double[] getTotalCountFrom2List(List<String> word1, List<String> word2, StockInfo temp1) throws IOException {  //合并两个List
        //分词
        List<String> temp1list = new ArrayList<String>();
        //anjs分词处理
        Result result = ToAnalysis.parse(temp1.getCONTENT());
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
        List<Term> terms= FilterModifWord.modifResult(result.getTerms());
        for (int i=0;i<terms.size();i++) temp1list.add(terms.get(i).getName());


        List<String> tempword = new ArrayList<String>();
        tempword.addAll(word1);
        tempword.addAll(word2);
        //统计词频数目
        double[] back = new double[tempword.size()];
        Map<String, Double> Map = new HashMap<String, Double>();
        String word = null;
        for (int i = 0; i < tempword.size(); i++) {
            double nCount=0;
            for(int j=0;j<temp1list.size();j++) {
                if(tempword.get(i).equals( temp1list.get(j))){
                    nCount++;
                }
            }
            Map.put(tempword.get(i),nCount);
        }
        Collection<Double> values = Map.values();// 得到全部的value
        Iterator<Double> iter = values.iterator();
        int j = 0;
        while (iter.hasNext()) {
            Double str = iter.next();
            back[j] = str;
            j++;
        }
        tempword.clear();
        return back;
    }
    //计算模
    public double vectorModulo(double[] temp) {
        double vectorModulo = 0;
        for (int i = 0; i < temp.length; i++) {
            vectorModulo += temp[i] * temp[i];
        }
        vectorModulo = Math.sqrt(vectorModulo);
        return vectorModulo;
    }
    //计算积
    public double vectorProduct(double[] temp1, double[] temp2) {
        double vectorProduct = 0;
        for (int i = 0; i < temp1.length; i++) {
            vectorProduct += temp1[i] * temp2[i];
        }
        return vectorProduct;
    }

    public double calculateTwoContentMatrix(StockInfo temp1,StockInfo temp2,StockInfo[] stocks) throws IOException {
        if (temp1==temp2){
            return 1;
        }
        else {
            double[] DoubleArray1 = new double[40];
            double[] DoubleArray2 = new double[40];
            List<String> word1 = new ArrayList<String>();//储存第一个分词排序后的20个词
            List<String> word2 = new ArrayList<String>();//储存第二个分词排序后的20个词
            word1 = get20WordsFromContent(temp1, stocks);
            word2 = get20WordsFromContent(temp2, stocks);
            DoubleArray1 = getTotalCountFrom2List(word1, word2, temp1);
            DoubleArray2 = getTotalCountFrom2List(word1, word2, temp2);
            double vector1Modulo = vectorModulo(DoubleArray1);//向量1的模
            double vector2Modulo = vectorModulo(DoubleArray2);//向量2的模
            double vectorProduct = vectorProduct(DoubleArray1, DoubleArray2);//向量积
            double matrix = vectorProduct / (vector1Modulo * vector2Modulo);
            return matrix;
        }



    }
    /**
     * this function need to calculate stocks' content similarity,and return the similarity matrix
     *
     * @param stocks stock info
     * @return similarity matrix
     */
    @Override
    public double[][] calculateMatrix(StockInfo[] stocks) throws IOException {
        double[][] matrix=new double[stocks.length][stocks.length];

        for (int i = 0; i < stocks.length; i++) {
            for (int j = 0; j <stocks.length; j++) {
                matrix[i][j]=calculateTwoContentMatrix(stocks[i],stocks[j],stocks);
            }
        }
        return matrix;
    }

    /**
     * this function need to recommend the most possibility stock number
     *
     * @param matrix       similarity matrix
     * @param userInterest user interest
     * @return commend stock number
     */
    @Override
    public double[][] recommend(double[][] matrix, UserInterest[] userInterest) {
        List<Integer> Readnews = new ArrayList<Integer>();//读了的newID
        List<Integer> UnReadnews = new ArrayList<Integer>();//未读newID
        Map<Integer,Double> map=new HashMap<Integer, Double>();
        List<Double> recomList = new ArrayList<Double>();//推荐new
        double[][] rResult = new double[500][10];//储存结果
        for (int i = 0; i < userInterest.length; i++) {
            //读取userinterest数据
            for (int j = 0; j < 60; j++) {
                if (userInterest[i].getInterests()[j] == 1)
                    Readnews.add(j);
                else UnReadnews.add(j);
            }
            //利用协同过滤算法对未阅读文件进行打分
          for(int m=0;m<UnReadnews.size();m++){
                double value=0;
                for(int n=0;n<Readnews.size();n++)
                {
                    value+= 1*matrix[UnReadnews.get(m)][Readnews.get(n)];
                }
              map.put(UnReadnews.get(m),value);
            }


            // 对HashMap中的 value 进行排序
            List<Map.Entry<Integer,Double>> infoIds = new ArrayList<Map.Entry<Integer,Double>>(map.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<Integer,Double>>() {
                public int compare(Map.Entry<Integer,Double> o1,
                                   Map.Entry<Integer,Double> o2) {
                    return (o1.getValue()).toString().compareTo(o2.getValue().toString());
                }
            });
int count=0;
for(Map.Entry<Integer,Double>mapping:infoIds)
{
    if(count<10)
    {
       Integer key=mapping.getKey();
       double temp=(double) key;
        recomList.add(temp);
        count++;
    }
}

            //存入矩阵
            for (int j = 0; j < 10; j++) {
               double temp=recomList.get(j);
                rResult[i][j] = temp;
            }
            Readnews.clear();
            UnReadnews.clear();
            recomList.clear();
        }
        return rResult;
    }
}
