package m_diary.assets;

import java.util.ArrayList;

import m_diary.controls.Sticker;

//日记内容存储
public class Diary{
    public Diary(String title, String subTitle, int index, int year, int month, int day, String week, String weather){
        this.title = title;
        this.subTitle = subTitle;
        this.index = index;
        this.year = year;
        this.month = month;
        this.week = week;
        this.weather = weather;
        texts = new ArrayList<>();
        pictures = new ArrayList<>();
        videos = new ArrayList<>();
        audios = new ArrayList<>();
    }
    public String getDate(){
        return year + "-" + month + "-" + day;
    }
    public String title;
    public String subTitle;
    public String savePath;
    public int index;
    public int year;
    public int month;
    public int day;
    public String week;
    public String weather;
    public ArrayList<Text> texts;
    public ArrayList<Picture> pictures;
    public ArrayList<Video> videos;
    public ArrayList<Audio> audios;
}