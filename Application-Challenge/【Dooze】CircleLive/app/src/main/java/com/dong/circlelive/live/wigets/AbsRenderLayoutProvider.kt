package com.dong.circlelive.live.wigets

import android.view.View
import com.dong.circlelive.utils.dp
import com.dong.circlelive.utils.dpF

abstract class AbsRenderLayoutProvider(private val renderContainer: LiveRenderContainer) : RenderLayoutProvider {
    protected val contentWidth: Int
        get() {
            val layout = renderContainer.videoRenderLayout
            return contentLayout().width - (layout.paddingStart + renderContainer.paddingStart) - (layout.paddingEnd + renderContainer.paddingStart)
        }
    protected val contentHeight: Int
        get() {
            return renderContainer.renderDisplayContentHeight()
        }

    protected open fun contentLayout(): View = renderContainer.wrapperLayout ?: renderContainer.videoRenderLayout

    protected val fullPrimaryMode: Boolean get() = renderContainer.fullPrimaryMode

    protected val renderCount: Int get() = renderContainer.videoRenderCount()

    protected val cornerRadius = 10.dpF(renderContainer.context)

    protected val gap = 1.dp(renderContainer.context)

    override fun totalRenderHeight(): Int {
        return renderContainer.videoRenderLayout.height
    }

    override fun renderDisplayHeight(): Int {
        return renderContainer.wrapperLayout?.height ?: renderContainer.height
    }

    override fun applyCornerForRenderContainerIfNeed() {
        renderContainer.videoRenderLayout.clipToOutline = false
    }
}