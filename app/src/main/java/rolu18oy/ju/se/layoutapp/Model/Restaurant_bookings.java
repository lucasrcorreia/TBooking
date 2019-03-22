package rolu18oy.ju.se.layoutapp.Model;

public class Restaurant_bookings {

    Restaurant_bookings(){

    }

    String id;
    int year;
    int month;
    int numberofpeople;

    public int getNumberofpeople() {
        return numberofpeople;
    }

    public void setNumberofpeople(int numberofpeople) {
        this.numberofpeople = numberofpeople;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    int day;
    int hour;
    String userID;

    String restname;

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Restaurant_bookings(String restname,String id,int numberofpeople, String userid,int year,int month,int day,int hour){
        this.restname = restname;
        this.id = id;
        this.numberofpeople = numberofpeople;
        this.userID = userid;
        this.year = year;
        this.month = month;
        this.hour = hour;
        this.day = day;
    }
}
