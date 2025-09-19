package com.karrar.movieapp.ui.movieDetails.saveMovie

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogSaveMovieBinding
import com.karrar.movieapp.ui.base.BaseDialogFragment
import com.karrar.movieapp.ui.movieDetails.saveMovie.uiState.SaveMovieUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SaveMovieDialog : BaseDialogFragment<DialogSaveMovieBinding>() {

    override val layoutIdFragment = R.layout.dialog_save_movie
    override val viewModel: SaveMovieViewModel by viewModels()
    var action: NavDirections? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveListAdapter.adapter = SaveListAdapter(mutableListOf(), viewModel)
        collectLast(viewModel.saveMovieUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }

        }
    }

    private fun onEvent(event: SaveMovieUIEvent) {
        if (event is SaveMovieUIEvent.DisplayMessage) {
            if (!event.message.isNullOrBlank()) {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        if (event is SaveMovieUIEvent.CreateNewList) {
            Log.d("TAG event", "onEvent: clicked new collection")
            action = SaveMovieDialogDirections.actionCreateNewList(500)
            action?.let { findNavController().navigate(it) }
        }
        if (event is SaveMovieUIEvent.DismissSheet){
            dismiss()
        }
        if (event is SaveMovieUIEvent.ShowLoginDialog){
            action = SaveMovieDialogDirections.actionSaveToListDialogToLoginDialog(-1)
            action?.let { findNavController().navigate(it) }
        }
    }

}