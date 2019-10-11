package com.rukiasoft.androidapps.cocinaconroll.ui.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BottomSheetDialogBaseFragment : BottomSheetDialogFragment(),
    HasSupportFragmentInjector {

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: CocinaConRollViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    @Suppress("unused")
    protected fun adaptModalToFullScreen(baseView: View, screenHeight: Int, container: ViewGroup) {
        baseView.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as? BottomSheetDialog
            dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)?.let { bottomSheet ->
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0

                val params = container.layoutParams
                params?.height = screenHeight
                container.layoutParams = params
            }

        }


    }

}