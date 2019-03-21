package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.CookeoBindingComponent
import com.rukiasoft.androidapps.cocinaconroll.databinding.IngredientItemBinding
import com.rukiasoft.androidapps.cocinaconroll.databinding.StepItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Ingredient
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step

class RecipeDetailsAdapter constructor(
    private val cookeoBindingComponent: CookeoBindingComponent
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (items.firstOrNull() is Ingredient) {
            val binding = DataBindingUtil.inflate<IngredientItemBinding>(
                inflater,
                R.layout.ingredient_item,
                parent,
                false,
                cookeoBindingComponent
            )
            IngredientViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<StepItemBinding>(
                inflater,
                R.layout.step_item,
                parent,
                false,
                cookeoBindingComponent
            )
            StepListViewHolder(binding)
        }

    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is IngredientViewHolder && item is Ingredient) {
            holder.bind(item)
        } else if (holder is StepListViewHolder && item is Step) {
            holder.bind(item)
        }
    }

    fun updateItems(items: List<Any>) {
        this.items.clear()
        if(items.firstOrNull() is Ingredient || items.firstOrNull() is Step) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

}