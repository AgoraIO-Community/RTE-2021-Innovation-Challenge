package io.agora.education.classroom.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.Appliance;
import com.herewhite.sdk.domain.CameraBound;
import com.herewhite.sdk.domain.GlobalState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.SceneState;
import com.herewhite.sdk.domain.WhiteDisplayerState;

import butterknife.BindView;
import butterknife.OnTouch;
import io.agora.base.ToastManager;
import io.agora.education.R;
import io.agora.education.base.BaseFragment;
import io.agora.education.classroom.bean.board.BoardState;
import io.agora.education.classroom.widget.whiteboard.ApplianceView;
import io.agora.education.classroom.widget.whiteboard.ColorPicker;
import io.agora.education.classroom.widget.whiteboard.PageControlView;
import io.agora.education.util.ColorUtil;
import io.agora.whiteboard.netless.listener.BoardEventListener;
import io.agora.whiteboard.netless.manager.BoardManager;

public class WhiteBoardFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener,
        PageControlView.PageControlListener, BoardEventListener {
    private static final String TAG = "WhiteBoardFragment";

    @BindView(R.id.white_board_view)
    protected WhiteboardView white_board_view;
    @BindView(R.id.appliance_view)
    protected ApplianceView appliance_view;
    @BindView(R.id.color_select_view)
    protected ColorPicker color_select_view;
    @BindView(R.id.page_control_view)
    protected PageControlView page_control_view;
    @BindView(R.id.pb_loading)
    protected ProgressBar pb_loading;

    private WhiteSdk whiteSdk;
    private BoardManager boardManager = new BoardManager();
    private String curLocalUuid, curLocalToken, localUserUuid;
    private final double miniScale = 0.1d;
    private final double maxScale = 10d;
    /*初始化时不进行相关提示*/
    private boolean inputTips = false;
    private boolean transform = false;
    /*是否允许在开启白板跟随的情况下，进行书写(仅仅书写，不包括移动缩放)*/
    private boolean inputWhileFollow = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GlobalStateChangeListener) {
            listener = (GlobalStateChangeListener) context;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_white_board;
    }

    @Override
    protected void initData() {
        WhiteDisplayerState.setCustomGlobalStateClass(BoardState.class);
//        WhiteSdkConfiguration configuration = new WhiteSdkConfiguration(DeviceType.touch, 10, 0.1);
        WhiteSdkConfiguration configuration = new WhiteSdkConfiguration(getString(R.string.whiteboard_app_id), true);
        whiteSdk = new WhiteSdk(white_board_view, context, configuration);
        boardManager.setListener(this);
    }

    @Override
    protected void initView() {
        white_board_view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            boardManager.refreshViewSize();
        });
        appliance_view.setVisibility(boardManager.isDisableDeviceInputs() ? View.GONE : View.VISIBLE);
        appliance_view.setOnCheckedChangeListener(this);
        color_select_view.setChangedListener(color -> {
            appliance_view.check(appliance_view.getApplianceId(boardManager.getAppliance()));
            boardManager.setStrokeColor(ColorUtil.colorToArray(color));
        });
        /*白板控制按钮暂时不予适用*/
//        page_control_view.setListener(this);
    }

    public void setInputWhileFollow(boolean inputWhileFollow) {
        this.inputWhileFollow = inputWhileFollow;
    }

    public void initBoardWithRoomToken(String uuid, String boardToken, String localUserUuid) {
        if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(boardToken)) {
            return;
        }
        this.curLocalUuid = uuid;
        this.curLocalToken = boardToken;
        this.localUserUuid = localUserUuid;
        boardManager.getRoomPhase(new Promise<RoomPhase>() {
            @Override
            public void then(RoomPhase phase) {
                Log.e(TAG, "then->" + phase.name());
                if (phase != RoomPhase.connected) {
                    runOnUiThread(() -> pb_loading.setVisibility(View.VISIBLE));
                    RoomParams params = new RoomParams(uuid, boardToken);
                    params.setCameraBound(new CameraBound(miniScale, maxScale));
                    boardManager.init(whiteSdk, params);
                }
            }

            @Override
            public void catchEx(SDKError t) {
                Log.e(TAG, "catchEx->" + t.getMessage());
                ToastManager.showShort(t.getMessage());
            }
        });
    }

    public void disableDeviceInputs(boolean disabled) {
        boolean a = boardManager.isDisableDeviceInputs();
        if (disabled != a) {
            if (!inputTips) {
                inputTips = true;
            } else {
                ToastManager.showShort(disabled ? R.string.revoke_board : R.string.authorize_board);
            }
        }
        if (appliance_view != null) {
            appliance_view.setVisibility(disabled ? View.GONE : View.VISIBLE);
        }
        if (page_control_view != null) {
            page_control_view.setVisibility(disabled ? View.GONE : View.VISIBLE);
        }
        boardManager.disableDeviceInputs(disabled);
    }

    public void disableCameraTransform(boolean disabled) {
        boolean a = boardManager.isDisableCameraTransform();
        if (disabled != a) {
            if (disabled) {
                if (!transform) {
                    transform = true;
                } else {
//                    ToastManager.showShort(R.string.follow_tips);
                }
                boardManager.disableDeviceInputsTemporary(true);
            } else {
                boardManager.disableDeviceInputsTemporary(boardManager.isDisableDeviceInputs());
            }
        }
        boardManager.disableCameraTransform(disabled);
    }

    public void setWritable(boolean writable) {
        boardManager.setWritable(writable);
    }

    public void releaseBoard() {
        boardManager.disconnect();
    }

    @OnTouch(R.id.white_board_view)
    boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            white_board_view.requestFocus();
            if (boardManager.isDisableCameraTransform() && !boardManager.isDisableDeviceInputs()) {
                ToastManager.showShort(R.string.follow_tips);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        color_select_view.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.tool_selector:
                boardManager.setAppliance(Appliance.SELECTOR);
                break;
            case R.id.tool_pencil:
                boardManager.setAppliance(Appliance.PENCIL);
                break;
            case R.id.tool_text:
                boardManager.setAppliance(Appliance.TEXT);
                break;
            case R.id.tool_eraser:
                boardManager.setAppliance(Appliance.ERASER);
                break;
            case R.id.tool_color:
                color_select_view.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void toStart() {
        boardManager.setSceneIndex(0);
    }

    @Override
    public void toPrevious() {
        boardManager.pptPreviousStep();
    }

    @Override
    public void toNext() {
        boardManager.pptNextStep();
    }

    @Override
    public void toEnd() {
        boardManager.setSceneIndex(boardManager.getSceneCount() - 1);
    }

    @Override
    public void onJoinSuccess(GlobalState state) {
        Log.e(TAG, "onJoinSuccess->" + new Gson().toJson(state));
        if (listener != null) {
            listener.onGlobalStateChanged(state);
        }
    }

    @Override
    public void onRoomPhaseChanged(RoomPhase phase) {
        Log.e(TAG, "onRoomPhaseChanged->" + phase.name());
        pb_loading.setVisibility(phase == RoomPhase.connected ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onGlobalStateChanged(GlobalState state) {
        if (listener != null) {
            listener.onGlobalStateChanged(state);
        }
    }

    @Override
    public void onSceneStateChanged(SceneState state) {
        Log.e(TAG, "onSceneStateChanged");
        page_control_view.setPageIndex(state.getIndex(), state.getScenes().length);
    }

    @Override
    public void onMemberStateChanged(MemberState state) {
        appliance_view.check(appliance_view.getApplianceId(state.getCurrentApplianceName()));
    }

    @Override
    public void onDisconnectWithError(Exception e) {
        initBoardWithRoomToken(curLocalUuid, curLocalToken, localUserUuid);
    }

    private GlobalStateChangeListener listener;

    public interface GlobalStateChangeListener {
        void onGlobalStateChanged(GlobalState state);
    }

}
