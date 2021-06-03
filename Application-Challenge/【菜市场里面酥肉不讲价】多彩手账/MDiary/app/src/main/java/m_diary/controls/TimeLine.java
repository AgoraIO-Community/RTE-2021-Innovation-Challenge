package m_diary.controls;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;

import m_diary.activities.DiaryActivity;
import m_diary.assets.Diary;
import m_diary.utils.Protocol;

import static m_diary.activities.MainActivity.totalDiaryNum;

public class TimeLine extends ConstraintLayout {

    private Context context;
    private Button  Diary_Button;
    private ImageView Time_Line_BG;
    private TextView Title_View;
    private TextView Date_View;
    private TextView Day_View;
    private TextView Weather_View;
    private String title;
    private String week;
    private String weather;
    private String date;
    private Diary diary;
    private LinearLayout linearLayout;
    private DialogInterface.OnClickListener confirm;
    private DialogInterface.OnClickListener cancel;
    private DialogInterface.OnClickListener delete;

    //构造函数
    public TimeLine(Context context, Diary diary){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.timelinebt,this,true);
        this.context = context;
        date = diary.getDate();
        week = diary.week;
        title = diary.title;
        weather = diary.weather;
        initial_content_View();
    }
    public TimeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TimeLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /*******************初始化函数**********************/
    //初始化控件
    private void initial_content_View(){
        Diary_Button = findViewById(R.id.Diary_bt);
        Time_Line_BG = findViewById(R.id.Time_Line_BG);
        Title_View = findViewById(R.id.Diary_Name);
        Date_View = findViewById(R.id.Diary_Date);
        Day_View = findViewById(R.id.Diary_Day);
        Weather_View = findViewById(R.id.Diary_Weather);
        initial_button_listener();
        setData();
    }
    //设置控件内容
    private void setData(){
        switch (weather){
            case "晴" :{ Time_Line_BG.setImageResource(R.drawable.sunny); }break;
            case "雨" :{ Time_Line_BG.setImageResource(R.drawable.rain); }break;
            case "多云" :{ Time_Line_BG.setImageResource(R.drawable.cloudy);}break;
            case "阴天" :{ Time_Line_BG.setImageResource(R.drawable.overcast); }break;
        }
        Weather_View.setText(weather);
        Title_View.setText(title);
        Date_View.setText(date);
        Day_View.setText(week);
    }
    /*******************初始化函数**********************/
    /*******************消息响应**********************/
    private void initial_dialog(){
        //确定按钮
        confirm= (arg0, arg1) -> {
            //这里删除日记
            linearLayout.removeView(TimeLine.this);
        };
        //删除日记
        delete = (dialog, which) -> {
            AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(context);
            alert_dialog_builder.setMessage("确定删除?");
            alert_dialog_builder.setPositiveButton("是", confirm);
            alert_dialog_builder.setNegativeButton("否", cancel);
            AlertDialog alert_dialog = alert_dialog_builder.create();
            alert_dialog.show();
        };
        //取消
        cancel= (arg0, arg1) -> arg0.cancel();
    }
    private void initial_button_listener(){
        Diary_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions compat = ActivityOptions.makeScaleUpAnimation(Diary_Button,Diary_Button.getWidth() / 2, Diary_Button.getHeight() / 2, 0, 0);
                Intent i = new Intent(context, DiaryActivity.class);
                i.putExtra(Protocol.DIARY_PATH, diary.savePath);
                i.putExtra(Protocol.DIARY_NUM, totalDiaryNum);
                i.putExtra(Protocol.NEW_DIARY,false);
                context.startActivity(i,compat.toBundle());
            }
        });
        Diary_Button.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(context);
                alert_dialog_builder.setMessage("删除这个日记?");
                alert_dialog_builder.setPositiveButton("删除", delete);
                alert_dialog_builder.setNegativeButton("取消", cancel);
                AlertDialog alert_dialog = alert_dialog_builder.create();
                alert_dialog.show();
                return true;
            }
        });
        initial_dialog();
    }
    /*******************消息响应**********************/


}
