package com.kangraoo.basektlib.tools.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater.Factory2
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.collection.ArrayMap
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.view.TView.buildLayer
import com.kangraoo.basektlib.tools.view.TView.buildTextDrawable
import com.kangraoo.basektlib.tools.view.statebuilder.BaseBuilder
import com.kangraoo.basektlib.tools.view.statebuilder.ColorStateBuilder
import com.kangraoo.basektlib.tools.view.statebuilder.EmptyBuilder
import com.kangraoo.basektlib.tools.view.statebuilder.ShapeBuilder
import com.kangraoo.basektlib.tools.view.statebuilder.StateListDrawableBuilder
import java.lang.reflect.Constructor

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/04
 * desc :
 * version: 1.0
 */
class LibViewFactory(factory2: Factory2) : ViewFactory(
    factory2
) {
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val view = super.onCreateView(parent, name, context, attrs)
        return libView(parent, name, context, attrs, view)
    }

    private fun libView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet,
        view: View?
    ): View? {
        var view: View? = view
        val shapeArray = context.obtainStyledAttributes(attrs, R.styleable.LibShape) // 可独立使用
        val drawableArray =
            context.obtainStyledAttributes(attrs, R.styleable.LibDrawableSelector) // 可独立使用
        val colorArray = context.obtainStyledAttributes(attrs, R.styleable.LibColorSelector) // 可独立使用
        val rippleArray = context.obtainStyledAttributes(attrs, R.styleable.LibRipple) // 可独立使用
        val buttonDrawable =
            context.obtainStyledAttributes(attrs, R.styleable.LibButtonDrawable) // 可独立使用
        val textViewDrawableArray =
            context.obtainStyledAttributes(attrs, R.styleable.LibTextViewDrawable) // 不可独立使用
        val stokePosition =
            context.obtainStyledAttributes(attrs, R.styleable.LibStokePosition) // 不可独立使用
        val stokeColor =
            context.obtainStyledAttributes(attrs, R.styleable.LibStrokeColorSelector) // 不可独立使用
        val solidColor =
            context.obtainStyledAttributes(attrs, R.styleable.LibSolidColorSelector) // 不可独立使用
        return try {
            if (shapeArray.indexCount == 0 && drawableArray.indexCount == 0 && colorArray.indexCount == 0 && rippleArray.indexCount == 0 && buttonDrawable.indexCount == 0) {
                return view
            }
            if (view == null) {
                view = createViewFromTag(parent, name!!, context, attrs)
            }
            if (view == null) {
                return null
            }
            if (buttonDrawable.indexCount > 0) {
                view.isClickable = true
                (view as CompoundButton).setButtonDrawable(
                    StateListDrawableBuilder().setShapeTypeArray(
                        shapeArray
                    ).setDrawableButtonTypeArray(buttonDrawable).build()
                )
                return view
            }
            var baseBuilder: BaseBuilder<*>? = null
            if (drawableArray.indexCount > 0) {
                view.isClickable = true
                baseBuilder = StateListDrawableBuilder().setShapeTypeArray(shapeArray)
                    .setDrawableTypeArray(drawableArray)
            } else if (shapeArray.indexCount > 0) {
                baseBuilder = ShapeBuilder()
                if (stokeColor.indexCount > 0) {
                    (baseBuilder as ShapeBuilder).setStrokeColorBuild(
                        ColorStateBuilder().setStrokeColorTypeArray(
                            stokeColor
                        )
                    )
                }
                if (solidColor.indexCount > 0) {
                    (baseBuilder as ShapeBuilder).setSolidColorBuild(
                        ColorStateBuilder().setSolidColorTypeArray(
                            solidColor
                        )
                    )
                }
                (baseBuilder as ShapeBuilder).setShapeTypeArray(shapeArray)
            } else {
                baseBuilder = EmptyBuilder().setView(view)
            }
            if (rippleArray.indexCount == 2) {
                val enable = rippleArray.getBoolean(R.styleable.LibRipple_lib_ripple_enable, false)
                val color = rippleArray.getColor(R.styleable.LibRipple_lib_ripple_color, 0)
                view.isClickable = true
                baseBuilder.setRipple(enable, color)
            }
            var drawable: Drawable? = null
            if (baseBuilder is StateListDrawableBuilder) {
                drawable = (baseBuilder as StateListDrawableBuilder).build()
            } else if (baseBuilder is ShapeBuilder) {
                drawable = (baseBuilder as ShapeBuilder).build()
                if (stokePosition.indexCount > 0) {
                    drawable = buildLayer(shapeArray, stokePosition, drawable)
                }
            } else if (baseBuilder is EmptyBuilder) {
                drawable = (baseBuilder as EmptyBuilder).build()
            }
            if (view is TextView && colorArray.indexCount > 0) {
                view.setTextColor(ColorStateBuilder().setColorTypeArray(colorArray).build())
            }
            if (view is TextView && textViewDrawableArray.indexCount > 0) {
                buildTextDrawable((view as TextView?)!!, drawable!!, textViewDrawableArray)
            } else {
                if (drawable != null) {
                    view.background = drawable
                }
            }
            view
        } catch (e: Exception) {
            e.printStackTrace()
            ULog.e(e, e.message)
            view
        } finally {
            shapeArray.recycle()
            drawableArray.recycle()
            colorArray.recycle()
            rippleArray.recycle()
            textViewDrawableArray.recycle()
            stokePosition.recycle()
            stokeColor.recycle()
            solidColor.recycle()
            buttonDrawable.recycle()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    fun createViewFromTag(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
//        return null;
        var name = name
        if (TextUtils.isEmpty(name)) {
            return null
        }
        if (name == "view") {
            name = attrs.getAttributeValue(null, "class")
        }
        return try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs
            /**
             * 如 TextView， View，LinearLayout 等
             */
            if (-1 == name.indexOf('.')) {
                var view: View? = null
                /**
                 * View
                 */
                if ("View" == name) {
                    view = createView(context, name, "android.view.")
                }
                /**
                 * widget 组件
                 */
                if (view == null) {
                    view = createView(context, name, "android.widget.")
                }
                /**
                 * webview
                 */
                if (view == null) {
                    view = createView(context, name, "android.webkit.")
                }
                view
            } else {
                createView(context, name, null)
            }
        } catch (e: Exception) {
            Log.w("BackgroundLibrary", "cannot create 【$name】 : ")
            null
        } finally {
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    companion object {
        private val mConstructorArgs = arrayOfNulls<Any>(2)
        private val sConstructorMap: MutableMap<String, Constructor<out View>> = ArrayMap()
        private val sConstructorSignature = arrayOf(
            Context::class.java, AttributeSet::class.java
        )

        @Throws(InflateException::class)
        private fun createView(context: Context, name: String, prefix: String?): View? {
            //缓存view类型对象，达到提升效率
            var constructor = sConstructorMap[name]
            return try {
                if (constructor == null) {
                    val clazz = context.classLoader.loadClass(
                        if (prefix != null) prefix + name else name
                    ).asSubclass(
                        View::class.java
                    )
                    constructor = clazz.getConstructor(*sConstructorSignature)
                    sConstructorMap[name] = constructor
                }
                constructor!!.isAccessible = true
                constructor.newInstance(*mConstructorArgs)
            } catch (e: Exception) {
                Log.w("BackgroundLibrary", "cannot create 【$name】 : ")
                null
            }
        }
    }
}
