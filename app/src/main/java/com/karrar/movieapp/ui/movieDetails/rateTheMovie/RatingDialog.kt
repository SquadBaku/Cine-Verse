package com.karrar.movieapp.ui.movieDetails.rateTheMovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogCreateListBinding
import com.karrar.movieapp.ui.base.BaseDialogFragment
import com.karrar.movieapp.ui.movieDetails.MovieDetailsUIEvent
import com.karrar.movieapp.ui.movieDetails.MovieDetailsViewModel
import com.karrar.movieapp.ui.movieDetails.saveMovie.CreateListDialogDirections
import com.karrar.movieapp.ui.movieDetails.saveMovie.uiState.SaveMovieUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingDialog :  BaseDialogFragment<DialogCreateListBinding>() {

    override val layoutIdFragment = R.layout.dialog_rating
    override val viewModel: MovieDetailsViewModel by viewModels()
    private var action: NavDirections? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLast(viewModel.movieDetailsUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: MovieDetailsUIEvent) {

        if (event is MovieDetailsUIEvent.DismissSheet) {
            dismiss()
        }
        if (event is MovieDetailsUIEvent.MessageAppear) {
            val movieId = viewModel.uiState.value.movieDetailsResult.id
            action = RatingDialogDirections.actionRatingDialogToMovieDetailFragment(movieId)
            action?.let { findNavController().navigate(it) }
        }
    }


}