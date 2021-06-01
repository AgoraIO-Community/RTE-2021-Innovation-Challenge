package com.qdedu.baselibcommon.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.zhpan.indicator.base.BaseIndicatorView

/**
 * @author shidawei
 * 创建日期：2021/3/29
 * 描述：
 */
class BannerViewPagerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseIndicatorView(context, attrs, defStyleAttr) {


    var paint: Paint = Paint()

    var rectF = RectF()

    var radius: Float = UUi.dp2px(SApplication.context(), 8.5f).toFloat()

    private var bgColor: Int = Color.parseColor("#99000000")

    var textSize: Float = UUi.dp2px(SApplication.context(), 12f).toFloat()

    var textColor = Color.WHITE

    override fun setBackgroundColor(color: Int) {
        this.bgColor = color
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = bgColor
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas!!.drawRoundRect(rectF, radius, radius, paint)


        paint.color = textColor
        paint.textSize = textSize
        var text = "${currentPosition + 1}/${pageSize}"
        //测量字体宽度
        var textWidth = paint.measureText(text)
        var fontMetricsInt = paint.fontMetricsInt
        var baseline =
            (measuredHeight - fontMetricsInt.bottom + fontMetricsInt.top) / 2 - fontMetricsInt.top
        canvas.drawText(text, (width - textWidth) / 2, baseline.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            UUi.dp2px(SApplication.context(), 35f),
            UUi.dp2px(SApplication.context(), 17f)
        )
    }

}