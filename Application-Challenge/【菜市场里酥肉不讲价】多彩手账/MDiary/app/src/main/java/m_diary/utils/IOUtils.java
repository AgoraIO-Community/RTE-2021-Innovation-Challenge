package m_diary.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import m_diary.assets.Diary;

public class IOUtils {
    public static String readFile(String path){
        File diaryFile = new File(path);
        if(!diaryFile.exists()){
            return "";
        }
        String content = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(diaryFile));
            content = br.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return content;
    }
    public static Diary getMyDiary(String content){
        return new Gson().fromJson(content, new TypeToken<Diary>(){}.getType());
    }
}
