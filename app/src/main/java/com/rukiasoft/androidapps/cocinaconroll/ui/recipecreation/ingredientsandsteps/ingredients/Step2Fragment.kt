package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep2Binding
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ChildBaseFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeParent
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common.EditRecipeAdapter


class Step2Fragment : ChildBaseFragment() {

    private lateinit var viewModel: Step2ViewModel
    private lateinit var binding: FragmentStep2Binding
    private lateinit var adapter: EditRecipeAdapter

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.SECOND


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step2, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        listener.setIngredients(adapter.currentList)
        listener.saveIngredientInBox(binding.editRecipeAddItem.text.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(Step2ViewModel::class.java)

        binding.editRecipeItemsTitle.text = resourcesManager.getString(R.string.ingredients)

        adapter = EditRecipeAdapter()
        binding.editRecipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this@apply.adapter = this@Step2Fragment.adapter
        }

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                adapter.submitList(recipe.ingredients?.map { ingredientWrapper -> ingredientWrapper.ingredient })
            }
        })

        binding.editRecipeAddItem.setText(listener.getIngredientInBox())

        binding.editRecipeAddFab.setOnClickListener {
            addItemToRecipe()
        }
    }

    private fun addItemToRecipe(){
        if(binding.editRecipeAddItem.text?.isNotBlank() == true){
            val list: MutableList<String> = adapter.currentList.toMutableList()
            list.add(binding.editRecipeAddItem.text.toString())
            adapter.submitList(list)
            binding.editRecipeAddItem.text = null
            (activity as? MainActivity)?.hideKeyboard()
        }
    }

    override fun validateFields(): Boolean {
        return true
    }

}
