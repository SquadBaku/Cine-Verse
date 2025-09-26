package com.karrar.movieapp.ui.movieDetails.saveMovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogCreateListBinding
import com.karrar.movieapp.ui.base.BaseDialogFragment
import com.karrar.movieapp.ui.movieDetails.saveMovie.uiState.SaveMovieUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateListDialog : BaseDialogFragment<DialogCreateListBinding>() {

    override val layoutIdFragment = R.layout.dialog_create_list
    override val viewModel: SaveMovieViewModel by viewModels()
    private var action: NavDirections? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLast(viewModel.saveMovieUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: SaveMovieUIEvent) {

        if (event is SaveMovieUIEvent.DismissSheet) {
            dismiss()
        }
        if (event is SaveMovieUIEvent.NavigateToListsScreen) {
            action = CreateListDialogDirections.actionHomeFragmentToSavedListFragment()
            action?.let { findNavController().navigate(it) }
        }
    }
}