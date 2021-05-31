package com.dong.circlelive.live.wigets

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.*
import com.dong.circlelive.Live
import com.dong.circlelive.R
import com.dong.circlelive.utils.*

/**
 * Create by dooze on 2020/9/3  3:31 PM
 * Email: stonelavender@hotmail.com
 */
@Suppress("DEPRECATION")
class LiveRenderContainer constructor(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    var clickListenerProvider: ClickListenerProvider? = null

    internal var wrapperLayout: View? = null
    internal val videoRenderLayout = FrameLayout(context).apply {
        id = R.id.lp_room_video_render_flex_layout
    }

    private val defaultLayoutProvider = NormalRenderLayoutProvider(this)

    var layoutProvider: RenderLayoutProvider = defaultLayoutProvider
        private set

    var fullPrimaryMode = false
        private set

    private var useTopRenderRemainderSpace = true

    var primaryRenderUserId: String? = null
        private set

    init {
        orientation = VERTICAL
        addView(videoRenderLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    fun setupWrapperLayout(wrapperLayout: FrameLayout) {
        this.wrapperLayout = wrapperLayout
        notifyChanged(true)
        wrapperLayout.doOnPreDraw {
            minimumHeight = it.height
            videoRenderLayout.minimumHeight = it.height - (topAdditionalView()?.height ?: 0)
        }
        wrapperLayout.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
            if (oldBottom != bottom || oldTop != top) {
                minimumHeight = bottom - top
                videoRenderLayout.minimumHeight = minimumHeight - (topAdditionalView()?.height ?: 0)
                notifyChanged(true)
            }
        }
    }

    fun renderDisplayContentHeight(): Int {
        val height = renderWrapperHeight()
        val layout = videoRenderLayout
        return height - layout.paddingBottom - layout.paddingTop
    }

    fun renderWrapperHeight(): Int = wrapperLayout?.height ?: videoRenderLayout.height

    /**
     * 只能添加一个top view
     * @param useRenderRemainderSpace 为true则优先布局完所有render，剩余的空间才是[view]的实际高度。否则优先[view]，剩下的空间再挤压布局所有render
     * @param layoutParams [useRenderRemainderSpace]为false时应该指定对应的布局参数，否则默认包裹内容，需要注意会挤压剩下的render空间
     */
    fun addTopAdditionalView(view: View, useRenderRemainderSpace: Boolean, layoutParams: LayoutParams? = null) {
        this.useTopRenderRemainderSpace = useRenderRemainderSpace
        removeTopAdditionalView(false)
        val params = layoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        notifyChanged(true)
        if (useRenderRemainderSpace) {
            post {
                params.height = height - layoutProvider.totalRenderHeight()
                addView(view, 0, params)
            }
        } else {
            addView(view, 0, params)
        }
    }

    /**
     * 移除index = 0的非FlexBoxLayout的view
     */
    private fun removeTopAdditionalView(notifyLayout: Boolean = true) {
        val view = getChildAt(0)
        if (view != null && view.id != R.id.lp_room_video_render_flex_layout) {
            removeViewAt(0)
        }
        if (notifyLayout) {
            notifyChanged(true)
        }
    }

    private fun topAdditionalView(): View? {
        val view = getChildAt(0)
        if (view?.id != R.id.lp_room_video_render_flex_layout) {
            return view
        }
        return null
    }

    fun notifyChangeTopView(postUpdate: Boolean) {
        val view = topAdditionalView()
        if (useTopRenderRemainderSpace && view != null) {
            if (postUpdate) {
                post {
                    view.updateViewHeight(height() - layoutProvider.totalRenderHeight())
                }
            } else {
                view.updateViewHeight(height() - layoutProvider.totalRenderHeight())
            }
        }
    }

    private fun height(): Int = wrapperLayout?.height ?: height

    fun addRender(liveRenderView: LiveRenderView) {
        if (videoRenderCount() >= Live.MAX_LIVING_USER) {
            return
        }
        liveRenderView.userPublicId?.let { removeUserRender(it) }
        liveRenderView.setOnClickListener(clickListenerProvider)
        liveRenderView.setOnLongClickListener(clickListenerProvider)
        notifyLayout(videoRenderCount() + 1)
        var index = liveRenderView.layoutIndex
        index = if (index < 0) {
            videoRenderCount()
        } else {
            index.coerceAtMost(videoRenderCount())
        }
        liveRenderView.layoutIndex = index
        videoRenderLayout.addView(liveRenderView, index, insertParams(index, liveRenderView))
        notifyChangeTopView(true)

    }

    fun removeAllRender() {
        videoRenderLayout.removeAllViews()
    }

    private fun removeRender(liveRenderView: LiveRenderView) {
        if (liveRenderView.parent != videoRenderLayout) {
            liveRenderView.removeFromParent()
        } else {
            videoRenderLayout.removeView(liveRenderView)
        }
        liveRenderView.setOnClickListener(null)
        liveRenderView.removeRender()
        liveRenderView.setOnLongClickListener(null)
        notifyLayout(videoRenderCount())
        notifyChangeTopView(true)
        liveRenderView.removeAvatarCover()
    }

    private fun renderView(index: Int): LiveRenderView? = videoRenderLayout.getChildAt(index) as? LiveRenderView

    fun renderView(userPublicId: String): LiveRenderView? {
        return videoRenderLayout.find {
            (it as? LiveRenderView)?.userPublicId == userPublicId
        } as? LiveRenderView
    }

    fun removeRender(userPublicId: String) {
        val renderView = videoRenderLayout.find {
            (it as? LiveRenderView)?.userPublicId == userPublicId
        } ?: return
        removeRender(renderView as LiveRenderView)
    }

    fun videoRenderCount(): Int = videoRenderLayout.childCount

    fun renderCount(): Int = videoRenderCount()

    fun renderUserIds(): List<String> {
        val ids = mutableListOf<String>()
        videoRenderLayout.forEach { renderView ->
            if (renderView is LiveRenderView) {
                ids.add(renderView.userPublicId ?: return@forEach)
            }
        }
        return ids
    }

    fun notifyChanged(post: Boolean = false, isGuessWordGame: Boolean = false) {
        if (post) {
            post {
                notifyLayout(videoRenderCount(), isGuessWordGame)
                requestLayout()
                videoRenderLayout.requestLayout()
            }
        } else {
            notifyLayout(videoRenderCount(), isGuessWordGame)
            requestLayout()
            videoRenderLayout.requestLayout()
        }
    }

    private fun changeRenderLayoutProvider(renderLayoutProvider: RenderLayoutProvider) {
        val change = layoutProvider != renderLayoutProvider
        this.layoutProvider = renderLayoutProvider
        if (change) {
            layoutProvider.applyCornerForRenderContainerIfNeed()
            notifyChanged(true)
        }
    }

    fun changeToNormalLayout(postUpdate: Boolean = false) {
        changeRenderLayoutProvider(defaultLayoutProvider)
        primaryRenderUserId = null
        notifyChanged(postUpdate)
    }

    /**
     * 将制定位置的render cell 撑满父布局的全部可用空间
     */
    private fun changeToFullPrimaryRender(index: Int) {
        if (videoRenderCount() <= 0 || index < 0) return
        val targetView = renderView(index) ?: return
        val primaryView = renderView(0) ?: return
        fullPrimaryMode = true
        if (index != 0) {
            primaryRenderUserId = targetView.userPublicId
            videoRenderLayout.removeViewAt(0)
            videoRenderLayout.removeView(targetView)
            videoRenderLayout.addView(targetView, 0)
            videoRenderLayout.addView(primaryView, index)
        }
        notifyChanged()
    }

    fun resetToNormalMode(postUpdate: Boolean = false) {
        fullPrimaryMode = false
        primaryRenderUserId = null
        notifyChanged(postUpdate)
    }

    /**
     * 将指定位置的render cell 切换为主render（最大的一个render cell）
     */
    private fun changeToPrimaryRender(index: Int) {
        if (videoRenderCount() <= 0 || index <= 0) return
        val targetView = renderView(index) ?: return
        val primaryView = renderView(0) ?: return
        primaryRenderUserId = targetView.userPublicId
        swapParams(targetView, primaryView)

        videoRenderLayout.removeViewAt(0)
        videoRenderLayout.removeView(targetView)
        videoRenderLayout.addView(targetView, 0)
        videoRenderLayout.addView(primaryView, index)

        notifyLayout(videoRenderCount())
        requestLayout()
    }

    private fun indexByUid(uid: Int): Int? {
        videoRenderLayout.forEachIndexed { index, view ->
            if (view is LiveRenderView && view.liveUid == uid) {
                return index
            }
        }
        return null
    }

    fun changeToPrimaryRenderByUserPublicId(publicId: String) {
        indexByUerPublicId(publicId)?.let { index ->
            changeToPrimaryRender(index)
        }
    }

    private fun indexByUerPublicId(publicId: String): Int? {
        videoRenderLayout.forEachIndexed { index, view ->
            if (view is LiveRenderView && view.userPublicId == publicId) {
                return index
            }
        }
        return null
    }

    private fun swapParams(targetView: LiveRenderView, primaryView: LiveRenderView) {
        val targetLp = targetView.layoutParams as MarginLayoutParams
        val primaryLp = primaryView.layoutParams as MarginLayoutParams

        val targetWidth = targetLp.width
        val targetHeight = targetLp.height
        val targetMarginTop = targetLp.topMargin
        val targetMarginBottom = targetLp.bottomMargin
        val targetMarginLeft = targetLp.leftMargin

        val primaryWidth = primaryLp.width
        val primaryHeight = primaryLp.height
        val primaryMarginTop = primaryLp.topMargin
        val primaryMarginBottom = primaryLp.bottomMargin
        val primaryMarginLeft = primaryLp.leftMargin

        primaryLp.width = targetWidth
        primaryLp.height = targetHeight
        primaryLp.topMargin = targetMarginTop
        primaryLp.bottomMargin = targetMarginBottom
        primaryLp.leftMargin = targetMarginLeft

        targetLp.width = primaryWidth
        targetLp.height = primaryHeight
        targetLp.topMargin = primaryMarginTop
        targetLp.bottomMargin = primaryMarginBottom
        targetLp.leftMargin = primaryMarginLeft
    }

    private fun notifyLayout(renderCount: Int, isGuessWordGame: Boolean = false): Boolean {
        var changed = false
        videoRenderLayout.updateLayoutParams<LayoutParams> {
            height = layoutProvider.renderDisplayHeight()
        }
        for (i in 0 until renderCount) {
            val renderView = renderView(i) ?: continue
            if (renderView.isExpandFocused) continue
            val params = layoutProvider.renderWidthHeight(i, renderCount)
            val (w, h, tx, ty) = params
            val lp = renderView.layoutParams as? MarginLayoutParams ?: continue
            changed = changed || (lp.width != w || lp.height != height) || (renderView.translationX != tx || renderView.translationY != ty)
            lp.width = w
            lp.height = h
            renderView.translationX = tx
            renderView.translationY = ty
            params.applyRadius(renderView)
        }

        return changed
    }


    private fun insertParams(index: Int, renderView: View): MarginLayoutParams {
        val renderCount = videoRenderCount() + 1
        val params = layoutProvider.renderWidthHeight(index, renderCount)
        val (w, h, tx, ty) = params
        renderView.translationX = tx
        renderView.translationY = ty
        params.applyRadius(renderView)
        return MarginLayoutParams(w, h)
    }

    fun addUserVideoSurface(
        uid: Int,
        userId: String,
        textureView: TextureView,
        username: String?,
        needAnimation: Boolean
    ) {
        var needAddRender = true
        var renderView = videoRenderLayout.find {
            (it as? LiveRenderView)?.userPublicId == userId
        } as? LiveRenderView

        if (renderView == null) {
            renderView = LiveRenderView(context)
        } else {
            needAddRender = false
            renderView.removeRender()
        }
        renderView.showRenderUsername(username)
        renderView.onclickListener = clickListenerProvider
        renderView.userPublicId = userId
        renderView.liveUid = uid
        renderView.addRender(textureView)
        if (needAddRender) {
            addRender(renderView)
        }
    }

    fun addUserRenderPlaceHolder(
        userPublicId: String,
        username: String?,
    ) {
        val existRender = videoRenderLayout.find {
            (it as? LiveRenderView)?.userPublicId == userPublicId
        }
        if (existRender != null) {
            (existRender as LiveRenderView).showRenderUsername(username)
            return
        }
        val renderView = LiveRenderView(context)
        renderView.onclickListener = clickListenerProvider
        renderView.userPublicId = userPublicId
        renderView.showRenderUsername(username)
        addRender(renderView)
    }

    fun renderIsDisplay(userId: String): Boolean {
        val renderView = renderView(userId) ?: return false
        return renderView.width > 0 && renderView.height > 0
    }

    fun checkLayoutRenderIndex(userPublicId: String, index: Int) {
        renderView(userPublicId)?.let { renderView ->
            if (renderView.layoutIndex != index) {
                renderView.removeFromParent()
                renderView.layoutIndex = index
                addRender(renderView)
                videoRenderLayout.forEachIndexed { index, view ->
                    (view as LiveRenderView).layoutIndex = index
                }
            }
        }
    }

    fun removeUserRender(userPublicId: String) {
        val renderView = videoRenderLayout.find {
            (it as? LiveRenderView)?.userPublicId == userPublicId
        } ?: return
        removeRender(renderView as LiveRenderView)
    }


    fun destroy() {
        videoRenderLayout.forEach {
            if (it is LiveRenderView) {
                it.removeRender()
            }
            it.setOnClickListener(null)
            it.setOnLongClickListener(null)
        }
    }

}
