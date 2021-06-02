package m_diary.assets;

public class Weather {
    public static int year = 2021;
    public static int month = 1;
    public static int day = 1;
    public static String week = "星期五";
    public static String weather = "晴";
    public static String notice = "天气晴朗心情舒畅";
    public static String temperature = "13.0f";
    private static boolean leap_year;
    public static String getDateStr(){
        return year + "年" + month + "月" + day + "日";
    }
    public static void plusDAY(){
        day++;
        if(day>get_month_day(month)){
            day = 1;
            month++;
            if (month>12){
                month = 1;
                year++;
                leap_year = is_leap_year(year);
            }
        }
    }
    public static void minusDAY(){
        day--;
        if(day<1){
            month--;
            day = get_month_day(month);
            if (month<1){
                month = 12;
                year--;
                leap_year = is_leap_year(year);
            }
        }
    }
    public static int get_month_day(int month){
        int days = 30;
        switch (month){
            case 1:{days = 31;}break;
            case 2:{
                if(leap_year){
                    days = 29;
                }
                else {
                    days = 28;
                }
            }break;
            case 3:{days = 31;}break;
            case 4:{days = 30;}break;
            case 5:{days = 31;}break;
            case 6:{days = 30;}break;
            case 7:{days = 31;}break;
            case 8:{days = 31;}break;
            case 9:{days = 30;}break;
            case 10:{days = 31;}break;
            case 11:{days = 30;}break;
            case 12:{days = 31;}break;
        }
        return days;
    }
    private static boolean is_leap_year(int year){
        if((year%4 == 0&&year%100 != 0)||year%400 == 0) {
            return true;
        }
        else{
            return false;
        }
    }
}
