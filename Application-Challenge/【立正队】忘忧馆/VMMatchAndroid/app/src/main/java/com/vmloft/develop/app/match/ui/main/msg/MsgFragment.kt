package com.vmloft.develop.app.match.ui.main.msg

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.vmloft.develop.app.match.R
import com.vmloft.develop.library.common.base.BaseFragment
import com.vmloft.develop.library.common.utils.CUtils
import com.vmloft.develop.library.im.conversation.IMConversationFragment


/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：消息
 */
class MsgFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_msg

    override fun initUI() {
        super.initUI()
        CUtils.setDarkMode(activity!!, true)
        setTopTitle(R.string.nav_msg)

        setupFragment()
    }

    override fun initData() {
    }

    private fun setupFragment() {
        val fragment = IMConversationFragment.newInstance()
        val manager: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.replace(R.id.conversation_container, fragment)
        ft.commit()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.let {
            CUtils.setDarkMode(it, true)
        }
    }
}