package com.dong.circlelive.posts

import android.view.ViewGroup
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseAdapter
import com.dong.circlelive.base.BaseViewHolder
import com.dong.circlelive.databinding.ItemPostsBinding
import com.dong.circlelive.utils.dateTime
import com.dong.circlelive.utils.roundCorner

/**
 * Create by dooze on 2021/5/24  2:10 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class PostsAdapter : BaseAdapter<Posts>() {
    override fun getItemViewType(position: Int): Int {
        return R.layout.item_posts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Posts> {
        return PostsHolder(ItemPostsBinding.inflate(layoutInflater, parent, false), this)
    }

    class PostsHolder(val binding: ItemPostsBinding, adapter: PostsAdapter) : BaseViewHolder<Posts>(binding.root, adapter) {

        init {
            binding.ivPostCover.roundCorner(isCircle = true)
        }

        override fun bind(item: Posts) {
            binding.tvPostName.text = item.owner.username
            binding.tvPostDesc.text = item.content
            binding.tvChannelUserInfo.text = item.createdAt.dateTime()
            addClickListener(binding.ivPostCover)
        }
    }
}