package util;

import vo.StockInfo;
import vo.UserInterest;

import java.io.*;
import java.util.Scanner;

public class FileHandlerImpl implements FileHandler {
    // Save the stockinfomation
    private static StockInfo[] stockInfos;
    // create a public static get moethod to make stockInfos directly used by other classes
    public static StockInfo[] getStockInfos() {
        return stockInfos;
    }
    /**
     * This func gets stock information from the given interfaces path.
     * If interfaces don't exit,or it has a illegal/malformed format, return NULL.
     * The filepath can be a relative path or a absolute path
     *
     * @param filePath
     * @return the Stock information array from the interfaces,or NULL
     */
    @Override
    public StockInfo[] getStockInfoFromFile(String filePath) throws IOException {
        int nCount= 0,x = 0;
        File file = new File(filePath);
        Scanner inputFromFile1 =  new Scanner(file,"UTF-8");
        while(inputFromFile1.hasNextLine()){
            inputFromFile1.nextLine();
            nCount++;
        }
        stockInfos = new StockInfo[nCount-1];
        Scanner inputFromFile2 =  new Scanner(file,"UTF-8");
        inputFromFile2.nextLine();
        for (int i =0;i<nCount-1;i++){
            String content = inputFromFile2.nextLine();
            stockInfos[i] = new StockInfo(content.split("\\t"));
       }
       return stockInfos;
    }

//ok
    /**
     * This func gets user interesting from the given interfaces path.
     * If interfaces don't exit,or it has a illegal/malformed format, return NULL.
     * The filepath can be a relative path or a absolute path
     *
     * @param filePath
     * @return
     */
    @Override
    public UserInterest[] getUserInterestFromFile(String filePath) throws IOException {
        UserInterest[] userInterests=new UserInterest[500];
        //读取文件
        File f = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(isr);
        String line;
        //利用char型数组存储每行数据，然后转为int型数组
        char[] cs = new char[60];
        int [] ints=new int[60];
        int count=0;
        line = bufferedReader.readLine();
        while (count < 500){
            cs= line.toCharArray();
            for (int i = 0; i < cs.length; i++) {
                ints[i] = Character.getNumericValue(cs[i]);
            }
            userInterests[count] = new UserInterest(ints);
            line = bufferedReader.readLine();
            count++;
        }
        return userInterests;
    }
//ok
    /**
     * This function need write matrix to files
     *
     * @param matrix the matrix you calculate
     */
    @Override
    public void setCloseMatrix2File(double[][] matrix) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource(".").getPath() + "closeResult.txt");
        //File file = new File("D:\\closeResult.txt");
        FileOutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream,"UTF-8");
        String str;
        for (int i = 0; i < matrix.length; i++) {
            str="";
            for (int j = 0; j < matrix[i].length; j++) {
                if (j==matrix[i].length-1) str += Double.toString(matrix[i][j])+"\n";
                else str += Double.toString(matrix[i][j])+"\t";
            }
            writer.write(str);
        }
        writer.close();
        outputStream.close();
    }

    /**
     * This function need write recommend to files
     *
     * @param recommend the recommend you calculate
     */
    @Override
    public void setRecommend2File(double[][] recommend) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource(".").getPath() + "recomResult");
        //File file = new File("D:\\recomResult.txt");
        FileOutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream,"UTF-8");
        String str;
        for (int i = 0; i < recommend.length; i++) {
            str="";
            for (int j = 0; j < recommend[i].length; j++) {
                if (j==recommend[i].length-1) str += Double.toString(recommend[i][j])+"\n";
                else str += Double.toString(recommend[i][j])+"\t";
            }
            writer.write(str);
        }
        writer.close();
        outputStream.close();

    }
    }


