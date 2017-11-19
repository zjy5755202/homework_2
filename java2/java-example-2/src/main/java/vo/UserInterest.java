package vo;

public class UserInterest
{
    //利用double型数组来存取数据
    private int[] interests;
    public UserInterest(int[] interests){
        setInterests(interests);
    }
    public int[] getInterests(){
        return interests;
    }
    private void setInterests(int[] interests){
        this.interests = new int[interests.length];
        for (int i = 0; i < interests.length; i++) {
            this.interests[i] = interests[i];
        }
    }
}
