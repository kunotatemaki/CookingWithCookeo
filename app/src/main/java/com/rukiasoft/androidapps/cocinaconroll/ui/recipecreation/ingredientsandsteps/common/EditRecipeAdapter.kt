package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.RecipeCreationItemBinding


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, May 2019
 *
 *
 */

class EditRecipeAdapter : ListAdapter<String, EditRecipeAdapter.CreateRecipeViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RecipeCreationItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.recipe_creation_item, parent, false)

        return CreateRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreateRecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.itemText = item
    }

    class CreateRecipeViewHolder constructor(val binding: RecipeCreationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}