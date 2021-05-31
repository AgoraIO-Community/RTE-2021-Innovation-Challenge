package com.qgmodel.qggame.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.qgmodel.qggame.R;
import com.qgmodel.qggame.databinding.ActivityMessageBinding;
import com.qgmodel.qggame.entity.MessageBean;
import com.qgmodel.qggame.entity.MessageListBean;
import com.qgmodel.qggame.rtmtutorial.AGApplication;
import com.qgmodel.qggame.rtmtutorial.ChatManager;
import com.qgmodel.qggame.utils.ImageUtil;
import com.qgmodel.qggame.utils.MessageUtil;
import com.qgmodel.qggame.utils.SPUtils;
import com.qgmodel.qggame.utils.ViewUtils;
import com.qgmodel.qggame.view.adapter.MessageAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;

public class MessageActivity extends AppCompatActivity {

    ActivityMessageBinding messageBinding;

    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    private String mUserId = "";
    private String mPeerId = "";
    private String mPeerName = "";
    
    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private RtmClientListener mClientListener;

    private boolean mIsInChat = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageBinding = ActivityMessageBinding.inflate(LayoutInflater.from(this));
        initView();
        setContentView(messageBinding.getRoot());
    }

    private void initView() {

        login();

        mClientListener = new MyRtmClientListener();
        mChatManager.registerListener(mClientListener);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(MessageUtil.INTENT_EXTRA_USER_ID);
        mPeerId = intent.getStringExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME);
        mPeerName = intent.getStringExtra(MessageUtil.INTENT_EXTRA_FRIEND_NAME);

        messageBinding.friendTalkToolbar.setTitle(mPeerName);
        ViewUtils.setTitleCenter(messageBinding.friendTalkToolbar);
        messageBinding.friendTalkToolbar.setNavigationIcon(R.mipmap.back);
        messageBinding.friendTalkToolbar.setNavigationOnClickListener(view -> finish());

        MessageListBean messageListBean = MessageUtil.getExistMessageListBean(mPeerId);
        if (messageListBean != null) {
            mMessageBeanList.addAll(messageListBean.getMessageBeanList());
        }

        MessageListBean offlineMessageBean = new MessageListBean(mPeerId, mChatManager);
        mMessageBeanList.addAll(offlineMessageBean.getMessageBeanList());
        mChatManager.removeAllOfflineMessages(mPeerId);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
                if (!TextUtils.isEmpty(message.getCacheFile())) {
                    Glide.with(this).load(message.getCacheFile()).into(messageBinding.bigImage);
                    messageBinding.bigImage.setVisibility(View.VISIBLE);
                } else {
                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String file) {
                            message.setCacheFile(file);
                            runOnUiThread(() -> {
                                Glide.with(MessageActivity.this).load(file).into(messageBinding.bigImage);
                                messageBinding.bigImage.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {

                        }
                    });
                }
            }
        });

        messageBinding.messageList.setLayoutManager(layoutManager);
        messageBinding.messageList.setAdapter(mMessageAdapter);

    }

    private void login() {

        mChatManager = AGApplication.the().getChatManager();
        mChatManager.isOfflineMessageEnabled();
        mChatManager.enableOfflineMessage(true);

        mRtmClient = mChatManager.getRtmClient();

        mIsInChat = true;
        mRtmClient.login(null, SPUtils.getString(this,"uid"), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.e("ERTY", "login success");
                showToast("登录服务器成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e("ERTY", "login failed: " + errorInfo);
                mIsInChat = false;
                showToast("登录服务器失败");

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MessageUtil.addMessageListBeanList(new MessageListBean(mPeerId, mMessageBeanList));
        mChatManager.unregisterListener(mClientListener);

        if (mIsInChat) {
            doLogout();
        }
    }

    private void doLogout() {
        mRtmClient.logout(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final String file = resultUri.getPath();
                ImageUtil.uploadImage(this, mRtmClient, file, new ResultCallback<RtmImageMessage>() {
                    @Override
                    public void onSuccess(final RtmImageMessage rtmImageMessage) {
                        runOnUiThread(() -> {
                            MessageBean messageBean = new MessageBean(mUserId, rtmImageMessage, true);
                            messageBean.setCacheFile(file);
                            mMessageBeanList.add(messageBean);
                            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                            messageBinding.messageList.scrollToPosition(mMessageBeanList.size() - 1);


                            sendPeerMessage(rtmImageMessage);

                        });
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError().printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selection_chat_btn:
                String msg = messageBinding.messageEdittiext.getText().toString();
                if (!msg.equals("")) {
                    RtmMessage message = mRtmClient.createMessage();
                    message.setText(msg);

                    MessageBean messageBean = new MessageBean(mUserId, message, true);
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    messageBinding.messageList.scrollToPosition(mMessageBeanList.size() - 1);

                    sendPeerMessage(message);

                }
                messageBinding.messageEdittiext.setText("");
                break;


            case R.id.big_image:
                messageBinding.bigImage.setVisibility(View.GONE);
                break;
        }
    }



    /**
     * 发信息给好友
     */
    private void sendPeerMessage(final RtmMessage message) {
        mRtmClient.sendMessageToPeer(mPeerId, message, mChatManager.getSendMessageOptions(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {

                final int errorCode = errorInfo.getErrorCode();
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT:
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE:
                            showToast("发送消息失败");
                            break;
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE:
                            showToast("好友不在线");
                            break;

                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER:
                            showToast("好友不在线，服务器已保存信息");
                            break;
                    }
                });
            }
        });
    }

    private class MyRtmClientListener implements RtmClientListener {
        @Override
        public void onConnectionStateChanged(final int state, int reason) {
            runOnUiThread(() -> {
                switch (state) {
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                        showToast("连接中...");
                        break;
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                        showToast("连接失败");
                        setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                        finish();
                        break;
                }
            });
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mPeerId)) {
                    MessageBean messageBean = new MessageBean(peerId, message, false);
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    messageBinding.messageList.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, message);
                }
            });
        }

        @Override
        public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mPeerId)) {
                    MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    messageBinding.messageList.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, rtmImageMessage);
                }
            });
        }

        @Override
        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

        }

        @Override
        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }
    }

    private void showToast(final String text) {
        runOnUiThread(() -> Toast.makeText(MessageActivity.this, text, Toast.LENGTH_SHORT).show());
    }
}

