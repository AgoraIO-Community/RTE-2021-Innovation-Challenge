package io.agora.education.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.education.BuildConfig;
import io.agora.education.R;
import io.agora.education.base.BaseActivity;
import io.agora.education.classroom.fragment.ReplayBoardFragment;


public class ReplayActivity extends BaseActivity {
    private static final String TAG = "ReplayActivity";

    public static final String WHITEBOARD_ROOM_ID = "whiteboardRoomId";
    public static final String WHITEBOARD_START_TIME = "whiteboardStartTime";
    public static final String WHITEBOARD_END_TIME = "whiteboardEndTime";
    public static final String WHITEBOARD_URL = "whiteboardUrl";
    public static final String WHITEBOARD_ID = "whiteboardId";
    public static final String WHITEBOARD_TOKEN = "whiteboardToken";

    @BindView(R.id.video_view)
    protected PlayerView video_view;

    private ReplayBoardFragment replayBoardFragment;
    private String url, roomId;
    private long startTime, endTime;
    private String boardId, boardToken;
    private boolean isInit;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_replay;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra(WHITEBOARD_URL);
        if (!url.startsWith("http")) {
            url = BuildConfig.REPLAY_BASE_URL.concat("/").concat(url);
        }
        Log.e(TAG, "回放链接:" + url);
        roomId = intent.getStringExtra(WHITEBOARD_ROOM_ID);
        startTime = intent.getLongExtra(WHITEBOARD_START_TIME, 0);
        endTime = intent.getLongExtra(WHITEBOARD_END_TIME, 0);
        boardId = intent.getStringExtra(WHITEBOARD_ID);
        boardToken = intent.getStringExtra(WHITEBOARD_TOKEN);
    }

    @Override
    protected void initView() {
        video_view.setUseController(false);
        video_view.setVisibility(!TextUtils.isEmpty(url) ? View.VISIBLE : View.GONE);
        findViewById(R.id.iv_temp).setVisibility(TextUtils.isEmpty(url) ? View.VISIBLE : View.GONE);

        replayBoardFragment = new ReplayBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(WHITEBOARD_START_TIME, startTime);
        bundle.putLong(WHITEBOARD_END_TIME, endTime);
        replayBoardFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_whiteboard, replayBoardFragment)
                .commitNow();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (!isInit) {
            replayBoardFragment.initReplayWithRoomToken(boardId, boardToken);
            replayBoardFragment.setPlayer(video_view, url);
            isInit = true;
        }
    }

    @Override
    protected void onDestroy() {
        replayBoardFragment.releaseReplay();
        super.onDestroy();
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        finish();
    }

}
