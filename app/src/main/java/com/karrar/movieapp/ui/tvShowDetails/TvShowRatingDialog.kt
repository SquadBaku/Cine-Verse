package com.karrar.movieapp.ui.tvShowDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogCreateListBinding
import com.karrar.movieapp.ui.base.BaseDialogFragment
import com.karrar.movieapp.ui.movieDetails.rateTheMovie.RatingDialogDirections
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowRatingDialog: BaseDialogFragment<DialogCreateListBinding>() {
    override val layoutIdFragment = R.layout.tv_show_dialog_rating
    override val viewModel: TvShowDetailsViewModel by viewModels()
    private var action: NavDirections? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLast(viewModel.tvShowDetailsUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: TvShowDetailsUIEvent) {

        if (event is TvShowDetailsUIEvent.DismissSheet) {
            dismiss()
        }
        if (event is TvShowDetailsUIEvent.MessageAppear) {
            val tvShowId = viewModel.stateFlow.value.tvShowDetailsResult.tvShowId
            action = RatingDialogDirections.actionRatingDialogToMovieDetailFragment(tvShowId)
            action?.let { findNavController().navigate(it) }
        }
    }
}