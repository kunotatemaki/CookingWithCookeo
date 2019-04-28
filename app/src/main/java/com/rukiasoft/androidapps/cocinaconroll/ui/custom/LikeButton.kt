package com.rukiasoft.androidapps.cocinaconroll.ui.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.ViewLikeButtonBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Recipe
import com.rukiasoft.androidapps.cocinaconroll.utils.AppExecutors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

class LikeButtonView : FrameLayout {


    private lateinit var binding: ViewLikeButtonBinding
    private lateinit var recipeKey: String
    private var favouriteFlag: Boolean = false
    private var animatorSet: AnimatorSet? = null
    private lateinit var favoriteIcon: ImageView
    private lateinit var persistenceManager: PersistenceManager
    private lateinit var appExecutors: AppExecutors

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    @SuppressLint("ClickableViewAccessibility")
    fun initialize(
        recipe: Recipe,
        favorite: ImageView,
        persistenceManager: PersistenceManager,
        appExecutors: AppExecutors,
        enableClick: Boolean
    ) {
        val inflater = LayoutInflater.from(context)

        this.removeAllViews()

        binding = DataBindingUtil.inflate(inflater, R.layout.view_like_button, this, true)
        recipeKey = recipe.recipeKey
        favouriteFlag = recipe.favourite
        favoriteIcon = favorite
        this.persistenceManager = persistenceManager
        this.appExecutors = appExecutors
        binding.ivStar.setImageResource(if (favouriteFlag) R.drawable.ic_favorite_white_36dp else R.drawable.ic_favorite_outline_white_36dp)
        setClick(enableClick)
        binding.ivStar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.ivStar.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).interpolator =
                        DECELERATE_INTERPOLATOR
                    isPressed = true
                }

                MotionEvent.ACTION_MOVE -> {
                    val x = event.x
                    val y = event.y
                    val isInside = x > 0 && x < width && y > 0 && y < height
                    if (isPressed != isInside) {
                        isPressed = isInside
                    }
                }

                MotionEvent.ACTION_UP -> {
                    binding.ivStar.animate().scaleX(1f).scaleY(1f).interpolator = DECELERATE_INTERPOLATOR
                    if (isPressed) {
                        v.performClick()
                        isPressed = false
                    }
                }
            }
            true
        }


    }

    fun setClick(enableClick: Boolean){
        if(enableClick){
            binding.ivStar.setOnClickListener {
                favouriteFlag = favouriteFlag.not()
                performClickInFavourite()
            }
        }else
            binding.ivStar.setOnClickListener (null)
    }

    @MainThread
    private fun performClickInFavourite() {
        favoriteIcon.visibility = if (favouriteFlag) View.VISIBLE else View.GONE
        val resourceId = if (favouriteFlag) {
            R.drawable.ic_favorite_white_36dp
        } else {
            R.drawable.ic_favorite_outline_white_36dp
        }
        binding.ivStar.setImageResource(resourceId)

        animatorSet?.cancel()

        if (favouriteFlag) {
            binding.ivStar.apply {
                animate().cancel()
                scaleX = 0f
                scaleY = 0f
            }
            binding.vCircle.apply {
                innerCircleRadiusProgress = 0f
                outerCircleRadiusProgress = 0f
            }
            binding.vDotsView.currentProgress = 0f

            animatorSet = AnimatorSet()

            val outerCircleAnimator =
                ObjectAnimator.ofFloat(binding.vCircle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f)
            outerCircleAnimator.duration = 250
            outerCircleAnimator.interpolator = DECELERATE_INTERPOLATOR

            val innerCircleAnimator =
                ObjectAnimator.ofFloat(binding.vCircle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f)
            innerCircleAnimator.duration = 200
            innerCircleAnimator.startDelay = 200
            innerCircleAnimator.interpolator = DECELERATE_INTERPOLATOR

            val starScaleYAnimator = ObjectAnimator.ofFloat(binding.ivStar, ImageView.SCALE_Y, 0.2f, 1f)
            starScaleYAnimator.duration = 350
            starScaleYAnimator.startDelay = 250
            starScaleYAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val starScaleXAnimator = ObjectAnimator.ofFloat(binding.ivStar, ImageView.SCALE_X, 0.2f, 1f)
            starScaleXAnimator.duration = 350
            starScaleXAnimator.startDelay = 250
            starScaleXAnimator.interpolator = OVERSHOOT_INTERPOLATOR

            val dotsAnimator = ObjectAnimator.ofFloat(binding.vDotsView, DotsView.DOTS_PROGRESS, 0f, 1f)
            dotsAnimator.duration = 900
            dotsAnimator.startDelay = 50
            dotsAnimator.interpolator = ACCELERATE_DECELERATE_INTERPOLATOR

            animatorSet?.playTogether(
                outerCircleAnimator,
                innerCircleAnimator,
                starScaleYAnimator,
                starScaleXAnimator,
                dotsAnimator
            )

            animatorSet?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    binding.vCircle.innerCircleRadiusProgress = 0f
                    binding.vCircle.outerCircleRadiusProgress = 0f
                    binding.vDotsView.currentProgress = 0f
                    binding.ivStar.scaleX = 1f
                    binding.ivStar.scaleY = 1f
                }

                override fun onAnimationEnd(animation: Animator?) {
                    persistData()
                }
            })

            animatorSet?.start()
        } else {
            persistData()
        }
    }

    private fun persistData() {
        GlobalScope.launch {
            persistenceManager.setFavourite(recipeKey, favouriteFlag)
        }
    }

    companion object {
        private val DECELERATE_INTERPOLATOR = DecelerateInterpolator()
        private val ACCELERATE_DECELERATE_INTERPOLATOR = AccelerateDecelerateInterpolator()
        private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)
    }


}
