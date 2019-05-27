package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep2Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.relations.RecipeWithInfo
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ChildBaseFragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar


abstract class Step2CommonFragment : ChildBaseFragment(), EditRecipeAdapter.ShowSnackbarOnDeleteItem {

     protected lateinit var binding: FragmentStep2Binding
    protected lateinit var adapter: EditRecipeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step2, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = EditRecipeAdapter(this)
        binding.editRecipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this@apply.adapter = this@Step2CommonFragment.adapter
            val callback = SimpleItemTouchHelperCallback(this@Step2CommonFragment.adapter,null)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                adapter.submitList(getListOfItems(recipe))
            }
        })

        binding.editRecipeAddItem.setText(getItemsInEditTextBox())

        binding.editRecipeAddItemLayout.hint = getHint()
        binding.editRecipeAddFab.setOnClickListener {
            addItemToRecipe()
        }
    }


    abstract fun getListOfItems(recipeWithInfo: RecipeWithInfo): List<String>?
    abstract fun getItemsInEditTextBox(): String
    abstract fun getHint(): String

    private fun addItemToRecipe() {
        if (binding.editRecipeAddItem.text?.isNotBlank() == true) {
            val list: MutableList<String> = adapter.currentList.toMutableList()
            list.add(binding.editRecipeAddItem.text.toString())
            adapter.submitList(list)
            binding.editRecipeAddItem.text = null
            (activity as? MainActivity)?.hideKeyboard()
        }
    }

     override fun showUndoSnackbar() {
         view?.let {
             val snackbar = Snackbar.make(
                 it, resourcesManager.getString(R.string.undo_text),
                 Snackbar.LENGTH_LONG
             )
             snackbar.setAction(resourcesManager.getString(R.string.undo_action)) { adapter.undoDelete() }
             snackbar.show()
         }
     }

}
