package mobi.yiyin.ui;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConstants;

import java.util.ArrayList;

import mobi.yiyin.R;
import mobi.yiyin.base.BaseActivity;
import mobi.yiyin.manager.RealTimeTranscriptionManager;

public class RealTimeTranscriptionActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RealTimeTransActivity";

    private RealTimeTranscriptionManager mRealTimeManager;
    private TextView resultTv;
    private TextView errorTv;
    public boolean isSelected = false;

    private final StringBuffer recognizerResult = new StringBuffer();
    private ImageView startBtn;

    private static final String LANGUAGE_ZH = MLSpeechRealTimeTranscriptionConstants.LAN_ZH_CN;
    private static final String LANGUAGE_EN = MLSpeechRealTimeTranscriptionConstants.LAN_EN_US;
    private static final String LANGUAGE_FR = MLSpeechRealTimeTranscriptionConstants.LAN_FR_FR;
    private String mLanguage = LANGUAGE_ZH;
    private Spinner langSp;
    private ImageView asrLongVoice;
    private TextView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_transcription);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("实时长语音识别");
        initView();

    }

    private void initView() {
        startBtn = findViewById(R.id.ars_start_btn);
        startBtn.setOnClickListener(this);
        resultTv = findViewById(R.id.real_time_output);
        errorTv = findViewById(R.id.tv_record_result);
        langSp = findViewById(R.id.language_sp);
        asrLongVoice = findViewById(R.id.id_recorder_asr_voice);
        if (isSelected) {
            isSelected = false;
            startBtn.setBackground(getResources().getDrawable(R.drawable.ic_baseline_stop_circle_24));
        } else {
            isSelected = true;
            startBtn.setBackground(getResources().getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
        }

        langSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) langSp.getSelectedItem();
                if (str.equals(getString(R.string.chinese))) {
                    mLanguage = LANGUAGE_ZH;
                } else if (str.equals(getString(R.string.english_choose))) {
                    mLanguage = LANGUAGE_EN;
                } else if (str.equals(getString(R.string.french))) {
                    mLanguage = LANGUAGE_FR;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealTimeManager != null) {
            MLSpeechRealTimeTranscription.getInstance().destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ars_start_btn:
                if (isSelected) {
                    isSelected = false;
                    startBtn.setBackground(getResources().getDrawable(R.drawable.sound_dect_end_btn));
                    if (mRealTimeManager == null) {
                        setSpeechListener();
                        resultTv.setText(getString(R.string.you_may_speak));
                        errorTv.setText("");
                        asrLongVoice.setImageResource(R.drawable.animlist);
                        AnimationDrawable animationDrawable = (AnimationDrawable) asrLongVoice.getDrawable();
                        animationDrawable.start();
                    } else if (!mRealTimeManager.ismIsListening()) {
                        mRealTimeManager.destroy();
                        setSpeechListener();
                        resultTv.setText(getString(R.string.you_may_speak));
                        errorTv.setText("");
                    }
                } else {
                    isSelected = true;
                    startBtn.setBackground(getResources().getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                    if (mRealTimeManager != null) {
                        if (recognizerResult.length() > 0) {
                            recognizerResult.delete(0, recognizerResult.length() - 1);
                        }
                        resultTv.setText(getString(R.string.destroied));

                        MLSpeechRealTimeTranscription.getInstance().destroy();
                        mRealTimeManager = null;

                        asrLongVoice.setImageResource(R.drawable.animlist);
                        AnimationDrawable animationDrawable = (AnimationDrawable) asrLongVoice.getDrawable();
                        animationDrawable.stop();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setSpeechListener() {
        mRealTimeManager = new RealTimeTranscriptionManager(this, mLanguage, new RealTimeTranscriptionManager.onResultsReady() {

            @Override
            public void onRecognizingResults(ArrayList<String> results, int status) {
                if (results != null && results.size() > 0) {
                    if (results.size() == 1) {
                        resultTv.setText(results.get(0));
                        if (status == RealTimeTranscriptionManager.RESULT_FINAL) {
                            if (!TextUtils.isEmpty(recognizerResult.toString())) {
                                //recognizerResult.append(",");
                            }
                            recognizerResult.append(results.get(0));
                            errorTv.setText(recognizerResult.toString());
                        }
                    } else {
                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String result : results) {
                            sb.append(result).append("\n");
                        }
                        resultTv.setText(sb.toString());

                        if (status == RealTimeTranscriptionManager.RESULT_FINAL) {
                            if (!TextUtils.isEmpty(recognizerResult.toString())) {
                                //recognizerResult.append(",");
                            }
                            recognizerResult.append(sb.toString());
                            errorTv.setText(recognizerResult.toString());
                        }
                    }
                }
            }

            @Override
            public void onError(int error) {
                String tip = getPrompt(error);
                if (!TextUtils.isEmpty(tip)) {
                    Log.d(TAG, "onError: " + error + " Update page: " + tip);
                    tip = "ERROR: " + tip;
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText(tip);
                    //去掉 you may speak now
                    resultTv.setText("");
/*                    if (getString(R.string.you_may_speak).equals(resultTv.getText().toString())) {
                        resultTv.setText("");
                    }*/
                }
            }
        });
    }

    private String getPrompt(int errorCode) {
        Log.d(TAG, "Error Code： " + errorCode + " Other errors");
        String error_text;
        switch (errorCode) {
            case MLSpeechRealTimeTranscriptionConstants.ERR_NO_NETWORK:
                error_text = getString(R.string.no_intnet);
                break;
            case MLSpeechRealTimeTranscriptionConstants.ERR_SERVICE_UNAVAILABLE:
                error_text = getString(R.string.no_service);
                break;
            case MLSpeechRealTimeTranscriptionConstants.ERR_SERVICE_CREDIT:
                error_text = getString(R.string.no_balance);
                break;
            default:
                error_text = getString(R.string.errorcode) + errorCode + ",  " + getString(R.string.Others);
                break;
        }
        return error_text;
    }
}
