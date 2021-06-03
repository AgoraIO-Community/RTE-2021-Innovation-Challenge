package com.game.tingshuo.fragment

import android.os.Bundle
import android.view.View
import butterknife.BindView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.game.tingshuo.R
import com.game.tingshuo.app.BaseFragment
import com.game.tingshuo.databinding.Tab3FragmentBinding

class Tab3Fragment: BaseFragment<Tab3FragmentBinding>() {
    @BindView(R.id.refreshLayout) lateinit var refreshLayout:SmartRefreshLayout

    override fun getLayoutId(): Int {
        return R.layout.tab3_fragment
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        title?.text = resources.getText(R.string.tab3)
        back?.visibility=View.GONE

    }

}