package com.dong.circlelive.im

import android.view.View
import com.dong.circlelive.R
import com.dong.circlelive.showOtherFragment
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.constants.EaseConstant
import com.hyphenate.easeui.modules.chat.EaseChatFragment
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment

/**
 * Create by dooze on 2021/5/25  3:40 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ConversationListFragment : EaseConversationListFragment() {

    override fun onItemClick(view: View, position: Int) {
        super.onItemClick(view, position)
        val conversationInfo = conversationListLayout.getItem(position);
        if (conversationInfo != null) {
            val conversation = conversationInfo.info as EMConversation

            val fragment = EaseChatFragment.newInstance(
                conversation.conversationId(), if (conversation.isGroup) {
                    EaseConstant.CHATTYPE_GROUP;
                } else {
                    EaseConstant.CHATTYPE_SINGLE;
                }
            )
            showOtherFragment(fragment, R.id.top_fragment_container)
        }
    }
}