package com.kangraoo.basektlib.widget.floatwindow

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.R2
import com.kangraoo.basektlib.app.ActivityLifeManager.getCurrentActivity
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.HString.concatObject
import com.kangraoo.basektlib.tools.SSystem.getAppName
import com.kangraoo.basektlib.tools.SSystem.isMainThread
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.log.ILog
import com.kangraoo.basektlib.widget.floatwindow.adapter.ConsoleTestAdapter
import com.kangraoo.basektlib.widget.floatwindow.adapter.OnConsoleItemClickListener
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener
import java.util.*

/**
 * Created by feifan on 2017/8/21.
 * Contacts me:404619986@qq.com
 */
class FloatWindowsView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ILog {
    var unbinder: Unbinder

    @BindView(R2.id.minmax)
    lateinit var minmax: TextView

    @BindView(R2.id.clear)
    lateinit var clear: TextView

    @BindView(R2.id.close)
    lateinit var close: TextView

    @BindView(R2.id.debugLog)
    lateinit var debugLog: TextView

    @BindView(R2.id.console)
    lateinit var console: LinearLayout

    @BindView(R2.id.fontDebug)
    lateinit var fontDebug: TextView

    @BindView(R2.id.debugRecycle)
    lateinit var debugRecycle: RecyclerView
    var consoleTestAdapter: ConsoleTestAdapter
    var isMin = true

    //
    //    @Override
    //    public boolean dispatchTouchEvent(MotionEvent ev) {
    //        if(ev.getAction() == MotionEvent.ACTION_DOWN){
    //            getParent().requestDisallowInterceptTouchEvent(true);
    //        }
    //        return super.dispatchTouchEvent(ev);
    //    }
    //
    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent ev) {
    //        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
    //            View v = findFocus();
    //            if(v!=null&&v == debugLog&&isMin){
    //                return true;
    //            }
    //        }
    //        return super.onInterceptTouchEvent(ev);
    //    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isMin) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveX = event.rawX - lastX
                    val moveY = event.rawY - lastY
                    lastX = event.rawX
                    lastY = event.rawY
                    if (mTouchHandler != null) {
                        mTouchHandler!!.onMove(moveX, moveY)
                    }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    if (mTouchHandler != null) {
                        mTouchHandler!!.onMoveEnd()
                    }
                }
            }
        }
        return false
    }

    private var mTouchHandler: OnTouchHandler? = null
    fun setOnTouchHandler(touchHandler: OnTouchHandler?) {
        mTouchHandler = touchHandler
    }

    override fun d(vararg message: Any?) {
        toLog("d", *message)
    }

    override fun dt(tag: String, vararg message: Any?) {}

    override fun e(vararg message: Any?) {
        toLog("e", *message)
    }

    override fun e(throwable: Throwable, vararg message: Any?) {
        toLog("e", throwable.message, *message)
    }

    override fun et(tag: String, vararg message: Any?) {}

    override fun et(tag: String, throwable: Throwable, vararg message: Any?) {}

    override fun w(vararg message: Any?) {
        toLog("w", *message)
    }

    override fun wt(tag: String, vararg message: Any?) {}

    override fun i(vararg message: Any?) {
        toLog("i", *message)
    }

    override fun it(tag: String, vararg message: Any?) {}

    override fun v(vararg message: Any?) {
        toLog("v", *message)
    }

    override fun vt(tag: String, vararg message: Any?) {}

    override fun wtf(vararg message: Any?) {
        toLog("wtf", *message)
    }

    override fun wtft(tag: String, vararg message: Any?) {}

    override fun dm(message: String, vararg args: Any?) {
        toLog("d", String.format(message, *args))
    }

    override fun dmt(tag: String, message: String, vararg args: Any?) {}

    override fun em(message: String, vararg args: Any?) {
        toLog("e", String.format(message, *args))
    }

    override fun em(throwable: Throwable, message: String, vararg args: Any?) {
        toLog("e", throwable.message, String.format(message, *args))
    }

    override fun emt(tag: String, message: String, vararg args: Any?) {
        toLog("e", tag + String.format(message, *args))
    }

    override fun emt(tag: String, throwable: Throwable, message: String, vararg args: Any?) {
        toLog("e", throwable.message, throwable.message + String.format(message, *args))

    }

    override fun wm(message: String, vararg args: Any?) {
        toLog("w", String.format(message, *args))
    }

    override fun wmt(tag: String, message: String, vararg args: Any?) {
        toLog("w", tag + String.format(message, *args))

    }

    override fun im(message: String, vararg args: Any?) {
        toLog("i", String.format(message, *args))
    }

    override fun imt(tag: String, message: String, vararg args: Any?) {
        toLog("i", tag + String.format(message, *args))

    }

    override fun vm(message: String, vararg args: Any?) {
        toLog("v", String.format(message, *args))
    }

    override fun vmt(tag: String, message: String, vararg args: Any?) {
        toLog("v", tag + String.format(message, *args))

    }

    override fun wtfm(message: String, vararg args: Any?) {
        toLog("wtf", String.format(message, *args))
    }

    override fun wtfmt(tag: String, message: String, vararg args: Any?) {
        toLog("wtf", tag + String.format(message, *args))
    }

    override fun json(json: String?) {
        toLog("json", format(json))
    }

    override fun jsont(tag: String, json: String?) {
        toLog("json", tag+format(json))

    }

    override fun xml(xml: String) {
        toLog("xml", xml)
    }

    override fun xmlt(tag: String, xml: String) {
        toLog("json", tag+format(xml))
    }

    override fun o(o: Any?) {
        json(HJson.toJson(o))
    }

    override fun ot(tag: String, any: Any?) {
        jsont(tag,HJson.toJson(any))
    }

    private fun toLog(tag: String, e: String?, vararg message: Any?) {
        if (isMainThread()) {
            debugLog!!.append(
                getAppName(SApplication.context()) + "_" + tag + ">" + e + " " + concatObject(
                    " ",
                    *message
                ) + "\n"
            )
            refreshView()
        } else {
            HAction.mainHandler.post(Runnable {
                debugLog!!.append(
                    getAppName(SApplication.context()) + "_" + tag + ">" + e + " " + concatObject(
                        " ",
                        *message
                    ) + "\n"
                )
                refreshView()
            })
        }
    }

    private fun refreshView() {
        val offset = debugLog!!.lineCount * debugLog!!.lineHeight
        if (offset > debugLog!!.height) {
            debugLog!!.scrollTo(0, offset - debugLog!!.height)
        }
    }

    private fun toLog(tag: String, vararg message: Any?) {
        if (isMainThread()) {
            debugLog.append(getAppName(SApplication.context()) + "_" + tag + ">" + concatObject(
                " ",
                *message
            ) + "\n")
            refreshView()
        } else {
            HAction.mainHandler.post(Runnable {
                debugLog.append(getAppName(SApplication.context()) + "_" + tag + ">" + concatObject(
                    " ",
                    *message
                ) + "\n")
                refreshView()
            })
        }
    }

    var iConsoleTest: IConsoleTest? = null
    fun testFloatView() {
        val activity = getCurrentActivity()
        if (activity != null && activity is IConsoleTest) {
            iConsoleTest = activity
            val map = iConsoleTest!!.floatTest() ?: return
            debugRecycle!!.visibility = VISIBLE
            val keys = map.keys
            val listKeys: List<String> = ArrayList(keys)
            consoleTestAdapter.refresh(listKeys)
            consoleTestAdapter.onConsoleItemClickListener = object : OnConsoleItemClickListener {
                override fun consoleClick(key: String, view: View) {
                    val onClickListener = map[key]
                    onClickListener?.onClick(view)
                }
            }
        }
    }

    fun testFloatViewGone() {
        iConsoleTest = null
        debugRecycle!!.visibility = GONE
        consoleTestAdapter.clean()
    }

    interface OnTouchHandler {
        fun onMove(moveX: Float, moveY: Float)
        fun onMoveEnd()
        fun min()
        fun max()
        fun close()
        fun debug()
    }

    companion object {
        private var lastX = 0f
        private var lastY = 0f
        fun format(json: String?): String {
            val indent = StringBuilder() // 缩进
            val sb = StringBuilder()
            for (c in json!!.toCharArray()) {
                when (c) {
                    '{' -> {
                        indent.append(" ")
                        sb.append("{\n").append(indent)
                    }
                    '}' -> {
                        indent.deleteCharAt(indent.length - 1)
                        sb.append("\n").append(indent).append("}")
                    }
                    '[' -> {
                        indent.append(" ")
                        sb.append("[\n").append(indent)
                    }
                    ']' -> {
                        indent.deleteCharAt(indent.length - 1)
                        sb.append("\n").append(indent).append("]")
                    }
                    ',' -> sb.append(",\n").append(indent)
                    else -> sb.append(c)
                }
            }
            return sb.toString()
        }
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.lib_system_console, this, true)
        unbinder = ButterKnife.bind(this, view)
        UFont.setTextViewFont(fontDebug, R.string.lib_icon_terminal)
        UFont.setTextViewFont(minmax, R.string.lib_icon_enlarge2)
        //        UFont.getInstance().setTextViewFont(max,R.string.lib_icon_enlarge2);
        UFont.setTextViewFont(clear, R.string.lib_icon_bin)
        UFont.setTextViewFont(close, R.string.lib_icon_cross)
        clear!!.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(var1: View) {
                debugLog!!.text = ""
            }
        })
        minmax!!.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(var1: View) {
                if (mTouchHandler != null) {
                    if (isMin) {
                        debugLog!!.movementMethod = ScrollingMovementMethod.getInstance()
                        UFont.setTextViewFont(minmax, R.string.lib_icon_shrink2)
                        mTouchHandler!!.max()
                    } else {
                        debugLog!!.movementMethod = null
                        UFont.setTextViewFont(minmax, R.string.lib_icon_enlarge2)
                        mTouchHandler!!.min()
                    }
                    isMin = !isMin
                }
            }
        })
        close!!.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(var1: View) {
                if (mTouchHandler != null) {
                    mTouchHandler!!.close()
                }
            }
        })
        fontDebug!!.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(var1: View) {
                if (mTouchHandler != null) {
                    mTouchHandler!!.debug()
                }
            }
        })
        debugLog.setOnLongClickListener {
            debugLog.setTextIsSelectable(true)
            false
        }
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        consoleTestAdapter = ConsoleTestAdapter(context!!)
        debugRecycle!!.layoutManager = layoutManager
        debugRecycle!!.setAdapter(consoleTestAdapter)
    }
}
