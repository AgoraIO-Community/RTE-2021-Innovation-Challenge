package com.dong.circlelive.live.wigets

/**
 * Create by dooze on 2021/1/20  4:36 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class NormalRenderLayoutProvider(renderContainer: LiveRenderContainer) : AbsRenderLayoutProvider(renderContainer) {

    override fun renderWidthHeight(index: Int, renderCount: Int): RenderLayoutParams {
        return when (renderCount) {
            0 -> RenderLayoutParams(0, 0)
            1 -> RenderLayoutParams(contentWidth, contentHeight)
            2 -> {
                when (index) {
                    0 -> {
                        RenderLayoutParams(contentWidth, (contentHeight - gap) / 2)
                    }
                    1 -> {
                        RenderLayoutParams(contentWidth, (contentHeight - gap) / 2, 0f, contentHeight / 2f + gap / 2f)
                    }
                    else -> throw RuntimeException("invalid renderWidthHeight renderCount = $renderCount index = $index")
                }
            }
            in 3..9 -> {
                if (index == 0) {
                    RenderLayoutParams(contentWidth, contentWidth * 5 / 4)
                } else {
                    val height = contentHeight - contentWidth * 5 / 4
                    when (renderCount) {
                        3, 4 -> {
                            val oneWidth = (contentWidth - (gap * (renderCount - 2))) / (renderCount - 1)
                            val oneHeight = height - gap
                            val tY = (contentWidth * 5 / 4f + gap)
                            when (index) {
                                1 -> {
                                    RenderLayoutParams(oneWidth, oneHeight, 0f, tY)
                                }
                                2 -> {
                                    RenderLayoutParams(oneWidth, oneHeight, (oneWidth + gap).toFloat(), tY)
                                }
                                3 -> {
                                    RenderLayoutParams(oneWidth, oneHeight, (oneWidth + gap) * 2f, tY)
                                }
                                else -> throw RuntimeException("invalid renderWidthHeight renderCount = $renderCount index = $index")
                            }
                        }
                        5, 6 -> {
                            val tY = (contentWidth * 5 / 4f + gap)
                            val tX = (contentWidth + gap) / 2f
                            val sWidth = (contentWidth / 2 - gap - gap / 2) / 2
                            val sHeight = (height - gap - gap) / 2
                            when (index) {
                                1 -> {
                                    RenderLayoutParams((contentWidth - gap) / 2, height - gap, 0f, tY)
                                }
                                2 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX, tY)
                                }
                                3 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX + sWidth + gap, tY)
                                }
                                4 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX, tY + sHeight + gap)
                                }
                                5 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX + sWidth + gap, tY + sHeight + gap)
                                }
                                else -> throw RuntimeException("invalid renderWidthHeight renderCount = $renderCount index = $index")
                            }
                        }
                        else -> {
                            val sWidth = (contentWidth - gap * 3) / 4
                            val sHeight = (height - gap * 2) / 2
                            val tY = (contentWidth * 5 / 4f + gap)
                            val tX = 0f
                            when (index) {
                                in 1..4 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX + (sWidth + gap) * (index - 1), tY)
                                }
                                in 5..9 -> {
                                    RenderLayoutParams(sWidth, sHeight, tX + (sWidth + gap) * (index - 5), tY + sHeight + gap)
                                }
                                else -> throw RuntimeException("invalid renderWidthHeight renderCount = $renderCount index = $index")
                            }
                        }
                    }
                }
            }
            else -> throw RuntimeException("invalid renderWidthHeight renderCount = $renderCount index = $index")
        }
    }

    override fun avatarCoverScale(index: Int, renderCount: Int): Float {
        return when {
            renderCount <= 2 -> 1f
            fullPrimaryMode -> 1f
            index == 0 -> 1f
            else -> 1.2f
        }
    }

    override fun muteIconScale(index: Int, renderCount: Int): Float {
        return when {
            renderCount <= 2 -> 1f
            fullPrimaryMode -> 1f
            index == 0 -> 1f
            else -> 0.85f
        }
    }
}