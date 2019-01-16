package com.rukiasoft.androidapps.cocinaconroll.extensions

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * Created by Roll on 31/8/17.
 * binding adapters to use glide on xml with data binding
 */
@Suppress("unused")
class CookeoBindingAdapters {


    @BindingAdapter("isVisibleOrGone")
    fun <T> setIsVisibleOrGone(view: View, isVisible: T?) {
        isVisible?.let {
            if (isVisible is Boolean && isVisible == true) {
                view.visibility = View.VISIBLE
                return
            } else if (isVisible is String? && isVisible.isNotBlank()) {
                view.visibility = View.VISIBLE
                return
            }
        }
        view.visibility = View.GONE
    }

    @BindingAdapter("isVisibleOrInvisible")
    fun <T> setIsVisibleOrInvisible(view: View, isVisible: T?) {
        isVisible?.let {
            if (isVisible is Boolean && isVisible == true) {
                view.visibility = View.VISIBLE
                return
            } else if (isVisible is String? && isVisible.isNotBlank()) {
                view.visibility = View.VISIBLE
                return
            }
        }
        view.visibility = View.INVISIBLE
    }

    @BindingAdapter("isBoldOrNot")
    fun <T> setIsBoldOrNot(textView: TextView, isBold: T?) {
        isBold?.let {
            if (isBold is Boolean && isBold == true) {
                textView.setTypeface(textView.typeface, Typeface.BOLD)
                return
            }
        }
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    @BindingAdapter("setBackground")
    fun <T> setBackground(view: View, background: T?) {
        background?.let {
            if (it is Int) {
                view.setBackgroundResource(it)
            }
        }
    }

    @BindingAdapter("setBackground")
    fun <T> setBackgroundOnTextView(textView: View, background: T?) {
        background?.let {
            if (it is Int) {
                textView.setBackgroundResource(it)
            }
        }
    }

    @BindingAdapter("setTextColor")
    fun <T> setTextColorOnTextView(textView: TextView, color: T?) {
        color?.let {
            if (it is Int) {
                textView.setTextColor(it)
            }
        }
    }
}