package m_diary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import m_diary.utils.Protocol;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
    }
    //打开新日记
    public void openDiary(View view){
        //int max_index = preferences.getInt(today_date + SaveActivity.MAX_INDEX,-1);
        Button mImage = findViewById(R.id.New_Diary_bt);
        ActivityOptions compat = ActivityOptions.makeScaleUpAnimation(mImage,mImage.getWidth() / 2, mImage.getHeight() / 2, 0, 0);
        Intent i = new Intent(this,DiaryActivity.class);
        i.putExtra(Protocol.NEW_DIARY,true);
//        i.putExtra(Protocl.DIARY_INDEX,max_index+1);
        startActivity(i,compat.toBundle());
    }
    //返回
    public void back(View view) {
        finish();
    }
}