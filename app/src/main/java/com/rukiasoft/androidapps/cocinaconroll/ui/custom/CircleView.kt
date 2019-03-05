package com.rukiasoft.androidapps.cocinaconroll.ui.custom

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Property
import android.view.View


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, March 2019
 *
 *
 */

class CircleView : View {

    private val argbEvaluator = ArgbEvaluator()

    private val circlePaint = Paint()
    private val maskPaint = Paint()

    private var tempBitmap: Bitmap? = null
    private var tempCanvas: Canvas? = null

    var outerCircleRadiusProgress = 0f
        set(outerCircleRadiusProgress) {
            field = outerCircleRadiusProgress
            updateCircleColor()
            postInvalidate()
        }
    var innerCircleRadiusProgress = 0f
        set(innerCircleRadiusProgress) {
            field = innerCircleRadiusProgress
            postInvalidate()
        }

    private var maxCircleSize: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    init {
        circlePaint.style = Paint.Style.FILL
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxCircleSize = w / 2
        tempBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(tempBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        tempCanvas!!.drawColor(0xffffff, PorterDuff.Mode.CLEAR)
        tempCanvas!!.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            this.outerCircleRadiusProgress * maxCircleSize,
            circlePaint
        )
        tempCanvas!!.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            this.innerCircleRadiusProgress * maxCircleSize,
            maskPaint
        )
        canvas.drawBitmap(tempBitmap!!, 0f, 0f, null)
    }

    private fun updateCircleColor() {
        var colorProgress: Double = CustomViewsUtils.clamp(outerCircleRadiusProgress.toDouble(), 0.5, 1.0)
        colorProgress = CustomViewsUtils.mapValueFromRangeToRange(colorProgress, 0.5, 1.0, 0.0, 1.0)
        this.circlePaint.color = argbEvaluator.evaluate(colorProgress.toFloat(), START_COLOR, END_COLOR) as Int
    }

    companion object {
        private const val START_COLOR = -0xa8de
        private const val END_COLOR = -0x3ef9

        val INNER_CIRCLE_RADIUS_PROGRESS: Property<CircleView, Float> = object :
            Property<CircleView, Float>(Float::class.java, "innerCircleRadiusProgress") {
            override fun get(element: CircleView): Float {
                return element.innerCircleRadiusProgress
            }

            override fun set(element: CircleView, value: Float?) {
                value?.let {
                    element.innerCircleRadiusProgress = value
                }
            }
        }

        val OUTER_CIRCLE_RADIUS_PROGRESS: Property<CircleView, Float> = object :
            Property<CircleView, Float>(Float::class.java, "outerCircleRadiusProgress") {
            override fun get(element: CircleView): Float {
                return element.outerCircleRadiusProgress
            }

            override fun set(element: CircleView, value: Float?) {
                value?.let {
                    element.outerCircleRadiusProgress = value
                }
            }
        }
    }
}
