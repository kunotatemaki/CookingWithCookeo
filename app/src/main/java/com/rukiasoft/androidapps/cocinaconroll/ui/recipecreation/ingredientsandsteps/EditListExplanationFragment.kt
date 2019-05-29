package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentEditListOverlayDialogBinding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.BottomSheetDialogBaseFragment
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection

class EditListExplanationFragment : BottomSheetDialogBaseFragment() {

    private lateinit var binding: FragmentEditListOverlayDialogBinding
    private lateinit var viewModel: EditListExplanationViewModel

    interface BottomSheetDialogHandler{
        fun dialogShown()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditListExplanationViewModel::class.java)

        binding.button.setOnClickListener {
            viewModel.saveExplanationShown()
            this.dismissAllowingStateLoss()
        }

        (targetFragment as? BottomSheetDialogHandler)?.dialogShown()
    }

    companion object {
        val TAG: String? = EditListExplanationFragment::class.java.canonicalName

        fun newInstance() = EditListExplanationFragment()

    }

}