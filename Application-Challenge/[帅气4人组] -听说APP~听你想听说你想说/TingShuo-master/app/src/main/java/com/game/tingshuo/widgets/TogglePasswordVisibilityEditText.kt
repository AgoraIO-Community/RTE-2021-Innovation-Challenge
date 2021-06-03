package com.game.tingshuo.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Selection
import android.text.Spannable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.game.tingshuo.R

class TogglePasswordVisibilityEditText @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context!!, attrs, defStyleAttr) {
    //切换drawable的引用
    private var visibilityDrawable: Drawable? = null
    private var visibililty = false
    private fun init() {

        //获得该EditText的left ,top ,right,bottom四个方向的drawable
        val compoundDrawables = compoundDrawables
        visibilityDrawable = compoundDrawables[2]
        if (visibilityDrawable == null) {
            visibilityDrawable = resources.getDrawable(R.mipmap.icon_eyeclose)
        }
    }

    /**
     * 用按下的位置来模拟点击事件
     * 当按下的点的位置 在  EditText的宽度 - (图标到控件右边的间距 + 图标的宽度)  和
     * EditText的宽度 - 图标到控件右边的间距 之间就模拟点击事件，
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                var xFlag = false
                val yFlag = false
                //得到用户的点击位置，模拟点击事件
                xFlag = event.x > width - (visibilityDrawable!!.intrinsicWidth + compoundPaddingRight) &&
                            event.x < width - (totalPaddingRight - compoundPaddingRight)
                if (xFlag) {
                    visibililty = !visibililty
                    if (visibililty) {
                        visibilityDrawable = resources.getDrawable(R.mipmap.icon_eye)
                        /*this.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
                        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    } else {
                        //隐藏密码
                        visibilityDrawable = resources.getDrawable(R.mipmap.icon_eyeclose)
                        //this.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                        this.transformationMethod = PasswordTransformationMethod.getInstance()
                    }

                    //将光标定位到指定的位置
                    val text: CharSequence? = this.text
                    if (text is Spannable) {
                        Selection.setSelection(text, text.length)
                    }
                    //调用setCompoundDrawables方法时，必须要为drawable指定大小，不然不会显示在界面上
                    visibilityDrawable!!.setBounds(0, 0, visibilityDrawable!!.minimumWidth, visibilityDrawable!!.minimumHeight)
                    setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], visibilityDrawable, compoundDrawables[3])
                }
            }
        }
        return super.onTouchEvent(event)
    }

    init {
        init()
    }
}