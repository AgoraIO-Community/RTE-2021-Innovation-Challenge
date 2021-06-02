package com.kangaroo.studentedu.ui.fragment

import android.os.Bundle
import android.view.View
import com.hyphenate.easeui.modules.contact.EaseContactListFragment
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.ui.activity.ChatActivity
import com.kangaroo.studentedu.ui.activity.ChatRoomActivity
import com.kangaroo.studentedu.ui.activity.GroupActivity
import com.kangraoo.basektlib.tools.log.ULog

/**
 * @author shidawei
 * 创建日期：2021/5/24
 * 描述：
 */
class ContactListFragment : EaseContactListFragment() {
    var isFInit = false

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        this.isFInit = true
        addHeader()
    }

    private fun addHeader() {
        contactLayout.contactList.addCustomItem(
            R.id.contact_header_item_group_list,
            R.drawable.ic_baseline_people_24,
            "群组"
        )
        contactLayout.contactList.addCustomItem(
            R.id.contact_header_item_chat_room_list,
            R.drawable.ic_baseline_people_24,
            "聊天室"
        )
        contactLayout.contactList.setOnCustomItemClickListener { view, position ->
            val item = contactLayout.contactList.customAdapter.getItem(position)
            when (item.id) {
                R.id.contact_header_item_group_list -> GroupActivity.startFrom(mContext)
                R.id.contact_header_item_chat_room_list -> ChatRoomActivity.startFrom(mContext)
            }
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        super.onItemClick(view, position)
        val item = contactLayout.contactList.getItem(position)
        ChatActivity.startFromSingle(mContext, item.username)
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
        requireActivity().title = "通讯录"
        ULog.i("(" + javaClass.simpleName + ".kt :" + 1 + ")", "(" + javaClass.simpleName + ".java :" + 1 + ")", getString(
            com.kangraoo.basektlib.R.string.libFragmentShow))
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