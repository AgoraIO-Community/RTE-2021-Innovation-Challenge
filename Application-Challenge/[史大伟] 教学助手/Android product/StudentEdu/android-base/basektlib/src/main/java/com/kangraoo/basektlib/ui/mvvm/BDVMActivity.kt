package com.kangraoo.basektlib.ui.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * DataBinding mvvm
 */
abstract class BDVMActivity<B : ViewDataBinding, VM : BViewModel> : BVMActivity<VM>() {
    protected lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        binding = DataBindingUtil.setContentView(this, layoutResID)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}
