package com.rukiasoft.androidapps.cocinaconroll.ui.settings


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.rukiasoft.androidapps.cocinaconroll.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.options, rootKey)
    }


}
