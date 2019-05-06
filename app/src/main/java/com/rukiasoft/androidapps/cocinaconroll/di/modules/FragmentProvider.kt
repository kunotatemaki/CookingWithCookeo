package com.rukiasoft.androidapps.cocinaconroll.di.modules

import com.rukiasoft.androidapps.cocinaconroll.di.interfaces.CustomScopes
import com.rukiasoft.androidapps.cocinaconroll.ui.greetings.ThanksFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.NewRecipeContainerFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.Step1Fragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.Step2Fragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation.Step3Fragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipedetails.RecipeDetailsFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.recipelist.RecipeListFragment
import com.rukiasoft.androidapps.cocinaconroll.ui.signin.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi


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
abstract class FragmentsProvider {

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesRecipeListFragmentFactory(): RecipeListFragment

    @ExperimentalCoroutinesApi
    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesRecipeDetailsFragmentFactory(): RecipeDetailsFragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesThanksFragmentFactory(): ThanksFragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesSignInFragmentFactory(): SignInFragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun provideStep1FragmentFactory(): Step1Fragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun provideStep2FragmentFactory(): Step2Fragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun provideStep3FragmentFactory(): Step3Fragment

    @ExperimentalCoroutinesApi
    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesNewRecipeContainerFragmentFactory(): NewRecipeContainerFragment

}