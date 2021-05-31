package com.dong.circlelive.wigets

/**
 * Create by dooze on 2021/5/14  11:43 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.updateLayoutParams
import com.dong.circlelive.R
import com.dong.circlelive.getColor
import com.dong.circlelive.utils.dp
import com.dong.circlelive.utils.updateViewSize

class StartLiveView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : GradientLoadingContainer(context, attributeSet, defStyle) {

    val tvText = AppCompatTextView(context)

    var status: Status = Status.NONE
        private set

    init {
        tvText.textSize = 24f
        tvText.typeface = Typeface.DEFAULT_BOLD
        tvText.setTextColor(getColor(R.color.white))
        tvText.setText(R.string.live)
        addView(tvText, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            topToTop = LayoutParams.PARENT_ID
            startToStart = LayoutParams.PARENT_ID
            endToEnd = LayoutParams.PARENT_ID
            bottomToBottom = LayoutParams.PARENT_ID
        })
    }


    fun changeStatus(status: Status) {
        when (status) {
            Status.NONE -> {
                updateViewSize(100.dp(), 100.dp())
                tvText.setText(R.string.live)
                tvText.textSize = 24f
                tvText.typeface = Typeface.DEFAULT_BOLD
                updateLayoutParams<LayoutParams> {
                    endToEnd = LayoutParams.PARENT_ID
                    marginStart = 0
                }
                updateGradientColor(getColor(R.color.colorAccent), getColor(R.color.ui_green_tertiary))
            }
            Status.LIVING -> {
                updateViewSize(50.dp(), 50.dp())
                tvText.setText(R.string.finish)
                tvText.textSize = 14f
                tvText.typeface = Typeface.DEFAULT
                updateGradientColor(getColor(R.color.ui_orange_dark_primary), getColor(R.color.ui_orange_tertiary))

                updateLayoutParams<LayoutParams> {
                    endToEnd = NO_ID
                    marginStart = 20.dp()
                }
            }
        }
        this.status = status
    }


    enum class Status {
        NONE, LIVING
    }
}