package com.dong.circlelive.live.wigets

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.TextureView
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.dong.circlelive.R
import com.dong.circlelive.getColor
import com.dong.circlelive.utils.dp
import com.dong.circlelive.utils.dpF
import com.dong.circlelive.utils.removeFromParent

/**
 * Create by dooze on 2020/9/3  3:38 PM
 * Email: stonelavender@hotmail.com
 */
class LiveRenderView constructor(context: Context) : FrameLayout(context) {

    var onclickListener: ClickListenerProvider? = null
        set(value) {
            field = value
            renderRoot.setOnClickListener(value)
            renderRoot.setOnLongClickListener(value)
        }

    var userPublicId: String? = null
    var liveUid = -1

    var layoutIndex = -1

    var isExpandFocused = false

    private val layoutInflater: LayoutInflater

    private val renderRoot: FrameLayout = FrameLayout(context)
    private val coverContentRoot: FrameLayout = FrameLayout(context)
    private var coverHintTv: TextView? = null

    val tvRenderUserName = TextView(context)


    private var intensityBar: SeekBar? = null

    init {
        addView(renderRoot, 0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(coverContentRoot, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        setBackgroundColor(getColor(R.color.color_white_10_alpha))
        layoutInflater = LayoutInflater.from(context)

        tvRenderUserName.apply {
            textSize = 10f
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(getColor(R.color.ui_white))
            setShadowLayer(4.dpF(context), 0f, 0f, getColor(R.color.overlay_30_black))
        }

        addView(tvRenderUserName, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = 8.dp(context)
            topMargin = marginStart
            marginEnd = marginStart
        })
        renderRoot.id = R.id.layout_user_render_root
    }

    fun setupSwitchCameraIcon() {
        val icon = AppCompatImageView(context)
        icon.id = R.id.iv_live_render_switch_camera
        icon.setImageResource(R.drawable.ic_switch_camera)

        addView(icon, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = 10.dp(context)
            topMargin = marginStart
            marginEnd = marginStart
        })

        icon.setOnClickListener(onclickListener)
    }

    fun setupIntensityBar(progress: Float, listener: SeekBar.OnSeekBarChangeListener) {
        var bar = intensityBar
        if (bar == null) {
            bar = SeekBar(context)
            bar.alpha = 0.3f
            bar.max = 100
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bar.min = 0
            }
            context.getDrawable(R.drawable.seekbar_thumb_with_shadow)?.let {
                bar.thumb = it
            }
            context.getDrawable(R.drawable.filter_adjust_seekbar_style)?.let {
                bar.progressDrawable = it
            }
            bar.progress = (100 * progress).toInt()
            bar.id = R.id.sb_filter_intensity
            bar.setOnSeekBarChangeListener(listener)
        }
        addView(bar, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM
            marginStart = 46.dp()
            marginEnd = marginStart
            bottomMargin = 30.dp()
        })
        this.intensityBar = bar
    }


    fun addRender(textureView: TextureView) {
        textureView.removeFromParent()
        renderRoot.addView(textureView, 0)
    }

    fun removeRender() {
        renderRoot.removeAllViews()
    }

    fun showRenderUsername(text: String?) {
        tvRenderUserName.text = text
    }

    fun showAroundText(show: Boolean) {
        coverHintTv?.isVisible = show
    }

    fun removeAvatarCover() {
        coverContentRoot.isVisible = false
    }

}