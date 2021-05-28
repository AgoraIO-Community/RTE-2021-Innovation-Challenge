package com.kangraoo.basektlib.widget.menu

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import java.util.*

/**
 * @author shidawei
 * 创建日期：2021/3/11
 * 描述：
 */
class DropDownMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :LinearLayout(context, attrs, defStyleAttr) {
    //记录tabTexts的顺序
    val dropTabViews: MutableList<View> = ArrayList()
    //顶部菜单布局
    private var tabMenuView: LinearLayout
//
//    //底部容器，包含popupMenuViews，maskView
//    private var containerView: FrameLayout? = null

    //弹出菜单父布局
    private var popupMenuViews: FrameLayout? = null

    //遮罩半透明View，点击可关闭DropDownMenu
    private var maskView: View? = null

    //tabMenuView里面选中的tab位置，-1表示未选中
    private var current_tab_position = -1

    private var dividerHeight = 0f

    //分割线颜色
    private var dividerColor:Int = Color.parseColor("#ffcccccc")

    //tab选中颜色
    private var textSelectedColor:Int = Color.parseColor("#ff890c85")

    //tab未选中颜色
    private var textUnselectedColor:Int = Color.parseColor("#ff111111")

    //遮罩颜色
    private var maskColor:Int = Color.parseColor("#88888888")

    //tab字体大小
    private var menuTextSize = 14

    //tab选中图标
    private var menuSelectedIcon = 0

    //tab未选中图标
    private var menuUnselectedIcon = 0

    //icon的方向
    private var iconOrientation: Int = Orientation.right //默认右则

    private var mOrientation: Orientation
    init {
        orientation = VERTICAL

        //为DropDownMenu添加自定义属性

        var menuBackgroundColor = Color.parseColor("#ffffffff")
        var underlineColor  = Color.parseColor("#ffcccccc")
        val a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu)
        underlineColor = a.getColor(R.styleable.DropDownMenu_lib_ddunderlineColor, underlineColor)
        dividerColor = a.getColor(R.styleable.DropDownMenu_lib_dddividerColor, dividerColor)
        textSelectedColor =
            a.getColor(R.styleable.DropDownMenu_lib_ddtextSelectedColor, textSelectedColor)
        textUnselectedColor =
            a.getColor(R.styleable.DropDownMenu_lib_ddtextUnselectedColor, textUnselectedColor)
        menuBackgroundColor =
            a.getColor(R.styleable.DropDownMenu_lib_ddmenuBackgroundColor, menuBackgroundColor)
        maskColor = a.getColor(R.styleable.DropDownMenu_lib_ddmaskColor, maskColor)
        menuTextSize =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_lib_ddmenuTextSize, menuTextSize)
        dividerHeight = a.getDimensionPixelSize(
            R.styleable.DropDownMenu_lib_dddividerHeight,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).toFloat()

        menuSelectedIcon =
            a.getResourceId(R.styleable.DropDownMenu_lib_ddmenuSelectedIcon, menuSelectedIcon)
        menuUnselectedIcon =
            a.getResourceId(R.styleable.DropDownMenu_lib_ddmenuUnselectedIcon, menuUnselectedIcon)
        iconOrientation =
            a.getInt(R.styleable.DropDownMenu_lib_ddmenuIconOrientation, iconOrientation)

        a.recycle()

//初始化位置参数
        //初始化位置参数
        mOrientation = Orientation(getContext())
        mOrientation.init(iconOrientation, menuSelectedIcon, menuUnselectedIcon)

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = LinearLayout(context)
        val params =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tabMenuView.setOrientation(HORIZONTAL)
        tabMenuView.setBackgroundColor(menuBackgroundColor)
        tabMenuView.setLayoutParams(params)
        addView(tabMenuView, 0)


        //为tabMenuView添加下划线
        val underLine = View(getContext())
        underLine.layoutParams =
            LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, UUi.dp2px(
                    SApplication.context(),
                    1.0f
                )
            )
        underLine.setBackgroundColor(underlineColor)
        addView(underLine, 1)


//        //初始化containerView并将其添加到DropDownMenu
//        containerView = FrameLayout(context)
//        containerView.setLayoutParams(
//            FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//            )
//        )
//        addView(containerView, 2)
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    fun setDropDownMenu(
        tabTexts: List<String>, popupViews: List<View>
    ) {
        var containerView: FrameLayout = findViewById(R.id.lib_drop_down_menu_list)
        require(tabTexts.size == popupViews.size) {
            "params not match, tabTexts.size() should be equal" +
                    " popupViews.size()"
        }
        for (i in tabTexts.indices) {
            addTab(tabTexts, i)
        }
        maskView = View(context)
        maskView!!.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        maskView!!.setBackgroundColor(maskColor)
        maskView!!.setOnClickListener { closeMenu() }
        containerView.addView(maskView, 1)
        maskView!!.visibility = GONE
        popupMenuViews = FrameLayout(context)
        popupMenuViews!!.visibility = GONE
        containerView.addView(popupMenuViews, 2)
        for (i in popupViews.indices) {
            if (popupViews[i].layoutParams == null) {
                popupViews[i].layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            }
            popupMenuViews!!.addView(popupViews[i], i)
        }
    }

//    /**
//     * 初始化DropDownMenu
//     *
//     * @param tabTexts
//     * @param popupViews
//     * @param contentView
//     */
//    fun setDropDownMenu(
//        tabTexts: List<String>, popupViews: List<View>,
//        contentView: View
//    ) {
//        require(tabTexts.size == popupViews.size) {
//            "params not match, tabTexts.size() should be equal" +
//                    " popupViews.size()"
//        }
//        for (i in tabTexts.indices) {
//            addTab(tabTexts, i)
//        }
//        containerView.addView(contentView, 0)
//        maskView = View(context)
//        maskView!!.layoutParams =
//            FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//            )
//        maskView!!.setBackgroundColor(maskColor)
//        maskView!!.setOnClickListener { closeMenu() }
//        containerView.addView(maskView, 1)
//        maskView!!.visibility = GONE
//        popupMenuViews = FrameLayout(context)
//        popupMenuViews!!.visibility = GONE
//        containerView.addView(popupMenuViews, 2)
//        for (i in popupViews.indices) {
//            if (popupViews[i].layoutParams == null) {
//                popupViews[i].layoutParams =
//                    ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//            }
//            popupMenuViews!!.addView(popupViews[i], i)
//        }
//    }

    /**
     * 自定义插入的tabView，如果包含R.id.tv_tab就当做普通的tabtext会统一做变色处理和向下的角标处理
     *
     * @param tab
     * @param index 0start
     */
    fun addTab(tab: View?, index: Int) {
        if (index == (tabMenuView.childCount + 1) / 2) {
            addTabEnd(tab)
            return
        }
        tabMenuView.addView(tab, index * 2)
        tabMenuView.addView(getDividerView(), index * 2 + 1)
    }

    fun addTabEnd(tab: View?) {
        tabMenuView.addView(getDividerView(), tabMenuView.childCount)
        tabMenuView.addView(tab, tabMenuView.childCount)
    }

    private fun addTab(tabTexts: List<String>, i: Int) {
        val tab = inflate(context, R.layout.lib_layout_tab_item, null)
        tab.layoutParams = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
        val textView = getTabTextView(tab)
        textView.text = tabTexts[i]
        textView.setTextColor(textUnselectedColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize.toFloat())
        setTextDrawables(textView, true)
        tabMenuView.addView(tab)
        tab.setOnClickListener { v ->
            if (itemMenuClickListener != null) {
                itemMenuClickListener!!.OnItemMenuClick(textView, i)
            }
            switchMenu(v)
        }

        //添加分割线
        if (i < tabTexts.size - 1) {
            tabMenuView.addView(getDividerView())
        }
        dropTabViews.add(tab)
    }

    private fun getDividerView(): View? {
        val view = View(context)
        val height = if (dividerHeight > 0) UUi.dp2px(SApplication.context(), dividerHeight) else dividerHeight.toInt()
        val params = LayoutParams(
            UUi.dp2px(SApplication.context(), 0.5f),
            height
        )
        params.gravity = Gravity.CENTER_VERTICAL
        view.layoutParams = params
        view.setBackgroundColor(dividerColor)
        return view
    }

    /**
     * 获取tabView中id为tv_tab的textView
     *
     * @param tabView
     * @return
     */
    private fun getTabTextView(tabView: View): TextView {
        return tabView.findViewById<View>(R.id.tv_tab) as TextView
    }

    fun setCurrentTabPosition(current_tab_position: Int) {
        this.current_tab_position = current_tab_position
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    fun setTabText(text: String?) {
        if (current_tab_position != -1) {
            getTabTextView(tabMenuView.getChildAt(current_tab_position)).text = text
        }
    }

    fun setTabClickable(clickable: Boolean) {
        var i = 0
        while (i < tabMenuView.childCount) {
            tabMenuView.getChildAt(i).isClickable = clickable
            i = i + 2
        }
    }

    /**
     * 关闭菜单
     */
    fun closeMenu() {
        if (current_tab_position != -1) {
            val textView = getTabTextView(tabMenuView.getChildAt(current_tab_position))
            textView.setTextColor(textUnselectedColor)
            setTextDrawables(textView, true)
            popupMenuViews!!.visibility = GONE
            popupMenuViews!!.animation =
                AnimationUtils.loadAnimation(context, R.anim.lib_dd_menu_out)
            maskView!!.visibility = GONE
            maskView!!.animation = AnimationUtils.loadAnimation(context, R.anim.lib_dd_mask_out)
            current_tab_position = -1
        }
    }

    fun isActive(): Boolean {
        return current_tab_position != -1
    }

    fun setTextDrawables(textview: TextView, close: Boolean) {
        textview.setCompoundDrawablesWithIntrinsicBounds(
            mOrientation.getLeft(close), mOrientation.getTop(close),
            mOrientation.getRight(close), mOrientation.getBottom(close)
        )
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    fun isShowing(): Boolean {
        return current_tab_position != -1
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private fun switchMenu(target: View) {
        println(current_tab_position)
        var i = 0
        while (i < tabMenuView.childCount) {
            if (target === tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu() //关闭
                } else { //打开
                    if (current_tab_position == -1) {
                        popupMenuViews!!.visibility = VISIBLE
                        popupMenuViews!!.animation = AnimationUtils.loadAnimation(
                            context, R.anim.lib_dd_menu_in
                        )
                        maskView!!.visibility = VISIBLE
                        maskView!!.animation = AnimationUtils.loadAnimation(
                            context, R.anim.lib_dd_mask_in
                        )
                    }
                    val listView = getListView(tabMenuView.getChildAt(i))
                    if (listView != null) {
                        listView.visibility = VISIBLE
                    }
                    current_tab_position = i
                    val textView = getTabTextView(tabMenuView.getChildAt(i))
                    textView.setTextColor(textSelectedColor)
                    setTextDrawables(textView, false)
                }
            } else { //关闭
                val textView = getTabTextView(tabMenuView.getChildAt(i))
                textView?.setTextColor(textUnselectedColor)
                val listView = getListView(tabMenuView.getChildAt(i))
                if (listView != null) {
                    if (textView != null) {
                        setTextDrawables(textView, true)
                    }
                    listView.visibility = GONE
                }
            }
            i = i + 2
        }
    }

    var itemMenuClickListener: OnItemMenuClickListener? = null

    fun setOnItemMenuClickListener(listener: OnItemMenuClickListener?) {
        itemMenuClickListener = listener
    }

    interface OnItemMenuClickListener {
        fun OnItemMenuClick(tabView: TextView?, position: Int)
    }

    /**
     * 获取dropTabViews中对应popupMenuViews数组中的ListView
     *
     * @param view
     * @return
     */
    private fun getListView(view: View): View? {
        return if (dropTabViews.contains(view)) {
            val index: Int = dropTabViews.indexOf(view)
            popupMenuViews!!.getChildAt(index)
        } else {
            null
        }
    }

}

internal class Orientation(var mContext: Context) {
    private var unSelectedDrawable: Drawable? = null
    private var selectedDrawable: Drawable? = null
    private var orientation = 0
    fun getLeft(close: Boolean): Drawable? {
        return if (orientation == left) if (close) unSelectedDrawable else selectedDrawable else null
    }

    fun getTop(close: Boolean): Drawable? {
        return if (orientation == top) if (close) unSelectedDrawable else selectedDrawable else null
    }

    fun getRight(close: Boolean): Drawable? {
        return if (orientation == right) if (close) unSelectedDrawable else selectedDrawable else null
    }

    fun getBottom(close: Boolean): Drawable? {
        return if (orientation == bottom) if (close) unSelectedDrawable else selectedDrawable else null
    }

    /**
     * 初始化位置参数
     *
     * @param orientation
     * @param menuUnselectedIcon
     */
    fun init(orientation: Int, menuSelectedIcon: Int, menuUnselectedIcon: Int) {
        unSelectedDrawable = mContext.resources.getDrawable(menuUnselectedIcon)
        selectedDrawable = mContext.resources.getDrawable(menuSelectedIcon)
        this.orientation = orientation
    }

    companion object {
        const val left = 0
        const val top = 1
        const val right = 2
        const val bottom = 3
    }
}