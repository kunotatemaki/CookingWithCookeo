package com.rukiasoft.androidapps.cocinaconroll.ui.views

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManagerImpl
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */


class AppToolbar : Toolbar {

    enum class NavIcon {
        HAMBURGER, ARROW
    }

    private lateinit var resourceUtils: ResourcesManager

    private fun getResourcesManager(): ResourcesManager {
        if (::resourceUtils.isInitialized.not()) {
            resourceUtils = ResourcesManagerImpl(context)
        }
        return resourceUtils
    }

    fun setResourcesManager(resourceUtils: ResourcesManager) {
        this.resourceUtils = resourceUtils
    }

    private var view: View? = null
    private var badgeCount: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun updateBackgroundColor(color: Int, activity: Activity?) {
        setBackgroundColor(color)
        (activity as? MainActivity)?.updateStatusBar(color)
    }

    /**
     * call this function AFTER inflating the header (#updateSingleTitle, #updateMultipleTitle, #setToolbarReceipt)
     */
    private fun updateTitleColor(color: Int, vararg textList: TextView?) {
        textList.forEach {
            it?.setTextColor(color)
        }
        view?.toolbarSingleTitle?.setTextColor(color)
        view?.toolbarMultipleTitleMain?.setTextColor(color)
        view?.toolbarMultipleTitleDetail?.setTextColor(color)
        view?.receipt_title?.setTextColor(color)
        view?.receipt_username?.setTextColor(color)
        ImageViewCompat.setImageTintList(toolbarNavDrawerMenu, ColorStateList.valueOf(color))
    }

    /**
     * call this function in onCreateOptionsMenu
     */
//    override fun setMenuTextColor(menuResId: Int, colorRes: Int) {
//        val color = getResourceUtils().getColor(colorRes)
//        activity_toolbar?.post {
//            val settingsMenuItem = activity_toolbar.findViewById<View>(menuResId)
//            if (settingsMenuItem is TextView) {
//                settingsMenuItem.setTextColor(color)
//            } else {
//                val menu = activity_toolbar.menu
//                menu.findItem(menuResId)?.let { item ->
//                    val s = SpannableString(item.title)
//                    s.setSpan(ForegroundColorSpan(color), 0, s.length, 0)
//                    item.title = s
//                }
//            }
//        }
//    }
//
//    override fun setMenuVisibility(menuId: Int, visible: Boolean) {
//        val item = activity_toolbar?.menu?.findItem(R.id.clear_menu_item)
//        item?.isVisible = visible
//    }

    fun updateSingleTitle(title: Int, color: Int?) {
        updateSingleTitle(getResourcesManager().getString(title), color)
    }

    fun updateSingleTitle(title: String?, color: Int?) {
        title?.let {
            removeHeader()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.toolbar_single_title, templateHeader, true)
            view?.toolbarSingleTitle?.text = title
            val textColor = color ?: getResourceUtils().getColor(R.color.white)
            updateTitleColor(textColor, view?.toolbarSingleTitle)
            templateHeader.requestLayout()
        }
    }

//    override fun updateMultipleTitle(title: Int, detail: Int, color: Int?) {
//        updateMultipleTitle(getResourceUtils().getString(title), getResourceUtils().getString(detail), color)
//    }
//
//    override fun updateMultipleTitle(title: String?, detail: String?, color: Int?) {
//        title?.let {
//            removeHeader()
//            val inflater = LayoutInflater.from(context)
//            view = inflater.inflate(R.layout.toolbar_multiple_title_and_detail, templateHeader, true)
//            view?.toolbarMultipleTitleMain?.text = title
//            view?.toolbarMultipleTitleDetail?.text = detail
//            val textColor = color ?: getResourceUtils().getColor(R.color.white)
//            updateTitleColor(textColor, view?.toolbarMultipleTitleMain, view?.toolbarMultipleTitleDetail)
//            templateHeader.requestLayout()
//        }
//    }

    fun setNavIconAsArrow(activity: Activity?, color: Int?) {
        show()
        if ((toolbarNavDrawerMenu.tag as NavIcon?) == NavIcon.HAMBURGER) {
            val backDrawable =
                getResourceUtils().getDrawable(R.drawable.ic_back_animatable) as AnimatedVectorDrawable
            toolbarNavDrawerMenu.setImageDrawable(backDrawable)
            backDrawable.start()
        } else {
            toolbarNavDrawerMenu.setImageResource(R.drawable.ic_nav_back)
        }

        toolbarNavDrawerMenu.tag = NavIcon.ARROW
        toolbarNavDrawerMenuBadge.visibility = View.GONE
        if (activity is BaseMenuActivity) {
            activity.getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        toolbarNavDrawerMenu?.setOnClickListener {
            activity?.onBackPressed()
        }
        val toolbarBackgroundColor = color ?: getResourceUtils().getColor(R.color.colorPrimary)
        updateBackgroundColor(color = toolbarBackgroundColor, activity = activity)
    }

    fun setNavIconAsHamburger(activity: Activity?, color: Int?) {
        show()
        if ((toolbarNavDrawerMenu.tag as NavIcon?) == NavIcon.ARROW) {
            val backDrawable =
                getResourceUtils().getDrawable(R.drawable.ic_hamburger_animatable) as AnimatedVectorDrawable
            toolbarNavDrawerMenu.setImageDrawable(backDrawable)
            backDrawable.start()
        } else {
            toolbarNavDrawerMenu.setImageResource(R.drawable.ic_nav_hamburger)
        }

        toolbarNavDrawerMenu.tag = NavIcon.HAMBURGER
        if (badgeCount > 0) {
            toolbarNavDrawerMenuBadge.visibility = View.VISIBLE
        }
        if (activity is BaseMenuActivity) {
            activity.getDrawerLayout()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
        toolbarNavDrawerMenu?.setOnClickListener {
            val listener = if (activity is DrawerListener) activity else null
            listener?.onMenuClicked()
        }
        val toolbarBackgroundColor = color ?: getResourceUtils().getColor(R.color.colorPrimary)
        updateBackgroundColor(toolbarBackgroundColor, activity)
    }

    fun hide() {
        this.visibility = View.GONE
    }

    fun show() {
        this.visibility = View.VISIBLE
    }

    fun removeHeader() {
        templateHeader.removeAllViews()
    }

}
