package com.rukiasoft.androidapps.cocinaconroll.ui.animation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.AnimationActivityBinding
import dagger.android.DaggerActivity


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, septiembre 2018
 *
 *
 */

class AnimationActivity : DaggerActivity() {

    private lateinit var circle1Anim: Animation
    private lateinit var circle2Anim: Animation
    private lateinit var circle3Anim: Animation
    private lateinit var circle4Anim: Animation
    private lateinit var circle5Anim: Animation
    private lateinit var circle6Anim: Animation
    private lateinit var rukiaAnim: Animation
    private lateinit var softAnim: Animation

    private lateinit var binding: AnimationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_animation)
        //configure the screen
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        circle1Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_1).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.circle1.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        circle2Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_2).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.circle2.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        circle3Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_3).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.circle3.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        circle4Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_4).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.circle4.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {

                }
            })
        }
        circle5Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_5).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.circle5.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        circle6Anim = AnimationUtils.loadAnimation(this, R.anim.anim_circle_6).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.layoutCircles.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        rukiaAnim = AnimationUtils.loadAnimation(this, R.anim.anim_rukia).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.textRukia.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        softAnim = AnimationUtils.loadAnimation(this, R.anim.anim_soft).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.textSoft.visibility = View.INVISIBLE
                    Handler().postDelayed({ finish() }, 200)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        super.onResume()
        binding.circle1.startAnimation(circle1Anim)
        binding.circle2.startAnimation(circle2Anim)
        binding.circle3.startAnimation(circle3Anim)
        binding.circle4.startAnimation(circle4Anim)
        binding.circle5.startAnimation(circle5Anim)
        binding.circle6.startAnimation(circle6Anim)
        binding.textRukia.startAnimation(rukiaAnim)
        binding.textSoft.startAnimation(softAnim)
    }


}