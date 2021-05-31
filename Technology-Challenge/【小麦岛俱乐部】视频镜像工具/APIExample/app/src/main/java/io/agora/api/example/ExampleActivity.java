package io.agora.api.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import io.agora.api.component.Constant;
import io.agora.api.example.common.model.ExampleBean;
import io.agora.api.example.examples.advanced.ARCore;
import io.agora.api.example.examples.advanced.AdjustVolume;
import io.agora.api.example.examples.advanced.ChannelEncryption;
import io.agora.api.example.examples.advanced.CustomRemoteVideoRender;
import io.agora.api.example.examples.advanced.GeoFencing;
import io.agora.api.example.examples.advanced.HostAcrossChannel;
import io.agora.api.example.examples.advanced.InCallReport;
import io.agora.api.example.examples.advanced.JoinMultipleChannel;
import io.agora.api.example.examples.advanced.LiveStreaming;
import io.agora.api.example.examples.advanced.MediaPlayerKit;
import io.agora.api.example.examples.advanced.PlayAudioFiles;
import io.agora.api.example.examples.advanced.PreCallTest;
import io.agora.api.example.examples.advanced.ProcessAudioRawData;
import io.agora.api.example.examples.advanced.ProcessRawData;
import io.agora.api.example.examples.advanced.PushExternalVideo;
import io.agora.api.example.examples.advanced.SendDataStream;
import io.agora.api.example.examples.advanced.SetVideoProfile;
import io.agora.api.example.examples.advanced.SuperResolution;
import io.agora.api.example.examples.advanced.SwitchExternalVideo;
import io.agora.api.example.examples.advanced.SetAudioProfile;
import io.agora.api.example.examples.advanced.MultiProcess;
import io.agora.api.example.examples.advanced.VideoQuickSwitch;
import io.agora.api.example.examples.advanced.RTMPStreaming;
import io.agora.api.example.examples.advanced.StreamEncrypt;
import io.agora.api.example.examples.advanced.SwitchCameraScreenShare;
import io.agora.api.example.examples.advanced.VideoMetadata;
import io.agora.api.example.examples.advanced.VoiceEffects;
import io.agora.api.example.examples.advanced.customaudio.CustomAudioSource;
import io.agora.api.example.examples.basic.JoinChannelAudio;
import io.agora.api.example.examples.basic.JoinChannelVideo;

/**
 * @author cjw
 */
public class ExampleActivity extends AppCompatActivity {
    private static final String TAG = "ExampleActivity";

    private ExampleBean exampleBean;

    public static void instance(Activity activity, ExampleBean exampleBean) {
        Intent intent = new Intent(activity, ExampleActivity.class);
        intent.putExtra(Constant.DATA, exampleBean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_layout);
        exampleBean = getIntent().getParcelableExtra(Constant.DATA);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(exampleBean.getName());
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Fragment fragment;
        fragment = new JoinChannelVideo();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_Layout, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
