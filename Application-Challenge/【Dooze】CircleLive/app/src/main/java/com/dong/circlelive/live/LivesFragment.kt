package com.dong.circlelive.live

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.CommonTipsDialog
import com.dong.circlelive.base.lazyFast
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentLivesBinding
import com.dong.circlelive.live.model.LivesViewModel
import com.dong.circlelive.live.model.subscriberUpdate
import com.dong.circlelive.live.wigets.ClickListenerProvider
import com.dong.circlelive.profile.ProfileFragment
import com.dong.circlelive.showOtherFragment
import com.dong.circlelive.toast
import kotlinx.coroutines.launch

/**
 * Create by dooze on 2021/5/14  11:23 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LivesFragment : BaseFragment(R.layout.fragment_lives), View.OnClickListener, ClickListenerProvider, OnAdapterItemClickListener {


    private val binding by viewBinding(FragmentLivesBinding::bind)

    private val viewModel by activityViewModels<LivesViewModel>()

    private val channelsAdapter by lazyFast { LivesChannelAdapter(viewLifecycleOwner) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.srlLiveChannels.setOnRefreshListener {
            viewModel.fetchLiveChannels()
        }

        binding.rvLiveChannels.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLiveChannels.adapter = channelsAdapter
        channelsAdapter.itemClickListener = this

        viewModel.liveChannels.observe(viewLifecycleOwner) { channels ->
            channelsAdapter.refresh(channels)
        }

        viewModel.stopRefresh.observe(viewLifecycleOwner) {
            if (it) {
                binding.srlLiveChannels.isRefreshing = false
            }
        }

        subscriberUpdate.observe(viewLifecycleOwner) {
            if (it) {
                channelsAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onClick(v: View?) {
    }

    override fun onLongClick(v: View?): Boolean {
        return true
    }

    override fun onAdapterItemClick(view: View, position: Int) {
        when (view.id) {
            R.id.tv_channel_my_live_status -> {
                val channel = channelsAdapter.getItem(position) ?: return
                showOtherFragment(
                    LivingFragment.newInstance(channel.id),
                    R.id.top_fragment_container
                )
            }
            R.id.iv_channel_cover -> {
                val channel = channelsAdapter.getItem(position) ?: return
                showOtherFragment(
                    ProfileFragment.newInstance(channel.creator.objectId),
                    R.id.top_fragment_container
                )
            }
            R.id.iv_channel_delete -> {
                val channel = channelsAdapter.getItem(position) ?: return
                viewModel.deleteChannel(channel)
            }
            R.id.iv_channel_subscriber -> {
                val channel = channelsAdapter.getItem(position) ?: return
                if (channel.isSubscriber) {
                    mainScope.launch {
                        channel.unSubscriber {
                            channelsAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    mainScope.launch {
                        if (!channel.subscriber()) {
                            CommonTipsDialog.show(
                                parentFragmentManager,
                                getString(R.string.live_channel_subscriber_limit_title),
                                getString(R.string.live_channel_subscriber_limit_subtitle)
                            )
                        } else {
                            channelsAdapter.notifyDataSetChanged()
                            toast(R.string.live_channel_subscriber_success)
                        }
                    }
                }
            }
        }
    }

}

