package com.kangraoo.basektlib.widget.toolsbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener
import com.kangraoo.basektlib.widget.toolsbar.option.BaseOption

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/09
 * desc :
 * version: 1.0
 */
class LibToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Toolbar(context, attrs, defStyleAttr), ILibToolbar {
    lateinit var title: TextView
    lateinit var rightLayout: LinearLayout
    lateinit var leftLayout: LinearLayout

    override fun onFinishInflate() {
        super.onFinishInflate()
        title = findViewById(R.id.lib_toolbar_title)
        rightLayout = findViewById(R.id.lib_toolbar_right_layout)
        leftLayout = findViewById(R.id.lib_toolbar_left_layout)
    }

    var mOptions: ToolBarOptions? = null

    override fun setOptions(options: ToolBarOptions) {
        mOptions = options
        title.text = options.titleString
        setBackgroundColor(ContextCompat.getColor(context, options.background))
        title.setTextColor(ContextCompat.getColor(context, options.titleColor))
        title.minHeight = options.height
        if (options.isLeftLayout) {
            leftLayout.visibility = View.VISIBLE
            val leftOption = options.leftOption
            leftOption?.let {
                var imageView1 = findViewById<ImageView>(R.id.lib_toolbar_left_img1)
                var imageView2 = findViewById<ImageView>(R.id.lib_toolbar_left_img2)
                var textView1 = findViewById<TextView>(R.id.lib_toolbar_left_text1)
                var textView2 = findViewById<TextView>(R.id.lib_toolbar_left_text2)
                baseOption(it, imageView1, imageView2, textView1, textView2)
                setLeftOnClickListener(imageView1, imageView2, textView1, textView2)
            }
        } else {
            leftLayout.visibility = View.GONE
        }
        if (options.isRightLayout) {
            rightLayout.visibility = View.VISIBLE
            val rightOption = options.rightOption
            rightOption?.let {
                var imageView1 = findViewById<ImageView>(R.id.lib_toolbar_right_img1)
                var imageView2 = findViewById<ImageView>(R.id.lib_toolbar_right_img2)
                var textView1 = findViewById<TextView>(R.id.lib_toolbar_right_text1)
                var textView2 = findViewById<TextView>(R.id.lib_toolbar_right_text2)
                baseOption(it, imageView1, imageView2, textView1, textView2)

                setRightOnClickListener(imageView1, imageView2, textView1, textView2)
            }
        } else {
            rightLayout.visibility = View.GONE
        }
    }

    private fun setLeftOnClickListener(vararg view: View) {
        for (v in view) {
            v.setOnClickListener(object : OnOnceClickListener() {
                override fun onOnceClick(var1: View) {
                    onToolBarListener?.onLeft(v, v.id)
                }
            })
        }
    }

    private fun setRightOnClickListener(vararg view: View) {
        for (v in view) {
            v.setOnClickListener(object : OnOnceClickListener() {
                override fun onOnceClick(var1: View) {
                    onToolBarListener?.onRight(v, v.id)
                }
            })
        }
    }

    private fun baseOption(baseOption: BaseOption, imageView1: ImageView, imageView2: ImageView, textView1: TextView, textView2: TextView) {
        if (baseOption.isText1) {
            textView1.visibility = View.VISIBLE
            textView1.typeface = baseOption.text1Type
            textView1.textSize = baseOption.text1Size.toFloat()
            textView1.setTextColor(ContextCompat.getColor(context, baseOption.text1Color))
            if (baseOption.text1 != null) {
                textView1.text = context.getString(baseOption.text1!!)
            }
        } else {
            textView1.visibility = View.GONE
        }
        if (baseOption.isText2) {
            textView2.visibility = View.VISIBLE
            textView2.typeface = baseOption.text2Type
            textView2.textSize = baseOption.text2Size.toFloat()
            textView2.setTextColor(ContextCompat.getColor(context, baseOption.text2Color))
            if (baseOption.text2 != null) {
                textView2.text = context.getString(baseOption.text2!!)
            }
        } else {
            textView2.visibility = View.GONE
        }
        if (baseOption.isImage1) {
            imageView1.visibility = View.VISIBLE
            if (baseOption.image1 != null) {
                imageView1.setImageResource(baseOption.image1!!)
            }
            val ps = imageView1.layoutParams
            ps.width = UUi.dp2px(SApplication.context(), baseOption.image1width.toFloat())
            ps.height = UUi.dp2px(SApplication.context(), baseOption.image1height.toFloat())
            imageView1.layoutParams = ps
        } else {
            imageView1.visibility = View.GONE
        }
        if (baseOption.isImage2) {
            imageView2.visibility = View.VISIBLE
            if (baseOption.image2 != null) {
                imageView2.setImageResource(baseOption.image2!!)
            }
            val ps = imageView2.layoutParams
            ps.width = UUi.dp2px(SApplication.context(), baseOption.image2width.toFloat())
            ps.height = UUi.dp2px(SApplication.context(), baseOption.image2height.toFloat())
            imageView2.layoutParams = ps
        } else {
            imageView2.visibility = View.GONE
        }
    }

    override fun setTitle(titleString: String?) {
        mOptions?.titleString = titleString
        title.text = titleString
    }

    var onToolBarListener: OnLibToolBarListener? = null

    override fun setOnLibToolBarListener(onLibToolBarListener: OnLibToolBarListener?) {
        onToolBarListener = onLibToolBarListener
    }

}
