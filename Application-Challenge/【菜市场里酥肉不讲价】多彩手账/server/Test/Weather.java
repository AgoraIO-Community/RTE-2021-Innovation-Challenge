package Test;

public class Weather {
    int year;
    int month;
    int day;
    String temperature;
    String notice;
    String weather;
    String week;
    String requestCode;

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public String getNotice() {
        return notice;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
