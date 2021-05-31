package com.dong.circlelive.live

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseAdapter
import com.dong.circlelive.base.BaseViewHolder
import com.dong.circlelive.databinding.ItemLivesChannelBinding
import com.dong.circlelive.getColor
import com.dong.circlelive.live.model.LiveChannel
import com.dong.circlelive.live.model.subscriberChannelMessages
import com.dong.circlelive.live.model.subscriberChannelMessagesLD
import com.dong.circlelive.utils.dpF
import com.dong.circlelive.utils.roundCorner

/**
 * Create by dooze on 2021/5/18  8:09 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LivesChannelAdapter(val lifecycleOwner: LifecycleOwner) : BaseAdapter<LiveChannel>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_lives_channel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LiveChannel> {
        return ChannelViewHolder(ItemLivesChannelBinding.inflate(layoutInflater, parent, false), this)
    }

    override fun onViewRecycled(holder: BaseViewHolder<LiveChannel>) {
        super.onViewRecycled(holder)
        (holder as ChannelViewHolder).channelId = null
    }
}


class ChannelViewHolder(private val binding: ItemLivesChannelBinding, val adapter: LivesChannelAdapter) :
    BaseViewHolder<LiveChannel>(binding.root, adapter) {

    init {
        binding.ivChannelCover.roundCorner(isCircle = true)
        binding.tvChannelMyLiveStatus.roundCorner(radius = 4.dpF(), paddingBottom = 0, paddingStart = 0, paddingEnd = 0, paddingTop = 0)
    }

    var channelId: String? = null
        set(value) {
            field = value
            if (value == null) {
                observer?.let { subscriberChannelMessagesLD.removeObserver(it) }
                observer = null
            }
        }


    private var observer: Observer<String>? = null

    override fun bind(item: LiveChannel) {
        channelId = item.id
        if (item.isSubscriber) {
            if (observer == null) {
                observer = Observer<String> { cId ->
                    if (channelId == cId) {
                        val liveMessage = subscriberChannelMessages[cId] ?: return@Observer
                        binding.tvChannelNotify.isVisible = true
                        binding.tvChannelNotify.text = liveMessage.content
                    }
                }
            }
            subscriberChannelMessagesLD.observe(adapter.lifecycleOwner, observer!!)
        } else {
            observer?.let { subscriberChannelMessagesLD.removeObserver(it) }
            observer = null
        }
        item.run {
            binding.tvChannelName.text = name
            binding.tvChannelDesc.text = desc
        }

        binding.ivChannelDelete.isVisible = item.isSelf

        if (item.livingTimes <= 0) {
            binding.tvChannelUserInfo.isInvisible = true
        } else {
            binding.tvChannelUserInfo.isVisible = true
            binding.tvChannelUserInfo.text = "${item.livingTimes}次"
        }

        if (binding.ivChannelDelete.isVisible) {
            addClickListener(binding.ivChannelDelete)
        }
        if (item.isSubscriber) {
            binding.ivChannelSubscriber.imageTintList = ColorStateList.valueOf(getColor(R.color.ui_red_tertiary))
        } else {
            binding.ivChannelSubscriber.imageTintList = ColorStateList.valueOf(getColor(R.color.textColorSecondary))
        }
        addClickListener(binding.ivChannelSubscriber)
        addClickListener(binding.ivChannelCover)
        addClickListener(binding.tvChannelMyLiveStatus)
    }

}
