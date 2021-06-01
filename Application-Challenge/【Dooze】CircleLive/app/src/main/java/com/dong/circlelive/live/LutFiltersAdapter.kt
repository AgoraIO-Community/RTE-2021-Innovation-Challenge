package com.dong.circlelive.live

import android.view.ViewGroup
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseAdapter
import com.dong.circlelive.base.BaseViewHolder
import com.dong.circlelive.databinding.ItemLutImageBinding
import com.dong.circlelive.live.model.LutImage

/**
 * Create by dooze on 2021/5/17  3:04 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LutFiltersAdapter : BaseAdapter<LutImage>() {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_lut_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LutImage> {
        return ViewHolder(ItemLutImageBinding.inflate(layoutInflater, parent, false), this)
    }
}


class ViewHolder(private val binding: ItemLutImageBinding, adapter: LutFiltersAdapter) : BaseViewHolder<LutImage>(binding.root, adapter) {

    init {
        binding.ivLut.setBackgroundResource(R.color.overlay_50_black)
    }

    override fun bind(item: LutImage) {
        binding.ivLut.background = null
        binding.ivLut.setImageBitmap(item.bitmap)
        addClickListener(binding.ivLut)
    }
}