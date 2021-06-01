package com.kangraoo.basektlib.tools.view.statebuilder

import android.content.res.TypedArray
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.IntDef
import com.kangraoo.basektlib.R

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/03
 * desc :
 * version: 1.0
 */
class ShapeBuilder : BaseBuilder<ShapeBuilder>() {
    @IntDef(RECTANGLE, OVAL, LINE, RING)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ShapeType

    @IntDef(LINEAR, RADIAL, SWEEP)
    @Retention(AnnotationRetention.SOURCE)
    annotation class GradientType

    private var useLevel = false

    @ShapeType
    private var shape: Int? = RECTANGLE
    private var solidColor: Int? = null

    @GradientType
    private var gradient: Int? = null
    private var cornersRadius: Float? = null
    private var cornersBottomLeftRadius: Float? = null
    private var cornersBottomRightRadius: Float? = null
    private var cornersTopLeftRadius: Float? = null
    private var cornersTopRightRadius: Float? = null
    private var gradientAngle: Int? = null
    private var gradientCenterX: Float? = null
    private var gradientCenterY: Float? = null
    private var gradientCenterColor: Int? = null
    private var gradientEndColor: Int? = null
    private var gradientStartColor: Int? = null
    private var gradientRadius: Float? = null
    private val padding = Rect()
    private var sizeWidth: Float? = null
    private var sizeHeight: Float? = null
    private var strokeWidth: Float? = null
    private var strokeColor: Int? = null
    private var strokeDashWidth = 0f
    private var strokeDashGap = 0f
    fun setShape(@ShapeType shape: Int): ShapeBuilder {
        this.shape = shape
        return this
    }

    fun setGradient(gradient: Int): ShapeBuilder {
        this.gradient = gradient
        return this
    }

    fun setGradientAngle(gradientAngle: Int?): ShapeBuilder {
        this.gradientAngle = gradientAngle
        return this
    }

    fun setSolidColor(solidColor: Int): ShapeBuilder {
        this.solidColor = solidColor
        return this
    }

    fun setUseLevel(useLevel: Boolean): ShapeBuilder {
        this.useLevel = useLevel
        return this
    }

    fun setCornersRadius(cornersRadius: Float): ShapeBuilder {
        this.cornersRadius = cornersRadius
        return this
    }

    fun setCornersRadius(
        cornersBottomLeftRadius: Float,
        cornersBottomRightRadius: Float,
        cornersTopLeftRadius: Float,
        cornersTopRightRadius: Float
    ): ShapeBuilder {
        this.cornersBottomLeftRadius = cornersBottomLeftRadius
        this.cornersBottomRightRadius = cornersBottomRightRadius
        this.cornersTopLeftRadius = cornersTopLeftRadius
        this.cornersTopRightRadius = cornersTopRightRadius
        return this
    }

    fun setGradientRadius(gradientRadius: Float): ShapeBuilder {
        this.gradientRadius = gradientRadius
        return this
    }

    fun setGradientColor(startColor: Int, endColor: Int): ShapeBuilder {
        gradientStartColor = startColor
        gradientEndColor = endColor
        return this
    }

    fun setGradientCenterXY(gradientCenterX: Float, gradientCenterY: Float): ShapeBuilder {
        this.gradientCenterX = gradientCenterX
        this.gradientCenterY = gradientCenterY
        return this
    }

    fun setGradientColor(startColor: Int, centerColor: Int, endColor: Int): ShapeBuilder {
        gradientStartColor = startColor
        gradientCenterColor = centerColor
        gradientEndColor = endColor
        return this
    }

    fun setPadding(
        paddingLeft: Float,
        paddingTop: Float,
        paddingRight: Float,
        paddingBottom: Float
    ): ShapeBuilder {
        padding.left = paddingLeft.toInt()
        padding.top = paddingTop.toInt()
        padding.right = paddingRight.toInt()
        padding.bottom = paddingBottom.toInt()
        return this
    }

    fun setSizeWidth(sizeWidth: Float): ShapeBuilder {
        this.sizeWidth = sizeWidth
        return this
    }

    fun setSizeHeight(sizeHeight: Float): ShapeBuilder {
        this.sizeHeight = sizeHeight
        return this
    }

    fun setStrokeWidth(strokeWidth: Float): ShapeBuilder {
        this.strokeWidth = strokeWidth
        return this
    }

    fun setStrokeColor(strokeColor: Int): ShapeBuilder {
        this.strokeColor = strokeColor
        return this
    }

    var strokeColorBuild: ColorStateBuilder? = null
    var solidColorBuild: ColorStateBuilder? = null
    fun setStrokeColorBuild(strokeColorBuild: ColorStateBuilder?): ShapeBuilder {
        this.strokeColorBuild = strokeColorBuild
        return this
    }

    fun setSolidColorBuild(solidColorBuild: ColorStateBuilder?): ShapeBuilder {
        this.solidColorBuild = solidColorBuild
        return this
    }

    fun setShapeTypeArray(typedArray: TypedArray): ShapeBuilder {
        for (i in 0 until typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.LibShape_lib_shape) {
                shape = typedArray.getInt(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_solid_color) {
                solidColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_corners_radius) {
                cornersRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_corners_bottom_left_radius) {
                cornersBottomLeftRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_corners_bottom_right_radius) {
                cornersBottomRightRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_corners_top_left_radius) {
                cornersTopLeftRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_corners_top_right_radius) {
                cornersTopRightRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_gradient_angle) {
                gradientAngle = typedArray.getInteger(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_gradient_center_x) {
                gradientCenterX = typedArray.getFloat(attr, -1f)
            } else if (attr == R.styleable.LibShape_lib_gradient_center_y) {
                gradientCenterY = typedArray.getFloat(attr, -1f)
            } else if (attr == R.styleable.LibShape_lib_gradient_center_color) {
                gradientCenterColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_gradient_end_color) {
                gradientEndColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_gradient_start_color) {
                gradientStartColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_gradient_gradient_radius) {
                gradientRadius = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_gradient_type) {
                gradient = typedArray.getInt(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_gradient_useLevel) {
                useLevel = typedArray.getBoolean(attr, false)
            } else if (attr == R.styleable.LibShape_lib_padding_left) {
                padding.left = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.LibShape_lib_padding_top) {
                padding.top = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.LibShape_lib_padding_right) {
                padding.right = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.LibShape_lib_padding_bottom) {
                padding.bottom = typedArray.getDimension(attr, 0f).toInt()
            } else if (attr == R.styleable.LibShape_lib_size_width) {
                sizeWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_size_height) {
                sizeHeight = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_stroke_width) {
                strokeWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_stroke_color) {
                strokeColor = typedArray.getColor(attr, 0)
            } else if (attr == R.styleable.LibShape_lib_stroke_dash_width) {
                strokeDashWidth = typedArray.getDimension(attr, 0f)
            } else if (attr == R.styleable.LibShape_lib_stroke_dash_gap) {
                strokeDashGap = typedArray.getDimension(attr, 0f)
            }
        }
        return this
    }

    fun setStrokeDashWidth(strokeDashWidth: Float): ShapeBuilder {
        this.strokeDashWidth = strokeDashWidth
        return this
    }

    fun setStrokeDashGap(strokeDashGap: Float): ShapeBuilder {
        this.strokeDashGap = strokeDashGap
        return this
    }

    fun buildNoRipple(): GradientDrawable {
        val drawable = GradientDrawable()
        if (shape == null) {
            throw NullPointerException()
        }
        drawable.shape = shape!!
        if (cornersRadius != null) {
            drawable.cornerRadius = cornersRadius!!
        } else {
            if (cornersBottomLeftRadius != null && cornersBottomRightRadius != null && cornersTopLeftRadius != null && cornersTopRightRadius != null) {
                val cornerRadius = FloatArray(8)
                cornerRadius[0] = cornersTopLeftRadius!!
                cornerRadius[1] = cornersTopLeftRadius!!
                cornerRadius[2] = cornersTopRightRadius!!
                cornerRadius[3] = cornersTopRightRadius!!
                cornerRadius[4] = cornersBottomRightRadius!!
                cornerRadius[5] = cornersBottomRightRadius!!
                cornerRadius[6] = cornersBottomLeftRadius!!
                cornerRadius[7] = cornersBottomLeftRadius!!
                drawable.cornerRadii = cornerRadius
            }
        }
        if (solidColorBuild != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.color = solidColorBuild!!.build()
            } else {
                if (solidColor != null) {
                    drawable.setColor(solidColor!!)
                }
            }
        } else {
            if (solidColor != null) {
                drawable.setColor(solidColor!!)
            }
        }
        if (gradient != null) {
            if (gradient == LINEAR && gradientAngle != null) {
                gradientAngle = gradientAngle!! % 360
                if (gradientAngle!! % 45 == 0) {
                    var mOrientation = GradientDrawable.Orientation.LEFT_RIGHT
                    when (gradientAngle) {
                        0 -> mOrientation = GradientDrawable.Orientation.LEFT_RIGHT
                        45 -> mOrientation = GradientDrawable.Orientation.BL_TR
                        90 -> mOrientation = GradientDrawable.Orientation.BOTTOM_TOP
                        135 -> mOrientation = GradientDrawable.Orientation.BR_TL
                        180 -> mOrientation = GradientDrawable.Orientation.RIGHT_LEFT
                        225 -> mOrientation = GradientDrawable.Orientation.TR_BL
                        270 -> mOrientation = GradientDrawable.Orientation.TOP_BOTTOM
                        315 -> mOrientation = GradientDrawable.Orientation.TL_BR
                    }
                    drawable.orientation = mOrientation
                }
            }
            if (gradientCenterX != null && gradientCenterY != null) {
                drawable.setGradientCenter(gradientCenterX!!, gradientCenterY!!)
            }
            if (gradientStartColor != null && gradientEndColor != null) {
                val colors: IntArray
                if (gradientCenterColor != null) {
                    colors = IntArray(3)
                    colors[0] = gradientStartColor!!
                    colors[1] = gradientCenterColor!!
                    colors[2] = gradientEndColor!!
                } else {
                    colors = IntArray(2)
                    colors[0] = gradientStartColor!!
                    colors[1] = gradientEndColor!!
                }
                drawable.setColors(colors)
            }
            if (gradientRadius != null) {
                drawable.gradientRadius = gradientRadius!!
            }
            drawable.gradientType = gradient!!
        }
        drawable.useLevel = useLevel
        if (!padding.isEmpty) {
            drawable.getPadding(padding)
        }
        if (sizeWidth != null && sizeHeight != null) {
            drawable.setSize(sizeWidth!!.toInt(), sizeHeight!!.toInt())
        }
        if (strokeWidth != null && strokeWidth!! > 0) {
            if (strokeColorBuild != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setStroke(
                        strokeWidth!!.toInt(),
                        strokeColorBuild!!.build(),
                        strokeDashWidth,
                        strokeDashGap
                    )
                } else {
                    if (strokeColor != null) {
                        drawable.setStroke(
                            strokeWidth!!.toInt(),
                            strokeColor!!,
                            strokeDashWidth,
                            strokeDashGap
                        )
                    }
                }
            } else if (strokeColor != null) {
                drawable.setStroke(
                    strokeWidth!!.toInt(),
                    strokeColor!!,
                    strokeDashWidth,
                    strokeDashGap
                )
            }
        }
        return drawable
    }

    fun build(): Drawable {
        return buildRipple(buildNoRipple())
    }

    companion object {
        const val RECTANGLE = 0
        const val OVAL = 1
        const val LINE = 2
        const val RING = 3
        const val LINEAR = 0
        const val RADIAL = 1
        const val SWEEP = 2
    }
}
