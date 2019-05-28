package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentEditListOverlayDialogBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeContainerViewModel
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class EditListExplanationFragment : BottomSheetDialogFragment() /*, HasSupportFragmentInjector*/ {

//    @Inject
//    var childFragmentInjector: DispatchingAndroidInjector<Fragment>? = null
    @Inject
    protected lateinit var viewModelFactory: CocinaConRollViewModelFactory

    private lateinit var binding: FragmentEditListOverlayDialogBinding
    private lateinit var viewModel: EditListExplanationViewModel

    override fun onAttach(context: Context) {
//        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

//    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
//        return childFragmentInjector
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_list_overlay_dialog, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditListExplanationViewModel::class.java)
//        adaptModalToFullScreen(binding.root)

//        fav_overlay_okay_button.setOnClickListener {
//            this.dismissAllowingStateLoss()
//        }

//        viewModel.saveExplanationShown()

    }

    protected open fun adaptModalToFullScreen(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as? BottomSheetDialog
            val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0

//            (activity as? MainActivity)?.getScreenHeight()?.let {
//                val params = fav_overlay_container?.layoutParams
//                params?.height = it
//                fav_overlay_container?.layoutParams = params
//            }
        }


    }

    companion object {
        val TAG: String? = EditListExplanationFragment::class.java.canonicalName

        fun newInstance() = EditListExplanationFragment()

    }

}