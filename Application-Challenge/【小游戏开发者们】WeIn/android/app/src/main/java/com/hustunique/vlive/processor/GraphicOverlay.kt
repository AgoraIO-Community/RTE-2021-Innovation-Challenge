package com.hustunique.vlive.processor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

class GraphicOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val graphics: MutableList<Graphic> = ArrayList()

    // Matrix for transforming from image coordinates to overlay view coordinates.
    private val transformationMatrix = Matrix()

    private var imageWidth = 0
    private var imageHeight = 0

    // The factor of overlay View size to image size. Anything in the image coordinates need to be
    // scaled by this amount to fit with the area of overlay View.
    private var scaleFactor = 1.0f

    // The number of horizontal pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleWidthOffset = 0f

    // The number of vertical pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleHeightOffset = 0f
    private var isImageFlipped = false
    private var needUpdateTransformation = true

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the [Graphic.draw] method to define the graphics element. Add
     * instances to the overlay using [GraphicOverlay.add].
     */
    abstract class Graphic(private val overlay: GraphicOverlay) {
        /**
         * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
         * to view coordinates for the graphics that are drawn:
         *
         *
         *  1. [Graphic.scale] adjusts the size of the supplied value from the image
         * scale to the view scale.
         *  1. [Graphic.translateX] and [Graphic.translateY] adjust the
         * coordinate from the image's coordinate system to the view coordinate system.
         *
         *
         * @param canvas drawing canvas
         */
        abstract fun draw(canvas: Canvas?)

        /** Adjusts the supplied value from the image scale to the view scale.  */
        fun scale(imagePixel: Float): Float {
            return imagePixel * overlay.scaleFactor
        }

        /** Returns the application context of the app.  */
        val applicationContext: Context
            get() = overlay.context.applicationContext

        fun isImageFlipped(): Boolean {
            return overlay.isImageFlipped
        }

        fun translateX(x: Float): Float {
            return if (overlay.isImageFlipped) {
                overlay.width - (scale(x) - overlay.postScaleWidthOffset)
            } else {
                scale(x) - overlay.postScaleWidthOffset
            }
        }

        fun translateY(y: Float): Float {
            return scale(y) - overlay.postScaleHeightOffset
        }

        fun getTransformationMatrix(): Matrix {
            return overlay.transformationMatrix
        }

        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }

    init {
        addOnLayoutChangeListener { _: View, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int ->
            needUpdateTransformation = true
        }
    }

    @Synchronized
    fun clear() {
        graphics.clear()
        postInvalidate()
    }

    @Synchronized
    fun add(graphic: Graphic) {
        graphics.add(graphic)
    }

    @Synchronized
    fun remove(graphic: Graphic) {
        graphics.remove(graphic)
        postInvalidate()
    }

    @Synchronized
    fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isFlipped: Boolean) {
        require(imageWidth > 0 && imageHeight > 0) {
            Log.e(TAG, "setImageSourceInfo: ")
        }
        if (imageWidth == this.imageWidth && imageHeight == this.imageHeight && isFlipped == this.isImageFlipped) return

        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        isImageFlipped = isFlipped
        needUpdateTransformation = true
        postInvalidate()
    }

    fun getImageWidth(): Int {
        return imageWidth
    }

    fun getImageHeight(): Int {
        return imageHeight
    }

    private fun updateTransformationIfNeeded() {
        if (!needUpdateTransformation || imageWidth <= 0 || imageHeight <= 0) {
            return
        }
        val viewAspectRatio = width.toFloat() / height
        val imageAspectRatio = imageWidth.toFloat() / imageHeight
        postScaleWidthOffset = 0f
        postScaleHeightOffset = 0f
        if (viewAspectRatio > imageAspectRatio) {
            // The image needs to be vertically cropped to be displayed in this view.
            scaleFactor = width.toFloat() / imageWidth
            postScaleHeightOffset = (width.toFloat() / imageAspectRatio - height) / 2
        } else {
            // The image needs to be horizontally cropped to be displayed in this view.
            scaleFactor = height.toFloat() / imageHeight
            postScaleWidthOffset = (height.toFloat() * imageAspectRatio - width) / 2
        }
        transformationMatrix.reset()
        transformationMatrix.setScale(scaleFactor, scaleFactor)
        transformationMatrix.postTranslate(-postScaleWidthOffset, -postScaleHeightOffset)
        if (isImageFlipped) {
            transformationMatrix.postScale(-1f, 1f, width / 2f, height / 2f)
        }
        needUpdateTransformation = false
    }

    /** Draws the overlay with its associated graphic objects.  */
    @Synchronized
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        updateTransformationIfNeeded()
        for (graphic in graphics) {
            graphic.draw(canvas)
        }
    }

    companion object {
        private const val TAG = "GraphicOverlay"
    }
}