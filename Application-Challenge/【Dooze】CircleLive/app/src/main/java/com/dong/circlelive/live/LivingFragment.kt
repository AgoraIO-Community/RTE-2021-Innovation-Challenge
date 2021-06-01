package com.dong.circlelive.live

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.AVUser
import com.dong.circlelive.R
import com.dong.circlelive.appContext
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.CommonTipsDialog
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentLivingBinding
import com.dong.circlelive.live
import com.dong.circlelive.live.model.LivingViewModel
import com.dong.circlelive.live.wigets.ClickListenerProvider
import com.dong.circlelive.live.wigets.LiveRenderView
import com.dong.circlelive.store.KEY_LIVE_FILTER_INTENSITY
import com.dong.circlelive.store.KEY_LIVE_FILTER_PATH
import com.dong.circlelive.store.store
import com.dong.circlelive.toast
import com.permissionx.guolindev.PermissionX

/**
 * Create by dooze on 2021/5/25  10:56 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LivingFragment : BaseFragment(R.layout.fragment_living), ClickListenerProvider, OnAdapterItemClickListener, Toolbar.OnMenuItemClickListener {

    private val binding by viewBinding(FragmentLivingBinding::bind)

    private val viewModel by activityViewModels<LivingViewModel> {

        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LivingViewModel(requireNotNull(requireArguments().getString(ARG_CHANNEL_ID))) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        binding.liveRenderContainer.clickListenerProvider = this
        binding.toolbar.inflateMenu(R.menu.menu_living)
        binding.toolbar.setOnMenuItemClickListener(this)
        viewModel.renderValue.observe(viewLifecycleOwner) { render ->
            render ?: return@observe
            binding.liveRenderContainer.addRender(LiveRenderView(appContext).apply {
                userPublicId = render.userId
                onclickListener = this@LivingFragment
                if (render.userId == AVUser.currentUser().objectId) {
                    setupSwitchCameraIcon()
                    val p = store.decodeFloat(KEY_LIVE_FILTER_INTENSITY, 1f)
                    setupIntensityBar(p, object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            live.cameraSource?.changeIntensity(progress / 100f)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {
                            seekBar.alpha = 1f
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            seekBar.alpha = 0.3f
                            store.encode(KEY_LIVE_FILTER_INTENSITY, seekBar.progress / 100f)
                        }
                    })
                }
                addRender(render.textureView ?: return@observe)
            })
        }

        viewModel.removeRender.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                val (uid, userId) = pair
                binding.liveRenderContainer.removeUserRender(userId)
            }
        }

        binding.rvFilters.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilters.adapter = LutFiltersAdapter().apply {
            itemClickListener = this@LivingFragment
        }
        viewModel.lutBitmaps.observe(viewLifecycleOwner) { lutBitmaps ->
            (binding.rvFilters.adapter as? LutFiltersAdapter)?.refresh(lutBitmaps ?: emptyList())
        }

        viewModel.liveChannel.observe(viewLifecycleOwner) { channel ->
            binding.toolbar.title = channel?.name
        }

        viewModel.livingCount.observe(viewLifecycleOwner) { count ->
            if (count > 1) {
                binding.tvLiveFullMask.isVisible = false
                (binding.tvLiveFullMask.getTag(R.id.tag_living_mask_anim) as? Animator)?.cancel()
            } else {
                binding.tvLiveFullMask.isVisible = true
                binding.tvLiveFullMask.setText(R.string.living_waiting)
                val anim = (binding.tvLiveFullMask.getTag(R.id.tag_living_mask_anim) as? Animator)
                if (anim == null) {
                    ObjectAnimator.ofPropertyValuesHolder(
                        binding.tvLiveFullMask,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f)
                    ).apply {
                        duration = 1000L
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        binding.tvLiveFullMask.setTag(R.id.tag_living_mask_anim, this)
                    }.start()
                } else {
                    anim.start()
                }
            }
        }

        viewModel.showFilterTips.observe(viewLifecycleOwner) {
            if (it) {
                CommonTipsDialog.show(
                    parentFragmentManager,
                    getString(R.string.living_filter_tips_title),
                    getString(R.string.living_filter_tips_subtitle),
                    getString(R.string.try_this)
                ) {
                    showFilterPicker()
                }
            }
        }

        binding.tvFiltersClose.setOnClickListener(this)

        PermissionX.init(this)
            .permissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).request { allGranted, _, _ ->
                if (allGranted) {
                    viewModel.start(viewLifecycleOwner)
                } else {
                    toast(R.string.live_permissin_denied)
                    parentFragmentManager.popBackStack()
                }
            }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_live_render_switch_camera -> {
                live.cameraSource?.switchCamera()
            }
            R.id.tv_filters_close -> {
                (binding.rvFilters.adapter as LutFiltersAdapter).refresh(emptyList())
                binding.flFiltersRoot.isVisible = false
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.layout_user_render_root -> {
                showFilterPicker()
            }
        }
        return true
    }


    private fun showFilterPicker() {
        binding.liveRenderContainer.renderView(AVUser.currentUser().objectId)?.let {
            if (binding.flFiltersRoot.isVisible) {
                binding.flFiltersRoot.isVisible = false
            } else {
                viewModel.transformFilters(BitmapFactory.decodeResource(resources, R.drawable.default_avatar))
                binding.flFiltersRoot.isVisible = true
            }
        }
    }

    override fun onAdapterItemClick(view: View, position: Int) {
        when (view.id) {
            R.id.iv_lut -> {
                (binding.rvFilters.adapter as LutFiltersAdapter).getItem(position)?.let {
                    live.cameraSource?.changeFilter(it.assetPath)
                    store.encode(KEY_LIVE_FILTER_PATH, it.assetPath)
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_living_filter -> {
                showFilterPicker()
            }
        }

        return true
    }

    override fun onStop() {
        super.onStop()
        (binding.tvLiveFullMask.getTag(R.id.tag_living_mask_anim) as? Animator)?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }

    companion object {

        private const val ARG_CHANNEL_ID = "channel_id"

        fun newInstance(channelId: String): LivingFragment {
            val fragment = LivingFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_CHANNEL_ID, channelId)
            }
            return fragment
        }
    }

}