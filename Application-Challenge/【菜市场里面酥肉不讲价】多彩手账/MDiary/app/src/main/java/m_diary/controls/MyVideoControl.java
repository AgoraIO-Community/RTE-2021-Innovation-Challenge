package m_diary.controls;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import m_diary.activities.AudioActivity;
import m_diary.activities.VideoActivity;
import m_diary.utils.UriUtils;

public class MyVideoControl {
    private Context video_context;
    public ArrayList<AlbumVideoView> videoViews = new ArrayList<>();
    public ArrayList<String> videoPath = new ArrayList<>();
    private MyMediaController mediaController;
    private boolean sound_enable;
    public int videoNum;

    private int mVideoWidth;//视频宽度
    private int mVideoHeight;//视频高度
    private float scale;//视频尺度
    private int showVideoHeight = 300;//默认显示高度

    private RelativeLayout rootView;

    public MyVideoControl(Context context) {
        video_context = context;
        videoNum = 0;
        Activity activity = (Activity) context;
        rootView = activity.getWindow().getDecorView().findViewById(R.id.video_in_diary);
    }

    public void addVideo(String path, AlbumVideoView newVideo){
        rootView.addView(newVideo);
        videoViews.add(newVideo);
        videoPath.add(path);
        videoNum++;
        newVideo.setEnabled(false);
        displayVideo(path);
    }

    /**
     * 显示视频初始化
     * @param Path
     */
    private void displayVideo(String Path) {
        if (Path != null) {
            AlbumVideoView currentView = videoViews.get(videoNum-1);
            currentView.setEnabled(true);
            currentView.setVideoPath(Path);
            mediaController = new MyMediaController(video_context, currentView);
            videoViews.get(videoNum-1).setMediaController(mediaController);
            mediaController.setAnchorView(currentView);
            currentView.requestFocus();
            sound_enable = true;
            //加载视频后按比例重置视频大小
            currentView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //获取视频资源的宽度
                    mVideoWidth = mp.getVideoWidth();
                    //获取视频资源的高度
                    mVideoHeight = mp.getVideoHeight();
                    scale = (float) mVideoWidth / (float) mVideoHeight;
                    refreshPortraitScreen(showVideoHeight, currentView);
                }
            });
        } else {
            Toast.makeText(video_context, "Failed to get video!", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 重新刷新视频显示大小，竖屏显示的大小，竖屏显示以宽度为准
     */
    public void refreshPortraitScreen(int height, AlbumVideoView albumVideoView) {
        if (height == 0) {
            height = showVideoHeight;
        }
        if (mVideoHeight > 0 && mVideoWidth > 0) {
            //拉伸比例
            mVideoWidth = (int) (height * scale);//设置surfaceview画布大小
            //mVideoHeight = (int) (mVideoWidth / scale);
            albumVideoView.getHolder().setFixedSize(mVideoWidth, height);
            //Log.i("竖屏时视频的宽高==", "宽=" + mVideoWidth + "，高=" + mVideoHeight);
            //F重绘VideoView大小，AlbumVideoView时对外抛出方法
            albumVideoView.setMeasure(mVideoWidth, height);
            //F请求调整
            albumVideoView.requestLayout();
        }
    }
}
