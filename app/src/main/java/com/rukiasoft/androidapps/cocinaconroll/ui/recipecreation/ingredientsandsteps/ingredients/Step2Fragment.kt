package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep2Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ChildBaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent


class Step2Fragment : ChildBaseFragment() {

    private lateinit var viewModel: Step2ViewModel
    private lateinit var binding: FragmentStep2Binding

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.SECOND


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step2, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(Step2ViewModel::class.java)

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                //todo leer la receta y cargar los ingredientes
            }
        })
    }

    override fun validateFields(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
