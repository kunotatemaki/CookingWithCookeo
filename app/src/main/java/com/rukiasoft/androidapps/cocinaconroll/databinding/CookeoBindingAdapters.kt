package com.rukiasoft.androidapps.cocinaconroll.databinding

import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.rukiasoft.androidapps.cocinaconroll.CocinaConRollApplication
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.utils.ReadWriteUtils
import java.io.File
import javax.inject.Inject


/**
 * Created by Roll on 31/8/17.
 * binding adapters to use glide on xml with data binding
 */
@Suppress("unused")
class CookeoBindingAdapters @Inject constructor(
    private val readWriteUtils: ReadWriteUtils,
    private val cocinaConRollApplication: CocinaConRollApplication
) {

    @BindingAdapter("imageRounded")
    fun setImageUrlRounded(view: ImageView, url: String?) {
        //circle images
        url?.let {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .circleCrop()
                )
                .into(view)
        }
    }

    @BindingAdapter("imageAsId")
    fun setImageFromId(view: ImageView, id: Int?) {
        //circle images
        id?.let {
            Glide.with(view.context)
                .load(id)
                .into(view)
        }
    }

    @BindingAdapter("recipeImage")
    fun setRecipeImageFromFile(view: ImageView, imageName: String?) {
        //circle images
        imageName?.also {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_dish)
            val resource = if (it == PersistenceConstants.DEFAULT_PICTURE_NAME) {
                R.drawable.default_dish
            } else {
                val fullPath = readWriteUtils.getOriginalStorageDir()
                val file = File(fullPath + imageName)
                Uri.fromFile(file)
            }

            Glide.with(view.context)
                .load(resource)
                .apply(options)
                .into(view)

        }
    }

    @BindingAdapter("recipeTarget")
    fun setRecipeTargetFromFile(imageView: ImageView, imageName: String?) {
        imageName?.also {
            val target = object : CustomViewTarget<ImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageView.setImageDrawable(errorDrawable)
                    setBitmapInObservable(errorDrawable)

                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    imageView.setImageDrawable(placeholder)
                    setBitmapInObservable(placeholder)
                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    imageView.setImageDrawable(resource)
                    setBitmapInObservable(resource)
                }
            }
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_dish)
            val resource = if (it == PersistenceConstants.DEFAULT_PICTURE_NAME) {
                R.drawable.default_dish
            } else {
                val fullPath = readWriteUtils.getOriginalStorageDir()
                val file = File(fullPath + imageName)
                Uri.fromFile(file)
            }

            Glide.with(target.view.context)
                .load(resource)
                .apply(options)
                .into(target)
        }
    }

    private fun setBitmapInObservable(drawable: Drawable?) {
        (drawable as? BitmapDrawable)?.let {
            cocinaConRollApplication.setRecipeDetailsBitmap(it.bitmap)
        }
    }

    @BindingAdapter("imageCenterCropped")
    fun setImageUrlCenterAndCropped(view: ImageView, url: String?) {
        //cropped images
        url?.let {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .centerCrop()
                )
                .into(view)
        }
    }

    @BindingAdapter("imageCenterCropped")
    fun setImageFileCenterAndCropped(view: ImageView, file: File?) {
        //cropped images
        file?.let {
            Glide.with(view.context)
                .load(file)
                .apply(
                    RequestOptions()
                        .centerCrop()
                )
                .into(view)
        }
    }

    @BindingAdapter("imageCenterCropped")
    fun setImageUriCenterAndCropped(view: ImageView, uri: Uri?) {
        //cropped images
        uri?.let {
            Glide.with(view.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .centerCrop()
                )
                .into(view)
        }
    }

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