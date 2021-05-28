package com.hustunique.vlive.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hustunique.vlive.R
import com.hustunique.vlive.databinding.FragmentFloatControlBinding
import com.hustunique.vlive.local.MemberInfo
import com.hustunique.vlive.util.ToastUtil

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/25
 */
class FloatControlFragment : Fragment() {

    companion object {
        private const val TAG = "FloatControlFragment"
    }

    private val binding by lazy {
        FragmentFloatControlBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<SceneViewModel>({ requireActivity() })

    private val listAdapter = UserListAdapter().apply {
        setOnItemClickListener { _, _, position ->
            data[position].modelObject?.getTransform()?.first?.let {
                viewModel.eventData.postValue(FlyEvent(it))
                if (userListShow) {
                    binding.userRecycler.visibility = View.GONE
                    binding.grayLayer.visibility = View.GONE
                    userListShow = !userListShow
                }
            }
        }
    }

    private var userListShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (userListShow) {
                    binding.userRecycler.visibility = View.GONE
                    binding.grayLayer.visibility = View.GONE
                    userListShow = !userListShow
                } else {
                    activity?.finish()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.rocker.onUpdate = { radians, progress, roll ->
            viewModel.eventData.postValue(RockerEvent(radians, progress, roll))
        }
        binding.modeSwitcher.setOnClickListener {
            val curState = binding.rocker.enable
            viewModel.eventData.postValue(ModeSwitchEvent(!curState))
            if (!curState) {
                binding.modeSwitcher.background =
                    requireActivity().getDrawable(R.drawable.round_btn_bg)
            } else {
                binding.modeSwitcher.background =
                    requireActivity().getDrawable(R.drawable.round_btn_bg_light)
            }
            binding.rocker.enable = !curState
        }
        binding.reset.setOnClickListener {
            viewModel.eventData.postValue(ResetEvent())
        }
        binding.userRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listAdapter
        }
        binding.grayLayer.setOnClickListener { }
        binding.userChoose.setOnClickListener {
            if (!userListShow && listAdapter.data.size == 0) {
                ToastUtil.makeShort("暂无其它用户")
                return@setOnClickListener
            }
            if (userListShow) {
                binding.userRecycler.visibility = View.GONE
                binding.grayLayer.visibility = View.GONE
            } else {
                binding.userRecycler.visibility = View.VISIBLE
                binding.grayLayer.visibility = View.VISIBLE
            }
            userListShow = !userListShow
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memberInfo.observe(viewLifecycleOwner) {
            listAdapter.setList(it)
        }
    }
}

class UserListAdapter : BaseQuickAdapter<MemberInfo, BaseViewHolder>(R.layout.item_user) {

    override fun convert(holder: BaseViewHolder, item: MemberInfo) {
        holder.setText(R.id.user_name, item.userName)
    }

}