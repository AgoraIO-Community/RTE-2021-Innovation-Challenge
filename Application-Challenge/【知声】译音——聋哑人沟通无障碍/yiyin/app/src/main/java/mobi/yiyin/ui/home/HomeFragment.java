package mobi.yiyin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import mobi.yiyin.ChatActivity;
import mobi.yiyin.R;
import mobi.yiyin.ui.AsrAudioActivity;
import mobi.yiyin.ui.AudioFileTranscriptionActivity;
import mobi.yiyin.ui.HandKeypointActivity;
import mobi.yiyin.ui.HandTrackActivity;
import mobi.yiyin.ui.RealTimeTranscriptionActivity;
import mobi.yiyin.ui.SoundDectActivity;
import mobi.yiyin.ui.TtsAnalyseActivity;
import mobi.yiyin.utils.Utils;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        root.findViewById(R.id.lv_write).setOnClickListener(this);
        root.findViewById(R.id.lv_chat_long).setOnClickListener(this);
        root.findViewById(R.id.lv_chat_short).setOnClickListener(this);
        root.findViewById(R.id.lv_voice_recognition).setOnClickListener(this);
        root.findViewById(R.id.lv_ai_subtitle).setOnClickListener(this);
        root.findViewById(R.id.lv_hand).setOnClickListener(this);
        root.findViewById(R.id.lv_asr_audio).setOnClickListener(this);
        root.findViewById(R.id.lv_tts_analyse).setOnClickListener(this);
        root.findViewById(R.id.lv_audio_file).setOnClickListener(this);
        root.findViewById(R.id.lv_asr_long).setOnClickListener(this);
        root.findViewById(R.id.lv_hand_track).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //滑板涂鸦
            case R.id.lv_write:
                Utils.yyOpenWXMin(getContext(), "gh_a6d00956f304", "pages/index/draw/draw");
                break;
            //小聊几句
            case R.id.lv_chat_short:
                startActivity(new Intent(getContext(), ChatActivity.class));
                break;
            //坐下长谈
            case R.id.lv_chat_long:
                //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                Utils.yyOpenWXMin(getContext(), "gh_a6d00956f304", "pages/index/longtalk/longtalk");
                break;
            //声纹识别
            case R.id.lv_voice_recognition:
                startActivity(new Intent(getContext(), SoundDectActivity.class));
                break;
            case R.id.lv_ai_subtitle:
                Toast.makeText(getContext(), "基于辅助功能的智能字幕软件系统，，目前在内测APP功能和软件测试，申请内测请联系客服QQ：1135309416", Toast.LENGTH_LONG).show();
                break;
            case R.id.lv_hand:
                startActivity(new Intent(getContext(), HandKeypointActivity.class));
                break;

            case R.id.lv_asr_audio:
                startActivity(new Intent(getContext(), AsrAudioActivity.class));
                break;
            case R.id.lv_tts_analyse:
                startActivity(new Intent(getContext(), TtsAnalyseActivity.class));
                break;
            case R.id.lv_audio_file:
                startActivity(new Intent(getContext(), AudioFileTranscriptionActivity.class));
                break;
            case R.id.lv_asr_long:
                startActivity(new Intent(getContext(), RealTimeTranscriptionActivity.class));
                break;
            case R.id.lv_hand_track:
                startActivity(new Intent(getContext(), HandTrackActivity.class));
                break;
            default:
                break;
        }

    }
}
