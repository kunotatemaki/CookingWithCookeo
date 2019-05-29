package com.rukiasoft.androidapps.cocinaconroll.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.ViewModelKey
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeContainerViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.EditListExplanationViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.ingredientsandsteps.ingredients.Step2IngredientsViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.maindata.Step1ViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails.RecipeDetailsViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.recipelist.RecipeListViewModel
import com.rukiasoft.androidapps.cocinaconroll.ui.signin.SignInViewModel
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, septiembre 2018
 *
 *
 */

@Suppress("unused")
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel::class)
    internal abstract fun bindRecipeListViewModel(recipeListViewModel: RecipeListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel::class)
    internal abstract fun bindRecipeDetailsViewModel(recipeDetailsViewModel: RecipeDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewRecipeContainerViewModel::class)
    internal abstract fun bindNewRecipeContainerViewModel(newRecipeContainerViewModel: NewRecipeContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(Step1ViewModel::class)
    internal abstract fun bindStep1ViewModel(step1ViewModel: Step1ViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditListExplanationViewModel::class)
    internal abstract fun bindEditListExplanationViewModel(editListExplanationViewModel: EditListExplanationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(Step2IngredientsViewModel::class)
    internal abstract fun bindStep2IngredientsViewModel(step2IngredientsViewModel: Step2IngredientsViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: CocinaConRollViewModelFactory): ViewModelProvider.Factory
}