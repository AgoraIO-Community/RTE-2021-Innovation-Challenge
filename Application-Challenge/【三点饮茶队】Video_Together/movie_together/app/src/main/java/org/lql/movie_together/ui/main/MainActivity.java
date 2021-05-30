package org.lql.movie_together.ui.main;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.lql.movie_together.R;
import org.lql.movie_together.adapters.VideoEncResolutionAdapter;
import org.lql.movie_together.ui.base.BaseActivity;
import org.lql.movie_together.ui.layout.SettingsButtonDecoration;
import org.lql.movie_together.utils.Constant;
import org.lql.movie_together.utils.ConstantApp;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUIandEvent() {
        initActionBar();
        initChannelAndEnc();
        initResolutionAndFrameFps();
        initMsgDebug();
        initFaceDebug();
    }

    @Override
    protected void deInitUIandEvent() {

    }

    public void onClickJoin(View view) {
        forwardToRoom();
    }

    public void forwardToRoom() {
        EditText v_channel = (EditText) findViewById(R.id.channel_name);
        String channel = v_channel.getText().toString();
        vSettings().mChannelName = channel;

        // 存到缓存中
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ConstantApp.DEFAULT_VIDEO_CHANNEL_NAME, channel);
        editor.apply();

        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String encryption = v_encryption_key.getText().toString();
        vSettings().mEncryptionKey = encryption;

        Intent i = new Intent(MainActivity.this, RoomActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, encryption);
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE, getResources().getStringArray(R.array.encryption_mode_values)[vSettings().mEncryptionModeIndex]);

        startActivity(i);
    }

    // 初始化顶部导航栏
    private void initActionBar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.ard_agora_actionbar);
        }
    }

    // 初始化频道名和加密选项
    private void initChannelAndEnc() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String channelName = pref.getString(ConstantApp.DEFAULT_VIDEO_CHANNEL_NAME, "");

        EditText v_channel = (EditText) findViewById(R.id.channel_name);
        if (!"".equals(channelName)) {
            v_channel.setText(channelName);
            findViewById(R.id.button_join).setEnabled(true);
        }
        v_channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                findViewById(R.id.button_join).setEnabled(!isEmpty);
            }
        });

        Spinner encryptionSpinner = (Spinner) findViewById(R.id.encryption_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.encryption_mode_values, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        encryptionSpinner.setAdapter(adapter);

        encryptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vSettings().mEncryptionModeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        encryptionSpinner.setSelection(vSettings().mEncryptionModeIndex);

        String lastChannelName = vSettings().mChannelName;
        if (!TextUtils.isEmpty(lastChannelName)) {
            v_channel.setText(lastChannelName);
            v_channel.setSelection(lastChannelName.length());
        }

        EditText v_encryption_key = (EditText) findViewById(R.id.encryption_key);
        String lastEncryptionKey = vSettings().mEncryptionKey;
        if (!TextUtils.isEmpty(lastEncryptionKey)) {
            v_encryption_key.setText(lastEncryptionKey);
        }
    }

    // 初始化分辨率和帧率的设置
    private void initResolutionAndFrameFps() {
        RecyclerView videoResolutionList = (RecyclerView) findViewById(R.id.settings_video_resolution);
        videoResolutionList.setHasFixedSize(true);
        videoResolutionList.addItemDecoration(new SettingsButtonDecoration());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int resolutionIdx = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX);
        int fpsIdx = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX);

        VideoEncResolutionAdapter videoResolutionAdapter = new VideoEncResolutionAdapter(this, resolutionIdx);
        videoResolutionAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        videoResolutionList.setLayoutManager(layoutManager);
        videoResolutionList.setAdapter(videoResolutionAdapter);

        Spinner videoFpsSpinner = (Spinner) findViewById(R.id.settings_video_frame_rate);
        ArrayAdapter<CharSequence> videoFpsAdapter = ArrayAdapter.createFromResource(this,
                R.array.string_array_frame_rate, R.layout.simple_spinner_item_light);
        videoFpsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        videoFpsSpinner.setAdapter(videoFpsAdapter);
        videoFpsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        videoFpsSpinner.setSelection(fpsIdx);
    }

    // 是否显示消息列表
    private void initMsgDebug() {
        Switch debugSwitch = findViewById(R.id.debug_options);
        debugSwitch.setChecked(Constant.DEBUG_INFO_ENABLED);
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean withDebugInfo) {
                Constant.DEBUG_INFO_ENABLED = withDebugInfo;
            }
        });
    }

    private void initFaceDebug() {
        Switch debugFace = findViewById(R.id.face_options);
        debugFace.setChecked(Constant.DEBUG_FACE_ENABLED);
        debugFace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean withDebugInfo) {
                Constant.DEBUG_FACE_ENABLED = withDebugInfo;
            }
        });
    }
}