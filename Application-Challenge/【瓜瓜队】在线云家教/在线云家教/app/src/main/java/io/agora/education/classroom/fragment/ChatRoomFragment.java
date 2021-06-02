package io.agora.education.classroom.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.agora.base.ToastManager;
import io.agora.base.network.RetrofitManager;
import io.agora.education.BuildConfig;
import io.agora.education.EduApplication;
import io.agora.education.R;
import io.agora.education.api.EduCallback;
import io.agora.education.api.message.EduChatMsg;
import io.agora.education.api.message.EduChatMsgType;
import io.agora.education.base.BaseCallback;
import io.agora.education.base.BaseFragment;
import io.agora.education.classroom.BaseClassActivity;
import io.agora.education.classroom.ReplayActivity;
import io.agora.education.classroom.adapter.MessageListAdapter;
import io.agora.education.classroom.bean.msg.ChannelMsg;
import io.agora.education.classroom.bean.record.RecordMsg;
import io.agora.education.service.RecordService;
import io.agora.education.service.bean.response.RecordRes;

import static io.agora.education.api.BuildConfig.API_BASE_URL;

public class ChatRoomFragment extends BaseFragment implements OnItemChildClickListener, View.OnKeyListener {
    public static final String TAG = ChatRoomFragment.class.getSimpleName();

    @BindView(R.id.rcv_msg)
    protected RecyclerView rcv_msg;
    @BindView(R.id.edit_send_msg)
    protected EditText edit_send_msg;

    private MessageListAdapter adapter;
    private boolean isMuteAll = false;
    private boolean isMuteLocal;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_chatroom;
    }

    @Override
    protected void initData() {
        adapter = new MessageListAdapter();
        adapter.setOnItemChildClickListener(this);
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        rcv_msg.setLayoutManager(layoutManager);
        rcv_msg.setAdapter(adapter);
        edit_send_msg.setOnKeyListener(this);
        setEditTextEnable(!(this.isMuteAll));
    }

    public void setMuteAll(boolean isMuteAll) {
        this.isMuteAll = isMuteAll;
        setEditTextEnable(!(this.isMuteAll));
    }

    public void setMuteLocal(boolean isMuteLocal) {
        this.isMuteLocal = isMuteLocal;
        setEditTextEnable(!(this.isMuteAll || isMuteLocal));
    }

    private void setEditTextEnable(boolean isEnable) {
        runOnUiThread(() -> {
            if (edit_send_msg != null) {
                edit_send_msg.setEnabled(isEnable);
                if (isEnable) {
                    edit_send_msg.setHint(R.string.hint_im_message);
                } else {
                    edit_send_msg.setHint(R.string.chat_muting);
                }
            }
        });
    }

    public void addMessage(ChannelMsg.ChatMsg chatMsg) {
        runOnUiThread(() -> {
            if (rcv_msg != null) {
                adapter.addData(chatMsg);
                rcv_msg.scrollToPosition(adapter.getItemPosition(chatMsg));
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tv_content) {
            Object object = adapter.getItem(position);
            if (object instanceof RecordMsg) {
                RecordMsg msg = (RecordMsg) object;
                if (context instanceof BaseClassActivity) {
                    fetchRecordList(EduApplication.getAppId(), msg.getRoomUuid(), nextId,
                            new EduCallback<RecordRes.RecordDetail>() {
                                @Override
                                public void onSuccess(@Nullable RecordRes.RecordDetail recordDetail) {
                                    if (recordDetail.isFinished()) {
                                        String url = recordDetail.url;
                                        if (!TextUtils.isEmpty(url)) {
                                            Intent intent = new Intent(context, ReplayActivity.class);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_ROOM_ID, recordDetail.roomUuid);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_START_TIME, recordDetail.startTime);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_END_TIME, recordDetail.endTime);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_URL, url);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_ID, recordDetail.boardId);
                                            intent.putExtra(ReplayActivity.WHITEBOARD_TOKEN, recordDetail.boardToken);
                                            startActivity(intent);
                                        }
                                    } else {
                                        ToastManager.showShort(R.string.wait_record);
                                    }
                                }

                                @Override
                                public void onFailure(int code, @Nullable String reason) {
                                }
                            });
                }
            }
        }
    }

    private int nextId = 0, total = 0;
    private List<RecordRes.RecordDetail> recordDetails = new ArrayList<>();

    private void fetchRecordList(String appId, String roomId, int next, EduCallback<RecordRes.RecordDetail> callback) {
        RetrofitManager.instance().getService(API_BASE_URL, RecordService.class)
                .record(appId, roomId, next)
                .enqueue(new BaseCallback<>(data -> {
                    total = data.total;
                    nextId = data.nextId;
                    recordDetails.addAll(data.list);
                    if (recordDetails.size() < total) {
                        fetchRecordList(appId, roomId, nextId, callback);
                    } else {
                        nextId = total = 0;
                        long max = 0;
                        RecordRes.RecordDetail recordDetail = null;
                        for (RecordRes.RecordDetail detail : recordDetails) {
                            if (detail.startTime > max) {
                                max = detail.startTime;
                                recordDetail = detail;
                            }
                        }
                        recordDetails.clear();
                        callback.onSuccess(recordDetail);
                    }
                }));
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (!edit_send_msg.isEnabled()) {
            return false;
        }
        String text = edit_send_msg.getText().toString();
        if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction() && text.trim().length() > 0) {
            if (context instanceof BaseClassActivity) {
                edit_send_msg.setText("");
                BaseClassActivity activity = (BaseClassActivity) getActivity();
                /*本地消息直接添加*/
                ChannelMsg.ChatMsg msg = new ChannelMsg.ChatMsg(activity.getLocalUser().getUserInfo(), text,
                        EduChatMsgType.Text.getValue());
                msg.isMe = true;
                addMessage(msg);
                activity.sendRoomChatMsg(text, new EduCallback<EduChatMsg>() {
                    @Override
                    public void onSuccess(@Nullable EduChatMsg res) {
                    }

                    @Override
                    public void onFailure(int code, @Nullable String reason) {
                    }
                });
            }
            return true;
        }
        return false;
    }

}
