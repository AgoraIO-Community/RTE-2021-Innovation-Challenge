package com.dong.circlelive.posts

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentPostsBinding
import com.dong.circlelive.live.OnAdapterItemClickListener
import com.dong.circlelive.profile.ProfileFragment
import com.dong.circlelive.showOtherFragment

/**
 * Create by dooze on 2021/5/24  1:38 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class PostsFragment : BaseFragment(R.layout.fragment_posts), OnAdapterItemClickListener, View.OnClickListener {

    private val binding by viewBinding(FragmentPostsBinding::bind)

    private val viewModel by activityViewModels<PostsViewModel>()

    private val postsAdapter = PostsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = postsAdapter
        postsAdapter.itemClickListener = this

        viewModel.loadMore()
        viewModel.postsLiveData.observe(viewLifecycleOwner) { posts ->
            postsAdapter.refresh(posts)
        }
        binding.srlPostsList.setOnRefreshListener {
            viewModel.load()
        }
        viewModel.stopRefresh.observe(viewLifecycleOwner) {
            if (it) {
                binding.srlPostsList.isRefreshing = false
            }
        }

        binding.rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lm = (binding.rvPosts.layoutManager as LinearLayoutManager)
                    val pos = lm.findLastVisibleItemPosition()
                    if (binding.rvPosts.adapter!!.itemCount - pos < 3) {
                        viewModel.loadMore()
                    }
                }

                binding.fabNewPost.isVisible = newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })

        binding.fabNewPost.setOnClickListener(this)
    }

    override fun onAdapterItemClick(view: View, position: Int) {
        when (view.id) {
            R.id.iv_post_cover -> {
                val user = postsAdapter.getItem(position)?.owner ?: return
                showOtherFragment(ProfileFragment.newInstance(user.objectId), R.id.top_fragment_container)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_new_post -> {
                showOtherFragment(CreatePostFragment(), R.id.top_fragment_container)
            }
        }
    }
}