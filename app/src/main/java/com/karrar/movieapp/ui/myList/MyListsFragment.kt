package com.karrar.movieapp.ui.myList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMyListsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.myList.myListUIState.MyListUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyListsFragment : BaseFragment<FragmentMyListsBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_my_lists
    override val viewModel: MyListsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false, getString(R.string.myList))
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.btnGoToExplore.setOnClickListener {
            findNavController().navigate(R.id.action_myListFragment_to_exploringFragment)
        }
        binding.savedList.adapter = CreatedListAdapter(emptyList(), viewModel)
        collectEvent()
    }

    private fun collectEvent() {
        collectLast(viewModel.myListUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: MyListUIEvent) {
        var action: NavDirections? = null
        when (event) {
            MyListUIEvent.CreateButtonClicked -> {
                action = MyListsFragmentDirections.actionMyListFragmentToCreateSavedList()
            }

            is MyListUIEvent.DisplayError -> {
                Toast.makeText(requireContext(), event.errorMessage, Toast.LENGTH_LONG).show()
            }

            is MyListUIEvent.OnSelectItem -> {
                action = MyListsFragmentDirections.actionMyListFragmentToSavedListFragment(
                    event.createdListUIState.listID,
                    event.createdListUIState.name
                )
            }

            else -> {
            }
        }
        action?.let { findNavController().navigate(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}