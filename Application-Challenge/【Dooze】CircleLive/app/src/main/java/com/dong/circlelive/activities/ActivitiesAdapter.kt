package com.dong.circlelive.activities

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseAdapter
import com.dong.circlelive.base.BaseViewHolder
import com.dong.circlelive.databinding.ItemActivityBinding
import com.dong.circlelive.utils.dpF
import com.dong.circlelive.utils.getDateString
import com.dong.circlelive.utils.roundCorner

/**
 * Create by dooze on 2021/5/25  10:35 上午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ActivitiesAdapter : BaseAdapter<Activity>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Activity> {
        return ActivityHolder(ItemActivityBinding.inflate(layoutInflater, parent, false), this)
    }

    class ActivityHolder(val binding: ItemActivityBinding, adapter: ActivitiesAdapter) : BaseViewHolder<Activity>(binding.root, adapter) {

        init {
            binding.ivActivityCover.roundCorner(isCircle = true)
            binding.tvActivityAction.roundCorner(radius = 4.dpF(), paddingStart = 0, paddingEnd = 0)
        }

        override fun bind(item: Activity) {
            binding.tvTitle.text = item.fromUsername
            binding.tvTitle.isVisible = !item.fromUsername.isNullOrEmpty()

            var content: String? = null
            when (item.type) {
                Activity.Type.INVITATION.value -> {
                    binding.tvActivityAction.isVisible = true
                    content = "好友请求：${item.content}"
                    when (item.status) {
                        Activity.Status.DONE.value -> {
                            binding.tvActivityAction.setBackgroundResource(R.color.textColorSecondary)
                            binding.tvActivityAction.setText(R.string.had_handle)
                        }
                        else -> {
                            binding.tvActivityAction.setBackgroundResource(R.color.colorAccent)
                            binding.tvActivityAction.setText(R.string.accept)
                        }
                    }
                }
                Activity.Type.LIVE_INFO.value -> {
                    content = item.content
                    binding.tvActivityAction.isVisible = false
                }
                else -> {
                    binding.tvActivityAction.isVisible = false
                }
            }

            binding.tvContent.text = content
            binding.tvContent.isVisible = !content.isNullOrEmpty()

            binding.tvTime.text = getDateString(time = item.createdAt)
            addClickListener(binding.ivActivityCover)
            addClickListener(binding.tvActivityAction)
            addLongClickListener(binding.layoutActivity)
        }
    }
}