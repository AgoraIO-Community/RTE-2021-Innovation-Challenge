package com.dong.circlelive.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.dong.circlelive.R
import com.dong.circlelive.databinding.DialogCommonTipsBinding
import com.dong.circlelive.getString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Create by dooze on 2021/5/24  8:29 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CommonTipsDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCommonTipsBinding

    private var confirmAction: (() -> Unit)? = null
    private var title: String? = null
    private var subtitle: String? = null
    private var ok: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogCommonTipsBinding.inflate(inflater, container, false)
        this.binding = binding
        binding.tvTitle.text = title
        binding.tvSubtitle.text = subtitle
        binding.btnOk.text = ok
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            dismissAllowingStateLoss()
            confirmAction?.invoke()
            confirmAction = null
        }
    }

    companion object {
        fun show(
            fm: FragmentManager,
            title: String,
            subTitle: String,
            ok: String = getString(R.string.okay),
            confirmAction: (() -> Unit)? = null
        ) {
            val fragment = CommonTipsDialog()
            fragment.confirmAction = confirmAction
            fragment.title = title
            fragment.subtitle = subTitle
            fragment.ok = ok
            fragment.show(fm, "CommonTipsDialog")
        }
    }
}