package com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails

import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.databinding.StepItemBinding
import com.rukiasoft.androidapps.cocinaconroll.persistence.entities.Step

class StepListViewHolder(
    private val binding: StepItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(step: Step) {
        binding.step = step
    }

}