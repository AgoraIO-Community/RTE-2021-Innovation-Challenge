package m_diary.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

import m_diary.activities.DiaryActivity;

//自定义音频播放控件
public class MyAudioControl extends ConstraintLayout implements View.OnTouchListener {
    private Context music_context;
    private TextView sound_name;
    private TextView play_time;
    private Button play_pause_bt;
    private Button stop_bt;
    private Button sound_mute_bt;
    private MediaPlayer mp;
    private Handler handler;
    private Timer play_timer;
    private TimerTask play_time_task;
    public boolean sound_enable;
    public int sound_power;
    private int Duration;
    public static int second;
    public static int minute;
    public String name;
    private SeekBar sb;
    public static int max_min = 0;
    public static int max_sec = 0;
    public static final int PLAYING = 1001;
    public String uri;
    public String path;

    //private RelativeLayout rootView;
    //构造函数
    public MyAudioControl(Context context, Uri uri, String path, String in_name) {
        super(context);
        this.uri = uri.toString();
        this.path = path;
//        rootView = activity.getWindow().getDecorView().findViewById(R.id.audio_in_diary);
        LayoutInflater.from(context).inflate(R.layout.audio_play_layout, this,true);
        name = in_name;
        music_context = context;
        sound_name = findViewById(R.id.Audio_Name);
        play_time = findViewById(R.id.Audio_Time);
        play_pause_bt = findViewById(R.id.Music_Play_Pause_bt);
        stop_bt = findViewById(R.id.Music_Stop_bt);
        sound_mute_bt = findViewById(R.id.Music_Sound_Mute_bt);
        sb = findViewById(R.id.Music_Seek_Bar);
        init_Music_Bar(music_context, uri);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float current_time = (float) seekBar.getProgress();
                int current_min = (int)((float)current_time/60000.0f);
                int current_sec = (int)((float)current_time/1000.0f) - current_min*60;
                minute = current_min;
                second = current_sec;
                String second_str;
                if (second < 10) {
                    second_str = "0" + current_sec;
                } else second_str = current_sec + "";
                String max_str = "/";
                if(max_min<10){
                    max_str += "0"+max_min + ":";
                }
                else max_str += max_min + ":";
                if(max_sec<10){
                    max_str += "0"+max_sec;
                }
                else max_str += max_sec;
                play_time.setText("时长 : 0" + current_min + ":" + second_str + max_str);
                mp.seekTo(seekBar.getProgress());
            }
        });
    }
    public MyAudioControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyAudioControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    // 初始化线程同步
    @SuppressLint("HandlerLeak")
    private void initial(){
        handler = new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message message){
                switch (message.what) {
                    case PLAYING:{
                        if (second == 60) {
                            second = 0;
                            minute++;
                        }
                        String second_str = "";
                        if (second < 10) {
                            second_str = "0" + second;
                        } else second_str = second + "";
                        String max_str = "/";
                        if(max_min<10){
                            max_str += "0"+max_min + ":";
                        }
                        else max_str += max_min + ":";
                        if(max_sec<10){
                            max_str += "0"+max_sec;
                        }
                        else max_str += max_sec;
                        play_time.setText("时间 : 0" + minute + ":" + second_str + max_str);
                    }break;
                }
            }
        };
    }
    //播放&暂停
    private void stop_play_timer(){
        if (play_timer != null) {
            play_timer.cancel();
            play_timer = null;
        }
        if (play_time_task != null) {
            play_time_task.cancel();
            play_time_task = null;
        }
    }
    //消息响应初始化
    private void init_Listener(){
        //播放&暂停按钮
        play_pause_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    stop_play_timer();
                    play_pause_bt.setBackgroundResource(R.drawable.music_play_bt_selector);
                }
                else {
                    play_timer = new Timer();
                    play_time_task = getPlay_time_task();
                    play_timer.schedule(play_time_task, 0, 1000);
                    handler.post(start);
                    play_pause_bt.setBackgroundResource(R.drawable.music_pause_bt_selector);
                }
            }
        });
        //停止按钮
        stop_bt.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                mp.seekTo(0);
                mp.pause();
                stop_play_timer();
                second = 0;
                minute = 0;
                String second_str;
                if (second < 10) {
                    second_str = "0" + second;
                } else second_str = second + "";
                String max_str = "/";
                if(max_min<10){
                    max_str += "0"+max_min + ":";
                }
                else max_str += max_min + ":";
                if(max_sec<10){
                    max_str += "0"+max_sec;
                }
                else max_str += max_sec;
                play_time.setText("时间 : 0" + minute + ":" + second_str + max_str);
                play_pause_bt.setBackgroundResource(R.drawable.music_play_bt_selector);
            }
        });
        //静音按钮
        sound_mute_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager mAudioManager = (AudioManager) music_context.getSystemService(Context.AUDIO_SERVICE);
                if (!sound_enable) {
                    sound_enable = true;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sound_power, 0);
                    sound_mute_bt.setBackgroundResource(R.drawable.music_sound_bt_selector);
                } else {
                    sound_power = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    sound_enable = false;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    sound_mute_bt.setBackgroundResource(R.drawable.music_mute_bt_selector);
                }
            }
        });
    }
    //线程获取
    public TimerTask getPlay_time_task() {
        return new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                second++;
                if((minute*60 + second)>=(float)Duration/1000.0f) {
                    stop_play_timer();
                    second = 0;
                    minute = 0;
                }
                message.what = PLAYING;
                handler.sendMessage(message);
            }
        };
    }
    //播放线程
    Runnable start=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            mp.start();
            handler.post(update_sb);

            //用一个handler更新SeekBar
        }

    };
    //时间线程
    Runnable update_sb =new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            sb.setProgress(mp.getCurrentPosition());
            handler.postDelayed(update_sb, 1000);
            //每秒钟更新一次
        }
    };
    //初始化控件
    public void init_Music_Bar(Context context,Uri uri){
        initial();
        mp = MediaPlayer.create(context, uri);
        //监听器
        Duration=mp.getDuration();
        //音乐文件持续时间
        sb.setMax(Duration);
        second = 0;
        minute = 0;
        String second_str;
        if (second < 10) {
            second_str = "0" + second;
        } else second_str = second + "";
        max_min = (int)((float)Duration/60000.0f);
        max_sec = (int)((float)Duration/1000.0f) - max_min*60;
        String max_str = "/";
        if(max_min<10){
            max_str += "0"+max_min + ":";
        }
        else max_str += max_min + ":";
        if(max_sec<10){
            max_str += "0"+max_sec;
        }
        else max_str += max_sec;
        sound_enable = true;
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sound_power = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        play_time.setText("时间 : 0" + minute + ":" + second_str + max_str);
        sound_name.setText(name);
        init_Listener();
    }

    private long begin, end;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                begin = System.currentTimeMillis();
            }break;
            case MotionEvent.ACTION_MOVE:{
                end = System.currentTimeMillis();
                long time = end - begin;
                if(time>500){
                    DiaryActivity activity = (DiaryActivity)music_context;
                    activity.audioContent.removeView(this);
                }
            }
            break;
        }
        return false;
    }
}
