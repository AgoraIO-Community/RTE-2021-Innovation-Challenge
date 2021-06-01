package com.kangraoo.basektlib.ui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
/**
 * DataBinding mvvm
 */
abstract class BDVMFragment <B : ViewDataBinding, VM : BViewModel> : BVMFragment<VM>() {

    protected lateinit var binding: B
        private set

    override fun onBaseCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.isFInit = true
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}
