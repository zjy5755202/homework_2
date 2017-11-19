package vo;

public class StockInfo {
    private String ID;
    private String TITLE;
    private String AUTHOR;
    private String DATE;
    private String LASTUPDATE;
    private String CONTENT;
    private String ANSWERAUTHOR;
    private String ANSWER;

    public String getID() {
        return ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public String getDATE() {
        return DATE;
    }

    public String getLASTUPDATE() {
        return LASTUPDATE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public String getANSWERAUTHOR() {
        return ANSWERAUTHOR;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public StockInfo(String[] stockStrings) {
        this.ID = stockStrings[0];
        this.TITLE = stockStrings[1];
        this.AUTHOR = stockStrings[2];
        this.DATE = stockStrings[3];
        this.LASTUPDATE = stockStrings[4];
        this.CONTENT = stockStrings[5];
        this.ANSWERAUTHOR = stockStrings[6];
        this.ANSWER = stockStrings[7];
    }

//    public StockInfo(String temp) {
//       String []stockStrings=temp.split("\\t");
//        this.ID = stockStrings[0];
//        this.TITLE = stockStrings[1];
//        this.AUTHOR = stockStrings[2];
//        this.DATE = stockStrings[3];
//        this.LASTUPDATE = stockStrings[4];
//        this.CONTENT = stockStrings[5];
//        this.ANSWERAUTHOR = stockStrings[6];
//        this.ANSWER = stockStrings[7];
//    }
}
