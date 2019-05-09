package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep1Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import javax.inject.Inject


class Step1Fragment : ChildBaseFragment() {

    private lateinit var binding: FragmentStep1Binding

    @Inject
    lateinit var appContext: Context

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.FIRST


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step1, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        listener.setStep1(
            name = binding.createRecipeNameEditText.text.toString(),
            minutes = binding.editRecipeMinutes.text.toString(),
            portions = binding.editRecipePortions.text.toString(),
            vegetarian = binding.checkboxVegetarian.isChecked,
            type = getTypeFromSpinner()
        )
    }

    private fun getTypeFromSpinner(): String {
        val position = binding.spinnerTypeDish.selectedItemPosition
        val type: String
        type = when (position) {
            0 -> PersistenceConstants.TYPE_STARTERS
            1 -> PersistenceConstants.TYPE_MAIN
            else -> PersistenceConstants.TYPE_DESSERTS
        }
        return type

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list = listOf(
            resourcesManager.getString(R.string.starters),
            resourcesManager.getString(R.string.main_courses),
            resourcesManager.getString(R.string.desserts)
        )

        val dataAdapter = ArrayAdapter(appContext, android.R.layout.simple_spinner_item, list)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerTypeDish.adapter = dataAdapter

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                binding.recipe = recipe.recipe
                binding.minutes = if (recipe.recipe.minutes > 0) recipe.recipe.minutes.toString() else ""
                binding.portions = if (recipe.recipe.portions > 0) recipe.recipe.portions.toString() else ""
                val type = when (recipe.recipe.type) {
                    PersistenceConstants.TYPE_STARTERS -> resources.getString(R.string.starters)
                    PersistenceConstants.TYPE_DESSERTS -> resources.getString(R.string.desserts)
                    else -> resources.getString(R.string.main_courses)
                }
                binding.spinnerTypeDish.setSelection(dataAdapter.getPosition(type))
            }
        })
    }

    //todo new layout landscape

}
