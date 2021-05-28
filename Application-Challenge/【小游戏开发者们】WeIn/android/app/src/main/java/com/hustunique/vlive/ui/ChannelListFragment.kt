package com.hustunique.vlive.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hustunique.vlive.R
import com.hustunique.vlive.databinding.FragmentChannelListBinding
import com.hustunique.vlive.remote.Channel
import com.hustunique.vlive.remote.Service
import com.hustunique.vlive.util.ToastUtil
import com.hustunique.vlive.util.UserInfoManager
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/23
 */
class ChannelListFragment : Fragment() {
    companion object {
        private const val TAG = "ChannelListFragment"
    }

    private val binding by lazy {
        FragmentChannelListBinding.inflate(layoutInflater)
    }

    private val listAdapter = ChannelListAdapter().apply {
        setOnItemClickListener { _, _, position ->
            findNavController().navigate(
                ChannelListFragmentDirections.actionChannelListFragmentToActorChooseFragment(
                    data[position].id,
                    true
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.actor_img_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.channelListRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listAdapter
            itemAnimator = SlideInRightAnimator()
        }
        binding.root.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                initData()
                binding.root.removeTransitionListener(this)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })
        binding.logout.setOnClickListener {
            UserInfoManager.saveUid("")
            findNavController().navigate(ChannelListFragmentDirections.actionChannelListFragmentToWelcomeFragment())
        }
        binding.createBtn.setOnClickListener {
            findNavController().navigate(ChannelListFragmentDirections.actionChannelListFragmentToCreateRoomFragment())
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (listAdapter.data.isNullOrEmpty()) {
            binding.root.transitionToState(R.id.end)
        } else {
            initData()
        }
    }

    private fun initData() {
        lifecycleScope.launchWhenCreated {
            Service.channelList().let {
                if (it.successful) {
                    it.data
                } else {
                    ToastUtil.makeShort(it.msg ?: "")
                    null
                }
            }?.let {
                if (it.isNotEmpty()) {
                    binding.root.transitionToState(R.id.none_actor)
                }
                listAdapter.setList(listOf())
                it.forEach { channel ->
                    listAdapter.addData(channel)
                }
            }
        }
    }
}

class ChannelListAdapter : BaseQuickAdapter<Channel, BaseViewHolder>(R.layout.item_channel) {

    override fun convert(holder: BaseViewHolder, item: Channel) {
        holder.setText(R.id.channel_name, "${item.id}的派对")
            .setText(R.id.channel_desc, item.desc)
    }

}