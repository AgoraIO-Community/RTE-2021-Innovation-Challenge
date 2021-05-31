package com.dong.circlelive.posts

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentCreatePostsBinding
import com.dong.circlelive.hideIme
import com.dong.circlelive.showIme
import com.dong.circlelive.toast

/**
 * Create by dooze on 2021/5/24  1:47 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CreatePostFragment : BaseFragment(R.layout.fragment_create_posts), View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private val viewModel by viewModels<CreatePostViewModel>()

    private val binding by viewBinding(FragmentCreatePostsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.menu_new_post)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.root.setOnClickListener(this)
        binding.toolbar.setOnMenuItemClickListener(this)

        viewModel.publishResult.observe(viewLifecycleOwner) {
            if (it.error.isNullOrEmpty()) {
                parentFragmentManager.popBackStack()
            } else {
                toast(it.error)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        showIme(binding.etPosts)
    }

    override fun onClick(v: View?) {

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_publish_post -> {
                val content = binding.etPosts.text?.toString()?.trim() ?: return true
                if (content.length < 5) {
                    toast(R.string.post_too_short)
                    return true
                }
                viewModel.publish(content)
            }
        }
        return true
    }

    override fun onDestroyView() {
        hideIme()
        super.onDestroyView()
    }

}