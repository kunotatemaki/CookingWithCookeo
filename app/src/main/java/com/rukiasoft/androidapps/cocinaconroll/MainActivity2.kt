package com.rukiasoft.androidapps.cocinaconroll

import android.os.Bundle
import com.rukiasoft.androidapps.cocinaconroll.ui.mainactivity2.MainActivity2Fragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity2 : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity2_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainActivity2Fragment.newInstance())
                    .commitNow()
        }
    }

}
