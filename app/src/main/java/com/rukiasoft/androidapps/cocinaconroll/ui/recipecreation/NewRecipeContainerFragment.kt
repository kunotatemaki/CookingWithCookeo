package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rukiasoft.androidapps.cocinaconroll.NavGraphDirections

import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import kotlinx.android.synthetic.main.new_recipe_container_fragment.*

class NewRecipeContainerFragment : Fragment() {

    companion object {
        fun newInstance() = NewRecipeContainerFragment()
    }

    private lateinit var viewModel: NewRecipeContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_recipe_container_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.apply {
            setToolbar(
                toolbar_new_recipe_ragment,
                false,
                null
            )
        }
        viewModel = ViewModelProviders.of(this).get(NewRecipeContainerViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_recipe, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_next -> {
                activity?.let {
                    when (Navigation.findNavController(
                        activity!!,
                        R.id.fragment_new_recipe_container
                    ).currentDestination?.id) {
                        R.id.step1Fragment -> Navigation.findNavController(
                            it,
                            R.id.fragment_new_recipe_container
                        ).navigate(Step1FragmentDirections.actionStep1FragmentToStep2Fragment())
                        R.id.step2Fragment -> Navigation.findNavController(
                            it,
                            R.id.fragment_new_recipe_container
                        ).navigate(Step2FragmentDirections.actionStep2FragmentToStep3Fragment())
                        R.id.step3Fragment -> findNavController().navigate(NavGraphDirections.actionGlobalRecipeListFragment())
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
