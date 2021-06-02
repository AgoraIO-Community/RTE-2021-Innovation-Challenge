package mobi.yiyin.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import mobi.yiyin.MyApplication;

import static mobi.yiyin.MyApplication.textToSpeech;

/**
 * Created by wlk.
 */

public class BaseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    public static String TAG;

    private static final int REQ_TTS_STATUS_CHECK = 0;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        mContext = this;
        MyApplication.getInstance().addActivity(this);
        // 朗读文字的类
        textToSpeech = new TextToSpeech(this, this);

    }

    /**
     * Set status bar immersion.
     */
    protected void setStatusBar() {
        // SDK 21/Android 5.0.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = this.getWindow().getDecorView();
            int setting = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(setting);
            // Set the status bar to transparent.
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110) {
            textToSpeech = new TextToSpeech(getApplicationContext(), null, null);
            read(strToSpeech);
            strToSpeech = "";
        }
        if (requestCode == REQ_TTS_STATUS_CHECK) {
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
                    textToSpeech = new TextToSpeech(getApplicationContext(), null, null);
                    Log.d(TAG, "onActivityResult: TTS Engine is installed!");
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                default:
                    Log.d(TAG, "onActivityResult: Got a failure. TTS apparently not available");
                    Intent dataIntent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(dataIntent);
                    break;
            }
        }
    }

    /**
     * 实现TTS初始化接口
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
        //TTS Engine初始化完成
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            //设置发音语言
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(TAG, "onInit: Language is not available");
            } else {
//                textToSpeech.speak("欢迎使用启明电台，请触摸屏幕，以环绕方式查找所需内容，找到后，放开手指即可将其激活。", TextToSpeech.QUEUE_FLUSH, null);
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private String strToSpeech = "";
    protected void read(String str) {
        if(TextUtils.isEmpty(str))
            return;
        if (textToSpeech == null) {
            //  MyApplication.textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            //检查 TTS 数据是否已经安装并且可用
          /*  Intent checkIntent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);*/
            strToSpeech = str;
            startActivityForResult(new Intent("com.android.settings.TTS_SETTINGS"), 110);
        }else{
            textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**
     * Set status bar's color.
     * @param activity Activity of page.
     * @param colorId Color ID.
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorId));
            }
        } catch (Exception e) {
            Log.e("BaseActivity", e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().finishActivity(this);
        super.onDestroy();
    }

}
