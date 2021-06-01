package com.kangraoo.basektlib.ui.mvvm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kangraoo.basektlib.ui.BFragment

abstract class BVMFragment<VM : BViewModel> : BFragment() {
    protected lateinit var _vm: VM

    abstract fun createVMInstance(): VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectBaseViewModel()
        initBaseInternalObserver()
    }

    private fun initBaseInternalObserver() {
        _vm.stateActionEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BStateActionEvent.ProgressingState -> if (it.isShow) showProgressingDialog() else dismissProgressDialog()
                is BStateActionEvent.ProgressingMessageState -> showProgressingDialog(it.message)
                is BStateActionEvent.ProgressingIntMessageState -> showProgressingDialog(it.message)
                is BStateActionEvent.ToastMsgState -> showToastMsg(it.message)
                is BStateActionEvent.ToastIntMsgState -> showToastMsg(it.message)
                is BStateActionEvent.ToastTipIntMsgState -> showToastMsg(it.iTipToast, it.message)
                is BStateActionEvent.ToastTipMsgState -> showToastMsg(it.iTipToast, it.message)
            }
        })
    }

    private fun injectBaseViewModel() {
        val vm = createVMInstance()
        _vm = ViewModelProvider(this, BViewModel.createViewModelFactory(vm))
            .get(vm::class.java)
        lifecycle.addObserver(_vm)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(_vm)
    }
}
