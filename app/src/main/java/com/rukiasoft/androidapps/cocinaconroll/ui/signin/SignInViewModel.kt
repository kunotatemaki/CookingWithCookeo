package com.rukiasoft.androidapps.cocinaconroll.ui.signin

import androidx.lifecycle.ViewModel
import com.rukiasoft.androidapps.cocinaconroll.persistence.PersistenceManager
import javax.inject.Inject


/**
 * Copyright (C) Rukiasoft - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Roll <raulfeliz@gmail.com>, January 2019
 *
 *
 */

class SignInViewModel @Inject constructor(
    private val persistenceManager: PersistenceManager
) : ViewModel()