package com.rukiasoft.androidapps.cocinaconroll.ui.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.LoadingViewBinding


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


class LoadingView(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    private val finalDelay = 700L
    private val durationMovement = 500L
    private val durationDelayExpanded = 600L
    private var distanceBetweenLogos: Float = 0f
    private val durationScale = 200L
    private val dotAnimation = 200L


    private var textColor: Int = 0
    private var textAlpha: Int = 255

    private var cancelAllAnimations = false

    private val animatorSetStart: AnimatorSet
    private lateinit var animatorSetEnd: AnimatorSet
    private val animateFirstDotShown: ValueAnimator
    private val animateSecondDotShown: ValueAnimator
    private val animateThirdDotShown: ValueAnimator
    private val animateFirstDotHide: ValueAnimator
    private val animateSecondDotHide: ValueAnimator
    private val animateThirdDotHide: ValueAnimator

    private val binding: LoadingViewBinding

    init {
        val displayMetrics = resources.displayMetrics
        val marginInPixels = 64 * displayMetrics.density
        val percentage = 0.5
        //image size is calculated this way: 7*imageSize + 6*(spacesBetweenImages) + 2*Margins = screenWidth
        //spaceBetweenImages = imageSize * percentage
        val imageSize = (displayMetrics.widthPixels - marginInPixels) / (7 + 6 * percentage)
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.loading_view, this, true)
        val params1 = binding.logoDog1.layoutParams
        val params2 = binding.logoDog2.layoutParams
        val params3 = binding.logoDog3.layoutParams
        val paramsCentral = binding.logoDogCentral.layoutParams
        val params4 = binding.logoDog4.layoutParams
        val params5 = binding.logoDog5.layoutParams
        val params6 = binding.logoDog6.layoutParams
        distanceBetweenLogos = (imageSize * (1 + percentage)).toFloat()
        params1.height = imageSize.toInt()
        params1.width = imageSize.toInt()
        params2.height = imageSize.toInt()
        params2.width = imageSize.toInt()
        params3.height = imageSize.toInt()
        params3.width = imageSize.toInt()
        paramsCentral.height = imageSize.toInt()
        paramsCentral.width = imageSize.toInt()
        params4.height = imageSize.toInt()
        params4.width = imageSize.toInt()
        params5.height = imageSize.toInt()
        params5.width = imageSize.toInt()
        params6.height = imageSize.toInt()
        params6.width = imageSize.toInt()
        binding.logoDog1.requestLayout()
        binding.logoDog2.requestLayout()
        binding.logoDog3.requestLayout()
        binding.logoDogCentral.requestLayout()
        binding.logoDog4.requestLayout()
        binding.logoDog5.requestLayout()
        binding.logoDog6.requestLayout()


        setAlphaInDot(0, 1)
        setAlphaInDot(0, 2)
        setAlphaInDot(0, 3)

        textColor = binding.loadingText.currentTextColor
        textAlpha = Color.alpha(textColor)


        //region OBJECT ANIMATORS
        //movement
        val animationExpandLogoDog1 = getExpandAnimator(binding.logoDog1, -3)
        val animationCollapseLogoDog1 = getCollapseAnimator(binding.logoDog1)
        val animationExpandLogoDog2 = getExpandAnimator(binding.logoDog2, -2)
        val animationCollapseLogoDog2 = getCollapseAnimator(binding.logoDog2)
        val animationExpandLogoDog3 = getExpandAnimator(binding.logoDog3, -1)
        val animationCollapseLogoDog3 = getCollapseAnimator(binding.logoDog3)
        val animationExpandLogoDog4 = getExpandAnimator(binding.logoDog4, 1)
        val animationCollapseLogoDog4 = getCollapseAnimator(binding.logoDog4)
        val animationExpandLogoDog5 = getExpandAnimator(binding.logoDog5, 2)
        val animationCollapseLogoDog5 = getCollapseAnimator(binding.logoDog5)
        val animationExpandLogoDog6 = getExpandAnimator(binding.logoDog6, 3)
        val animationCollapseLogoDog6 = getCollapseAnimator(binding.logoDog6)

        //alpha
        val animationAlphaRevealLogoDog1 = getAlphaRevealAnimator(binding.logoDog1)
        val animationAlphaRevealLogoDog2 = getAlphaRevealAnimator(binding.logoDog2)
        val animationAlphaRevealLogoDog3 = getAlphaRevealAnimator(binding.logoDog3)
        val animationAlphaRevealLogoDog4 = getAlphaRevealAnimator(binding.logoDog4)
        val animationAlphaRevealLogoDog5 = getAlphaRevealAnimator(binding.logoDog5)
        val animationAlphaRevealLogoDog6 = getAlphaRevealAnimator(binding.logoDog6)
        val animationAlphaHideLogoDog1 = getAlphaHideAnimator(binding.logoDog1)
        val animationAlphaHideLogoDog2 = getAlphaHideAnimator(binding.logoDog2)
        val animationAlphaHideLogoDog3 = getAlphaHideAnimator(binding.logoDog3)
        val animationAlphaHideLogoDog4 = getAlphaHideAnimator(binding.logoDog4)
        val animationAlphaHideLogoDog5 = getAlphaHideAnimator(binding.logoDog5)
        val animationAlphaHideLogoDog6 = getAlphaHideAnimator(binding.logoDog6)

        //scale
        val animationScaleXInStart = getScaleXInAnimator(binding.logoDogCentral)
        val animationScaleYInStart = getScaleYInAnimator(binding.logoDogCentral)
        val animationScaleXInEnd = getScaleXInAnimator(binding.logoDogCentral)
        val animationScaleYInEnd = getScaleYInAnimator(binding.logoDogCentral)
        val animationScaleXOutStart = getScaleXOutAnimator(binding.logoDogCentral)
        val animationScaleYOutStart = getScaleYOutAnimator(binding.logoDogCentral)
        val animationScaleXOutEnd = getScaleXOutAnimator(binding.logoDogCentral)
        val animationScaleYOutEnd = getScaleYOutAnimator(binding.logoDogCentral)

        //endregion

        //region ANIMATOR SET
        val animatorSetExpand = AnimatorSet().apply {
            play(animationScaleXInStart)
            play(animationScaleYInStart).with(animationScaleXInStart)
            play(animationExpandLogoDog1).after(animationScaleXInStart)
            play(animationScaleXOutStart).with(animationExpandLogoDog1)
            play(animationScaleYOutStart).with(animationExpandLogoDog1)
            play(animationExpandLogoDog2).with(animationExpandLogoDog1)
            play(animationExpandLogoDog3).with(animationExpandLogoDog1)
            play(animationExpandLogoDog4).with(animationExpandLogoDog1)
            play(animationExpandLogoDog5).with(animationExpandLogoDog1)
            play(animationExpandLogoDog6).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog1).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog2).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog3).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog4).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog5).with(animationExpandLogoDog1)
            play(animationAlphaRevealLogoDog6).with(animationExpandLogoDog1)
        }

        val animatorSetCollapse = AnimatorSet().apply {
            play(animationCollapseLogoDog1).after(durationDelayExpanded)
            play(animationCollapseLogoDog2).with(animationCollapseLogoDog1)
            play(animationCollapseLogoDog3).with(animationCollapseLogoDog1)
            play(animationCollapseLogoDog4).with(animationCollapseLogoDog1)
            play(animationCollapseLogoDog5).with(animationCollapseLogoDog1)
            play(animationCollapseLogoDog6).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog1).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog2).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog3).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog4).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog5).with(animationCollapseLogoDog1)
            play(animationAlphaHideLogoDog6).with(animationCollapseLogoDog1)
            play(animationScaleXInEnd).after(durationDelayExpanded + durationMovement - durationScale)
            play(animationScaleYInEnd).with(animationScaleXInEnd)
            play(animationScaleXOutEnd).after(animationScaleXInEnd)
            play(animationScaleYOutEnd).with(animationScaleXOutEnd)
        }
        animatorSetStart = AnimatorSet().apply {
            play(animatorSetExpand)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    if (cancelAllAnimations) return
                    animatorSetEnd.start()
                }
            })

        }
        animatorSetEnd = AnimatorSet().apply {
            play(animatorSetCollapse)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    if (cancelAllAnimations) return
                    if (cancelAllAnimations) return
                    Handler().postDelayed({
                        animatorSetStart.start()
                    }, finalDelay)
                }
            })
        }

        //endregion

        //region ANIMATION DOTS
        animateFirstDotShown = getDotAnimator(1, true)
        animateSecondDotShown = getDotAnimator(2, true)
        animateThirdDotShown = getDotAnimator(3, true)
        animateFirstDotHide = getDotAnimator(1, false)
        animateSecondDotHide = getDotAnimator(2, false)
        animateThirdDotHide = getDotAnimator(3, false)

        //endregion

    }

    private fun getDotAnimator(position: Int, show: Boolean): ValueAnimator {

        val startAlpha = if (show) 0 else textAlpha
        val endAlpha = if (show) textAlpha else 0
        return ValueAnimator.ofInt(startAlpha, endAlpha).apply {
            duration = dotAnimation
            addUpdateListener {
                setAlphaInDot(it.animatedValue as Int, position)
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    if (cancelAllAnimations) return
                    when (position) {
                        1 -> if (show) animateSecondDotShown.start() else animateFirstDotShown.start()
                        2 -> if (show) animateThirdDotShown.start() else animateFirstDotHide.start()
                        else -> if (show) animateThirdDotHide.start() else animateSecondDotHide.start()
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
        }
    }

    private fun getExpandAnimator(view: View, multiplier: Int): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "translationX", multiplier * distanceBetweenLogos).apply {
            duration = durationMovement
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun getCollapseAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "translationX", 0f).apply {
            duration = durationMovement
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun getAlphaRevealAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 2 * durationMovement / 3

        }
    }

    private fun getAlphaHideAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
            duration = 2 * durationMovement / 3

        }
    }

    private fun getScaleXInAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleX", 1.2f).apply {
            duration = durationScale
        }
    }

    private fun getScaleYInAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleY", 1.2f).apply {
            duration = durationScale
        }
    }

    private fun getScaleXOutAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleX", 1.0f).apply {
            duration = durationScale
        }
    }

    private fun getScaleYOutAnimator(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "scaleY", 1.0f).apply {
            duration = durationScale
        }
    }

    private fun setAlphaInDot(alpha: Int, position: Int) {
        if (binding.loadingText.text.isNullOrBlank()) return
        val s = binding.loadingText.text as Spannable
        val start: Int
        val end: Int
        when (position) {
            1 -> {
                start = s.length - 3
                end = s.length - 2
            }
            2 -> {
                start = s.length - 2
                end = s.length - 1
            }
            else -> {
                start = s.length - 1
                end = s.length
            }
        }

        val colorWithAlpha = Color.argb(alpha, Color.red(textColor), Color.green(textColor), Color.blue(textColor))
        s.setSpan(ForegroundColorSpan(colorWithAlpha), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    //region USER FUNCTIONS
    @Synchronized
    fun show() {
        this.visibility = View.VISIBLE
        animatorSetStart.start()
        animateFirstDotShown.start()
    }

    @Synchronized
    fun updateTitle(id: Int) {
        val text = this.context.resources.getString(id)
        updateTitle(text)
    }

    @Synchronized
    fun updateTitle(text: String) {
        if (text.isBlank()) return
        val finalText = if (text.endsWith("...")) text else "$text..."
        binding.loadingText.setText(finalText, TextView.BufferType.SPANNABLE)
    }

    @Synchronized
    fun dismiss() {
        this.visibility = View.GONE
        cancelAllAnimations = true
        animatorSetStart.cancel()
        animatorSetEnd.cancel()
        animateFirstDotShown.cancel()
        animateSecondDotShown.cancel()
        animateThirdDotShown.cancel()
        animateFirstDotHide.cancel()
        animateSecondDotHide.cancel()
        animateThirdDotHide.cancel()
        binding.logoDog1.x = binding.logoDogCentral.x
        binding.logoDog1.y = binding.logoDogCentral.y
        binding.logoDog2.x = binding.logoDogCentral.x
        binding.logoDog2.y = binding.logoDogCentral.y
        binding.logoDog3.x = binding.logoDogCentral.x
        binding.logoDog3.y = binding.logoDogCentral.y
        binding.logoDog4.x = binding.logoDogCentral.x
        binding.logoDog4.y = binding.logoDogCentral.y
        binding.logoDog5.x = binding.logoDogCentral.x
        binding.logoDog5.y = binding.logoDogCentral.y
        binding.logoDog6.x = binding.logoDogCentral.x
        binding.logoDog6.y = binding.logoDogCentral.y
        binding.logoDog1.alpha = 0f
        binding.logoDog2.alpha = 0f
        binding.logoDog3.alpha = 0f
        binding.logoDog4.alpha = 0f
        binding.logoDog5.alpha = 0f
        binding.logoDog6.alpha = 0f
        binding.logoDogCentral.scaleX = 1f
        binding.logoDogCentral.scaleY = 1f
        setAlphaInDot(0, 1)
        setAlphaInDot(0, 2)
        setAlphaInDot(0, 3)
    }

    // endregion
}
