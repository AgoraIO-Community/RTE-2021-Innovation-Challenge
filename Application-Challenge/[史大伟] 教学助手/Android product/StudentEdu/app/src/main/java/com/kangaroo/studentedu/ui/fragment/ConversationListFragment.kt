package com.kangaroo.studentedu.ui.fragment

import android.os.Bundle
import android.view.View
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.manager.EaseSystemMsgManager
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.hyphenate.easeui.utils.EaseCommonUtils
import com.kangaroo.studentedu.ui.activity.ChatActivity
import com.kangraoo.basektlib.tools.log.ULog

/**
 * @author shidawei
 * 创建日期：2021/5/24
 * 描述：
 */
class ConversationListFragment: EaseConversationListFragment() {
    var isFInit = false

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        this.isFInit = true
    }

    override fun onItemClick(view: View?, position: Int) {
        super.onItemClick(view, position)
        val item = conversationListLayout.getItem(position).info
        if (item is EMConversation) {
            if (EaseSystemMsgManager.getInstance().isSystemConversation(
                    item
                )
            ) {
//                SystemMsgsActivity.actionStart(mContext)
            } else {
                ChatActivity.startFrom(
                    mContext, item.conversationId(), EaseCommonUtils.getChatType(
                        item
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onLibResume()
    }

    open fun onLibResume() {
        if (this.isFInit && !this.isResume) {
            if (mMyHidden != null && !mMyHidden!!) {
                this.isResume = true
                this.onFragmentResume()
            } else if (mMyHidden == null && this.getUserVisibleHint()) {
                this.isResume = true
                this.onFragmentResume()
            }
        }
    }

    /**
     * 只有在展示的fragment 才会调用该方法，不展示的fragment 不会调用
     */
    open fun onFragmentResume() {
        requireActivity().title = "消息列表"
        ULog.i("(" + javaClass.simpleName + ".kt :" + 1 + ")", "(" + javaClass.simpleName + ".java :" + 1 + ")", getString(
            com.kangraoo.basektlib.R.string.libFragmentShow))
        onRefresh()
    }

    //  private boolean isPause = true;
    var isResume = false

    /**
     * 需要修改
     */
    override fun onPause() {
        super.onPause()
        onLibPause()
    }

    open fun onLibPause() {
        if (this.isFInit && this.isResume) {
            this.isResume = false
            this.onFragmentPause()
        }
    }

    /**
     * 只有在展示的fragment 才会调用该方法，不展示的fragment 不会调用
     */
    open fun onFragmentPause() {
        ULog.i("(" + javaClass.simpleName + ".kt :" + 1 + ")", "(" + javaClass.simpleName + ".java :" + 1 + ")", getString(
            com.kangraoo.basektlib.R.string.libFragmentClose))
    }

    fun getContentView(): View? {
        return this.getContentView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (this.isFInit && this.getContentView() != null) {
            if (isVisibleToUser) {
                this.onLibResume()
            } else {
                this.onLibPause()
            }
        }
    }

    var mMyHidden: Boolean? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (this.isFInit) {
            mMyHidden = hidden
            if (hidden) {
                onLibPause()
            } else {
                onLibResume()
            }
        }
    }

}