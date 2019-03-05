package com.rukiasoft.androidapps.cocinaconroll.ui.custom

import android.animation.ArgbEvaluator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
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

class DotsView : View {

    private val circlePaints = arrayOfNulls<Paint>(4)

    private var centerX: Int = 0
    private var centerY: Int = 0

    private var maxOuterDotsRadius: Float = 0.toFloat()
    private var maxInnerDotsRadius: Float = 0.toFloat()
    private var maxDotSize: Float = 0.toFloat()

    var currentProgress = 0f
        set(currentProgress) {
            field = currentProgress

            updateInnerDotsPosition()
            updateOuterDotsPosition()
            updateDotsPaints()
            updateDotsAlpha()

            postInvalidate()
        }

    private var currentRadius1 = 0f
    private var currentDotSize1 = 0f

    private var currentDotSize2 = 0f
    private var currentRadius2 = 0f

    private val argbEvaluator = ArgbEvaluator()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        for (i in circlePaints.indices) {
            circlePaints[i] = Paint()
            circlePaints[i]?.style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        maxDotSize = 10f
        maxOuterDotsRadius = w / 2 - maxDotSize * 2
        maxInnerDotsRadius = 0.8f * maxOuterDotsRadius
    }

    override fun onDraw(canvas: Canvas) {
        drawOuterDotsFrame(canvas)
        drawInnerDotsFrame(canvas)
    }

    private fun drawOuterDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX =
                (centerX + currentRadius1 * Math.cos(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            val cY =
                (centerY + currentRadius1 * Math.sin(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            circlePaints[i % circlePaints.size]?.let {
                canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize1, it)
            }
        }
    }

    private fun drawInnerDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX = (centerX + currentRadius2 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            val cY = (centerY + currentRadius2 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            circlePaints[(i + 1) % circlePaints.size]?.let {
                canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize2,it)
            }
        }
    }

    private fun updateInnerDotsPosition() {
        if (this.currentProgress < 0.3f) {
            this.currentRadius2 = CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.0, 0.3, 0.0,
                maxInnerDotsRadius.toDouble()
            ).toFloat()
        } else {
            this.currentRadius2 = maxInnerDotsRadius
        }

        when {
            this.currentProgress < 0.2 -> this.currentDotSize2 = maxDotSize
            this.currentProgress < 0.5 -> this.currentDotSize2 =
                CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.2, 0.0, maxDotSize.toDouble(), 0.3 * maxDotSize).toFloat()
            else -> this.currentDotSize2 = CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.5, 1.0, maxDotSize * 0.3, 0.0).toFloat()
        }

    }

    private fun updateOuterDotsPosition() {
        if (this.currentProgress < 0.3f) {
            this.currentRadius1 =
                CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.0, 0.3, 0.0, maxOuterDotsRadius.toDouble() * 0.8f).toFloat()
        } else {
            this.currentRadius1 =
                CustomViewsUtils.mapValueFromRangeToRange(
                    this.currentProgress.toDouble(),
                    0.3,
                    1.0,
                    0.8 * maxOuterDotsRadius,
                    maxOuterDotsRadius.toDouble()
                ).toFloat()
        }

        if (this.currentProgress < 0.7) {
            this.currentDotSize1 = maxDotSize
        } else {
            this.currentDotSize1 = CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.7, 1.0, maxDotSize.toDouble(), 0.0).toFloat()
        }
    }

    private fun updateDotsPaints() {
        if (this.currentProgress < 0.5f) {
            val progress = CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.0, 0.5, 0.0, 1.0)
            circlePaints[0]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_1, COLOR_2) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_2, COLOR_3) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_3, COLOR_4) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_4, COLOR_1) as Int
        } else {
            val progress = CustomViewsUtils.mapValueFromRangeToRange(this.currentProgress.toDouble(), 0.5, 1.0, 0.0, 1.0)
            circlePaints[0]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_2, COLOR_3) as Int
            circlePaints[1]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_3, COLOR_4) as Int
            circlePaints[2]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_4, COLOR_1) as Int
            circlePaints[3]?.color = argbEvaluator.evaluate(progress.toFloat(), COLOR_1, COLOR_2) as Int
        }
    }

    private fun updateDotsAlpha() {
        val progress = CustomViewsUtils.clamp(this.currentProgress.toDouble(), 0.6, 1.0)
        val alpha = CustomViewsUtils.mapValueFromRangeToRange(progress, 0.6, 1.0, 255.0, 0.0).toInt()
        circlePaints[0]?.alpha = alpha
        circlePaints[1]?.alpha = alpha
        circlePaints[2]?.alpha = alpha
        circlePaints[3]?.alpha = alpha
    }

    companion object {
        private const val DOTS_COUNT = 7
        private const val OUTER_DOTS_POSITION_ANGLE = 51

        private const val COLOR_1 = -0x3ef9
        private const val COLOR_2 = -0x6800
        private const val COLOR_3 = -0xa8de
        private const val COLOR_4 = -0xbbcca

        val DOTS_PROGRESS: Property<DotsView, Float> =
            object : Property<DotsView, Float>(Float::class.java, "dotsProgress") {
                override fun get(`object`: DotsView): Float {
                    return `object`.currentProgress
                }

                override fun set(`object`: DotsView, value: Float?) {
                    value?.let {
                        `object`.currentProgress = value
                    }
                }
            }
    }
}
