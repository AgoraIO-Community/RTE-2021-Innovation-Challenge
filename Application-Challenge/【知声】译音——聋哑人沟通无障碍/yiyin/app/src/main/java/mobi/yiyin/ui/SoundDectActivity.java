package mobi.yiyin.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.mlsdk.sounddect.MLSoundDectListener;
import com.huawei.hms.mlsdk.sounddect.MLSoundDector;

import java.text.DecimalFormat;
import java.util.Vector;

import mobi.yiyin.R;
import mobi.yiyin.base.BaseActivity;

public class SoundDectActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SoundDectActivity";

    private static final int RC_RECORD_CODE = 0x123;

    private final String[] perms = {
            Manifest.permission.RECORD_AUDIO
    };
    private static String[] type;

    private TextView timeTv;
    private TextView voiceTypeTv;

    private long baseTimer;
    private TimerHandler timerHandler;
    private Vector<String> logList;
    private MLSoundDector soundDector;

    private final MLSoundDectListener listener = new MLSoundDectListener() {
        @Override
        public void onSoundSuccessResult(Bundle result) {
            int voiceType = result.getInt(MLSoundDector.RESULTS_RECOGNIZED);
            if (voiceType > 0 && voiceType < 13) {
                logList.add(type[voiceType]);
            }

            String text = "";
            for (String log : logList) {
                text += log + "\n";
            }
            if (logList.size() > 10) {
                logList.remove(0);
            }
            voiceTypeTv.setText(text);
        }

        @Override
        public void onSoundFailResult(int errCode) {
            Log.e(TAG, "FailResult errCode: " + errCode);
            Toast.makeText(SoundDectActivity.this, getString(R.string.sound_dect_error) + errCode, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_dect);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("实时声音场景识别");

        timeTv = findViewById(R.id.time_tv);
        voiceTypeTv = findViewById(R.id.voice_type_tv);
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.stop_btn).setOnClickListener(this);
        type = getResources().getStringArray(R.array.sound_dect_voice_type);
        timerHandler = new TimerHandler();
        logList = new Vector<>();
        initModel();
    }

    private void initModel() {
        soundDector = MLSoundDector.createSoundDector();
        soundDector.setSoundDectListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundDector.destroy();
        if (timerHandler != null) {
            timerHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                if (ActivityCompat.checkSelfPermission(SoundDectActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    boolean startSuccess = soundDector.start(SoundDectActivity.this);
                    if (startSuccess) {
                        timerHandler.sendMessage(Message.obtain(timerHandler, 1));
                        Toast.makeText(SoundDectActivity.this, R.string.sound_dect_start, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                ActivityCompat.requestPermissions(SoundDectActivity.this, perms, RC_RECORD_CODE);

                break;
            case R.id.stop_btn:
                soundDector.stop();
                baseTimer = 0;
                timerHandler.removeCallbacksAndMessages(null);
                Toast.makeText(SoundDectActivity.this, R.string.sound_dect_stop, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_RECORD_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            boolean startSuccess = soundDector.start(SoundDectActivity.this);
            if (startSuccess) {
                timerHandler.sendMessage(Message.obtain(timerHandler, 1));
                Toast.makeText(SoundDectActivity.this, R.string.sound_dect_start, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (0 == baseTimer) {
                    baseTimer = SystemClock.elapsedRealtime();
                }

                int time = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                String mm = new DecimalFormat("00").format(time / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                if (null != timeTv) {
                    timeTv.setText(mm + ":" + ss);
                }
                Message message = Message.obtain();
                message.what = 1;
                sendMessageDelayed(message, 1000);
            }
        }
    }
}