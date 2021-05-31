package com.kangraoo.basektlib.ui.mvvm

import android.os.Bundle
import androidx.lifecycle.*
import com.kangraoo.basektlib.ui.BActivity

abstract class BVMActivity<VM : BViewModel> : BActivity() {

    protected lateinit var _vm: VM

    abstract fun createVMInstance(): VM

    override fun onViewCreatedBefore(savedInstanceState: Bundle?) {
        super.onViewCreatedBefore(savedInstanceState)
        injectBaseViewModel()
        initBaseInternalObserver()
    }

    private fun initBaseInternalObserver() {
        _vm.stateActionEvent.observe(this, Observer {
            stateActionOnState(it)
        })
    }

    open fun stateActionOnState(bStateActionEvent: BStateActionEvent?) {
        when (bStateActionEvent) {
            is BStateActionEvent.ProgressingState -> if (bStateActionEvent.isShow) showProgressingDialog() else dismissProgressDialog()
            is BStateActionEvent.ProgressingMessageState -> showProgressingDialog(bStateActionEvent.message)
            is BStateActionEvent.ProgressingIntMessageState -> showProgressingDialog(bStateActionEvent.message)
            is BStateActionEvent.ToastMsgState -> showToastMsg(bStateActionEvent.message)
            is BStateActionEvent.ToastIntMsgState -> showToastMsg(bStateActionEvent.message)
            is BStateActionEvent.ToastTipIntMsgState -> showToastMsg(bStateActionEvent.iTipToast, bStateActionEvent.message)
            is BStateActionEvent.ToastTipMsgState -> showToastMsg(bStateActionEvent.iTipToast, bStateActionEvent.message)
        }
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
