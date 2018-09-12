package com.rukiasoft.androidapps.cocinaconroll

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rukiasoft.androidapps.cocinaconroll.viewmodel.CocinaConRollViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import javax.inject.Inject


class BlankFragment : DaggerFragment() {

    companion object {
        fun newInstance() = BlankFragment()
    }

    @Inject
    lateinit var myContext: Context

    @Inject
    lateinit var viewModelFactory: CocinaConRollViewModelFactory

    private lateinit var viewModel: BlankViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BlankViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val gView = inflater.inflate(R.layout.app_bar_main, container, false)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        gView.fab.setOnClickListener { _ ->
            findNavController().navigate(R.id.action_blankFragment6_to_mainActivity2Fragment)
        }


        return gView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        viewModel.test()
    }

}
