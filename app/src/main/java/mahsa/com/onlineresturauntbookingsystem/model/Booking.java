package mahsa.com.onlineresturauntbookingsystem.model;

/**
 * Created by mahsa on 24/05/2017.
 */

public class Booking {

    private String time;
    private String tableNum;
    private int countPerson;
    private String userId;

    public Booking(String time, String tableNum, int countPerson, String userId) {
        this.time = time;
        this.tableNum = tableNum;
        this.countPerson = countPerson;
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public int getCountPerson() {
        return countPerson;
    }

    public void setCountPerson(int countPerson) {
        this.countPerson = countPerson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
