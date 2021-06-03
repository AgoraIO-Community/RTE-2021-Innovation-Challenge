package m_diary.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import m_diary.utils.Protocol;
import m_diary.utils.UriUtils;
import m_diary.utils.UserManager;

import static m_diary.activities.DiaryActivity.diary;

public class VideoActivity extends AppCompatActivity {
    private Uri video_uri;
    private String filePath;
    private VideoView videoView;
    private MediaController mediaController;
    private boolean sound_enable;
    private boolean shoot_video;
    private boolean open_video;
    private int sound_power;

    private DialogInterface.OnClickListener confirm;
    private DialogInterface.OnClickListener cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.Video_Show);
        videoView.setEnabled(false);
        shoot_video = false;
        open_video = false;
        initial_dialog();
        ActivityCompat.requestPermissions(VideoActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Protocol.STORAGE_PERMISSION);
        ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Protocol.READE_PERMISSION);
    }
    /*******************功能函数**********************/
    //将视频复制到存储目录下
    public void copy_Video(String from_File){
        String save_File_Name = Environment.getExternalStorageDirectory() + "/Diary_Data/" + UserManager.username + "/" + diary.index + "/Video/" + System.currentTimeMillis();
        File save_File = new File(save_File_Name);
        if (!save_File.getParentFile().exists()) {
            save_File.getParentFile().mkdirs();
        }
        else if(save_File.exists()||save_File.isDirectory()){
            save_File.delete();
        }
        try {
            InputStream inputStream = new FileInputStream(from_File);
            FileOutputStream outputStream = new FileOutputStream(save_File);
            byte[] bytes = new byte[1024];
            int i;
            while ((i = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, i);
            }
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        filePath = save_File_Name;
    }
    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("video/*");
        startActivityForResult(intent, Protocol.CHOOSE_VIDEO); // 打开相册
    }
    //打开相机
    private void openCamera(){
// 保存录像到指定的路径
        /*用来记录录像存储路径*/
        filePath = Environment.getExternalStorageDirectory() + "/Diary_Data/" + UserManager.username + "/" + diary.index + "/Video/" + System.currentTimeMillis() + ".mp4";
        File file = new File(filePath);//设置录像存储路径
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            video_uri = Uri.fromFile(file);
        } else {
            video_uri = FileProvider.getUriForFile(VideoActivity.this, "m_diary.my_provider", file);
        }
        // 激活系统的照相机进行录像，通过Intent激活相机并实现录像功能
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
        startActivityForResult(intent, Protocol.SHOOT_VIDEO);
    }
    //获取视频文件路径
    @TargetApi(19)
    private void handleVideoOnKitKat(Intent data) {
        String Path = null;
        if(data != null) {
            Uri uri = data.getData();
            video_uri = uri;
            Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
            Path = UriUtils.getPath(VideoActivity.this,uri);
            displayVideo(Path); // 根据图片路径显示图片
        }
        else{
            Toast.makeText(this, "You canceled open", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleVideoBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        video_uri = uri;
        String imagePath = getVideoPath(uri, null);
        displayVideo(imagePath);
    }
    private String getVideoPath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //显示视频
    private void displayVideo(String Path) {
        if (Path != null) {
            open_video = true;
            shoot_video= false;
            filePath = Path;
            videoView.setEnabled(true);
            videoView.setVideoPath(filePath);
            mediaController=new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoView);
            videoView.requestFocus();
            sound_enable = true;
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            sound_power = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.play_bt_selector);
                }
            });

        } else {
            Toast.makeText(this, "打开视频失败!", Toast.LENGTH_SHORT).show();
        }
    }
    //停止播放
    public void stop_all(){
        if(videoView.isEnabled()){
            videoView.seekTo(0);
            videoView.pause();
            findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.play_bt_selector);
        }
        else{
            Toast.makeText(this, "未打开视频!", Toast.LENGTH_SHORT).show();
        }
    }
    /*******************功能函数**********************/
    /*******************消息响应**********************/
    private void initial_dialog(){
        confirm= (arg0, arg1) -> {
            Intent i = new Intent();
            i.putExtra(Protocol.CHANGED, false);
            VideoActivity.this.setResult(Protocol.ADD_VIDEO,i);
            if(shoot_video){
                File delete_file = new File(filePath);
                delete_file.delete();
                String[] path = {filePath};
                VideoActivity.this.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?",path);
            }
            stop_all();
            finish();
        };
        cancel= (arg0, arg1) -> arg0.cancel();
    }
    //返回
    public void back_video(View view){
        if(open_video || shoot_video) {
            AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(this);
            alert_dialog_builder.setMessage("离开且不保存吗?");
            alert_dialog_builder.setPositiveButton("是", confirm);
            alert_dialog_builder.setNegativeButton("否", cancel);
            AlertDialog alert_dialog = alert_dialog_builder.create();
            alert_dialog.show();
        }
        else {
            Intent i = new Intent();
            i.putExtra(Protocol.CHANGED, false);
            VideoActivity.this.setResult(Protocol.ADD_VIDEO,i);
            finish();
        }
    }
    //完成视频选择
    public void complete_video(View view){
        //to do:add your media
        if(open_video||shoot_video) {
            Intent i = new Intent();
            if (open_video) {
                copy_Video(filePath);
            }
            stop_all();
            i.putExtra(Protocol.CHANGED, true);
            i.putExtra(Protocol.VIDEO_PATH, filePath);
            //i.putExtra(Protocol.VIDEO_URI, video_uri.toString());
            this.setResult(Protocol.ADD_VIDEO, i);
            finish();
        }
        else{
            Toast.makeText(this, "你没有选择打开视频!", Toast.LENGTH_SHORT).show();
        }
    }
    //打开视频文件
    public void open_video_file(View view){
        if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Protocol.OPEN_PERMISSION);
        }
        else{
            openAlbum();
        }
    }
    //录制视频按钮
    public void shoot_video(View view){
        if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.CAMERA}, Protocol.CAMERA_PERMISSION);
        }
        else{
            openCamera();
        }
    }
    //播放暂停按钮
    public void play_pause(View view){
        if(videoView.isEnabled()){
            if(videoView.isPlaying()){
                videoView.pause();
                findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.play_bt_selector);
            }
            else {
                videoView.start();
                findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.pause_bt_selector);
            }
        }
        else{
            Toast.makeText(this, "未打开视频!", Toast.LENGTH_SHORT).show();
        }
    }
    //停止按钮
    public void stop(View view){
        if(videoView.isEnabled()){
            videoView.seekTo(0);
            videoView.pause();
            findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.play_bt_selector);
        }
        else{
            Toast.makeText(this, "未打开视频!", Toast.LENGTH_SHORT).show();
        }
    }
    //静音按钮
    public void sound_mute(View view){
        if(videoView.isEnabled()){
            AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if(!sound_enable) {
                sound_enable = true;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,sound_power,0);
                findViewById(R.id.Audio_Mute_bt).setBackgroundResource(R.drawable.audio_bt_selector);
            }
            else {
                sound_power =  mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                sound_enable = false;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE,0);
                findViewById(R.id.Audio_Mute_bt).setBackgroundResource(R.drawable.mute_bt_selector);
            }
        }
        else{
            Toast.makeText(this, "未打开视频!", Toast.LENGTH_SHORT).show();
        }
    }
    //按键响应
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(open_video || shoot_video) {
                AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(this);
                alert_dialog_builder.setMessage("离开且不保存吗?");
                alert_dialog_builder.setPositiveButton("是", confirm);
                alert_dialog_builder.setNegativeButton("否", cancel);
                AlertDialog alert_dialog = alert_dialog_builder.create();
                alert_dialog.show();
            }
            else {
                Intent i = new Intent();
                i.putExtra(Protocol.CHANGED, false);
                VideoActivity.this.setResult(Protocol.ADD_VIDEO,i);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    //得到权限响应
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Protocol.OPEN_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
            }break;
            case Protocol.CAMERA_PERMISSION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
            }break;
        }
    }
    //打开或录像得到的视频
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case Protocol.SHOOT_VIDEO:{
                if(data != null) {
                    shoot_video = true;
                    open_video = false;
                    videoView.setEnabled(true);
                    videoView.setVideoURI(video_uri);
                    videoView.setVideoPath(filePath);
                    mediaController = new MediaController(this);
                    videoView.setMediaController(mediaController);
                    mediaController.setMediaPlayer(videoView);
                    videoView.requestFocus();
                    sound_enable = true;
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            findViewById(R.id.Play_Pause_bt).setBackgroundResource(R.drawable.play_bt_selector);
                        }
                    });
                }
                else {
                    File delete_file = new File(filePath);
                    delete_file.delete();
                    Toast.makeText(this, "You canceled taking!", Toast.LENGTH_SHORT).show();
                    shoot_video= false;
                }
            }break;
            case Protocol.CHOOSE_VIDEO:{
                //choose your video
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleVideoOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleVideoBeforeKitKat(data);
                }
            }break;
        }
    }
    /*******************消息响应**********************/
}