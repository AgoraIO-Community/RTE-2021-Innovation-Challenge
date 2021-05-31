package com.dong.circlelive.profile

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import cn.leancloud.AVUser
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.lazyFast
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentProfileBinding
import com.dong.circlelive.emClient
import com.dong.circlelive.live.LivesChannelAdapter
import com.dong.circlelive.live.LivingFragment
import com.dong.circlelive.live.OnAdapterItemClickListener
import com.dong.circlelive.main.MainViewModel
import com.dong.circlelive.model.emUsername
import com.dong.circlelive.posts.PostsAdapter
import com.dong.circlelive.showOtherFragment
import com.hyphenate.easeui.constants.EaseConstant
import com.hyphenate.easeui.modules.chat.EaseChatFragment

/**
 * Create by dooze on 2021/5/24  7:18 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ProfileFragment : BaseFragment(R.layout.fragment_profile), View.OnClickListener, OnAdapterItemClickListener, Toolbar.OnMenuItemClickListener {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val postsAdapter = PostsAdapter()

    private val channelAdapter by lazyFast {  LivesChannelAdapter(viewLifecycleOwner) }

    private val uid by lazyFast { requireNotNull(requireArguments().getString(USER_ID)) }

    private val viewModel by viewModels<ProfileViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ProfileViewModel(uid) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivCover.setImageResource(R.drawable.default_avatar)

        val rvPost = RecyclerView(view.context).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postsAdapter
        }
        val rvChannel = RecyclerView(view.context).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = channelAdapter
        }

        if (uid == AVUser.currentUser().objectId) {
            binding.toolbar.inflateMenu(R.menu.menu_my_profile)
            binding.toolbar.setOnMenuItemClickListener(this)
        }

        binding.viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int = 2

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                if (`object` is RecyclerView) {
                    container.removeView(`object`)
                }
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val pager = when (position) {
                    0 -> rvPost
                    else -> rvChannel
                }
                container.addView(pager, 0)
                return pager
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.posts)
                    else -> getString(R.string.live)
                }
            }
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.tabLayout.apply {
            background = null
        }
        viewModel.avUser.observe(viewLifecycleOwner) { avUser ->
            binding.toolbar.title = avUser.username
            if (avUser.objectId == AVUser.currentUser().objectId) {
                binding.fabProfileAction.isVisible = false
            } else {
                binding.fabProfileAction.isVisible = true
                if (emClient.contactManager().contactsFromLocal.toSet().contains(avUser.emUsername)) {
                    binding.fabProfileAction.tag = true
                    binding.fabProfileAction.setImageResource(R.drawable.ic_chat)
                } else {
                    binding.fabProfileAction.tag = false
                    binding.fabProfileAction.setImageResource(R.drawable.ic_add_friend)
                }
            }
        }

        viewModel.liveChannels.observe(viewLifecycleOwner) { channels ->
            channelAdapter.refresh(channels)
        }
        binding.fabProfileAction.setOnClickListener(this)

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postsAdapter.refresh(posts)
        }

        channelAdapter.itemClickListener = this

        rvPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lm = (rvPost.layoutManager as LinearLayoutManager)
                    val pos = lm.findLastVisibleItemPosition()
                    if (rvPost.adapter!!.itemCount - pos < 3) {
                        viewModel.fetch()
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_profile_action -> {
                if (binding.fabProfileAction.tag as Boolean? == true) {
                    viewModel.avUser.value?.let { user ->
                        showOtherFragment(EaseChatFragment.newInstance(user.emUsername, EaseConstant.CHATTYPE_SINGLE), R.id.top_fragment_container)
                    }
                } else {
                    CommonInputDialog.show(parentFragmentManager, getString(R.string.add_friend_invite)) { reason ->
                        viewModel.sendAddFriendInvitation(reason)
                    }
                }
            }
        }
    }

    override fun onAdapterItemClick(view: View, position: Int) {
        when (view.id) {
            R.id.tv_channel_my_live_status -> {
                val channel = channelAdapter.getItem(position) ?: return
                showOtherFragment(
                    LivingFragment.newInstance(channel.id),
                    R.id.top_fragment_container
                )
            }
            R.id.iv_channel_delete -> {
                val channel = channelAdapter.getItem(position) ?: return
                viewModel.deleteChannel(channel)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_toolbar_logout -> {
                val mainViewModel by activityViewModels<MainViewModel>()
                mainViewModel.logout()
            }
        }
        return true
    }

    companion object {

        private const val USER_ID = "user_id"

        fun newInstance(userId: String): ProfileFragment {

            val fragment = ProfileFragment()
            fragment.arguments = Bundle().apply {
                putString(USER_ID, userId)
            }
            return fragment
        }
    }

}