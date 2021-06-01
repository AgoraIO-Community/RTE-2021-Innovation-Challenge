package com.qgmodel.qggame.utils;

import com.qgmodel.qggame.entity.MessageBean;
import com.qgmodel.qggame.entity.MessageListBean;
import java.util.ArrayList;
import java.util.List;
import io.agora.rtm.RtmMessage;


public class MessageUtil {
    public static final int MAX_INPUT_NAME_LENGTH = 64;

    public static final int ACTIVITY_RESULT_CONN_ABORTED = 1;

    public static final String INTENT_EXTRA_IS_PEER_MODE = "chatMode";
    public static final String INTENT_EXTRA_USER_ID = "userId";
    public static final String INTENT_EXTRA_TARGET_NAME = "targetName";
    public static final String INTENT_EXTRA_FRIEND_NAME = "friendName";

    private static List<MessageListBean> messageListBeanList = new ArrayList<>();

    public static void addMessageListBeanList(MessageListBean messageListBean) {
        messageListBeanList.add(messageListBean);
    }

    public static void cleanMessageListBeanList() {
        messageListBeanList.clear();
    }

    public static MessageListBean getExistMessageListBean(String accountOther) {
        int ret = existMessageListBean(accountOther);
        if (ret > -1) {
            return messageListBeanList.remove(ret);
        }
        return null;
    }

    private static int existMessageListBean(String userId) {
        int size = messageListBeanList.size();
        for (int i = 0; i < size; i++) {
            if (messageListBeanList.get(i).getAccountOther().equals(userId)) {
                return i;
            }
        }
        return -1;
    }

    public static void addMessageBean(String account, RtmMessage msg) {
        MessageBean messageBean = new MessageBean(account, msg, false);
        int ret = existMessageListBean(account);
        if (ret == -1) {
            List<MessageBean> messageBeanList = new ArrayList<>();
            messageBeanList.add(messageBean);
            messageListBeanList.add(new MessageListBean(account, messageBeanList));

        } else {
            MessageListBean bean = messageListBeanList.remove(ret);
            List<MessageBean> messageBeanList = bean.getMessageBeanList();
            messageBeanList.add(messageBean);
            bean.setMessageBeanList(messageBeanList);
            messageListBeanList.add(bean);
        }
    }
}
