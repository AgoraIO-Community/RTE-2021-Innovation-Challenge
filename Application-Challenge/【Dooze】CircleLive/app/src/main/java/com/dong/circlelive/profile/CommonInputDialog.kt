package com.dong.circlelive.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.dong.circlelive.databinding.DialogCommonInputBinding
import com.dong.circlelive.main.MainActivity
import com.dong.circlelive.utils.dp
import com.dong.circlelive.utils.dpF
import com.dong.circlelive.utils.roundCorner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Create by dooze on 2021/5/24  8:29 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CommonInputDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCommonInputBinding

    private var confirmAction: ((content: String) -> Unit)? = null
    private var title: String? = null
    private var windowInsetsController: WindowInsetsControllerCompat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogCommonInputBinding.inflate(inflater, container, false)
        this.binding = binding
        binding.tvTitle.text = title
        binding.etInput.roundCorner(radius = 4.dpF(), paddingEnd = 0, paddingStart = 0, paddingTop = 0, paddingBottom = 0)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibDone.apply {
            isEnabled = false
            alpha = 0.3f
        }
        binding.etInput.addTextChangedListener {
            binding.ibDone.apply {
                isEnabled = it != null && it.trim().isNotEmpty()
                alpha = if (isEnabled) 1f else 0.3f
            }
        }

        binding.ibDone.setOnClickListener {
            confirmAction?.invoke(binding.etInput.text.toString())
            dismissAllowingStateLoss()
            confirmAction = null
        }


        dialog?.window?.let {
            windowInsetsController = WindowInsetsControllerCompat(it, binding.etInput)
        }

        binding.root.postDelayed({
            windowInsetsController?.show(WindowInsetsCompat.Type.ime())
        }, 100)


        MainActivity.imeHeight.observe(viewLifecycleOwner) { height ->
            binding.root.updatePadding(bottom = height + 16.dp())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        windowInsetsController = null
    }

    companion object {
        fun show(fm: FragmentManager, title: String, confirmAction: (content: String) -> Unit) {
            val fragment = CommonInputDialog()
            fragment.confirmAction = confirmAction
            fragment.title = title
            fragment.show(fm, "CommonInputDialog")
        }
    }
}