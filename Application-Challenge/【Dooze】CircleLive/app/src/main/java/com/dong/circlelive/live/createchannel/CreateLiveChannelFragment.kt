package com.dong.circlelive.live.createchannel

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentCreateLiveChannelBinding
import com.dong.circlelive.hideIme
import com.dong.circlelive.live.LivingFragment
import com.dong.circlelive.live.model.LivesViewModel
import com.dong.circlelive.showOtherFragment
import com.dong.circlelive.toast
import com.dong.circlelive.utils.roundCorner

/**
 * Create by dooze on 2021/5/27  6:08 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CreateLiveChannelFragment : BaseFragment(R.layout.fragment_create_live_channel), Toolbar.OnMenuItemClickListener {


    private val binding by viewBinding(FragmentCreateLiveChannelBinding::bind)

    private val viewModel by viewModels<CreateLiveChannelViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivChannelCover.roundCorner(isCircle = true)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.toolbar.inflateMenu(R.menu.menu_create_live_channel)
        binding.toolbar.setOnMenuItemClickListener(this)


        viewModel.publishError.observe(viewLifecycleOwner) { errorInfo ->
            binding.toolbar.menu.findItem(R.id.menu_tool_bar_create_live_channel).isEnabled = true
            toast(errorInfo ?: return@observe)
        }

        viewModel.publishedChannelId.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val vm: LivesViewModel by activityViewModels()
                vm.fetchLiveChannels()
                hideIme()
                parentFragmentManager.popBackStack()
                if (binding.cbAutoLive.isChecked) {
                    showOtherFragment(LivingFragment.newInstance(it), R.id.top_fragment_container)
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_tool_bar_create_live_channel -> {
                item.isEnabled = false
                viewModel.publish(binding.tvChannelName.text.toString(), binding.tvChannelDesc.text.toString())
            }
        }

        return true
    }
}