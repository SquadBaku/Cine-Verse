package com.karrar.movieapp.ui.movieDetails.saveMovie.uiState

sealed interface SaveMovieUIEvent {
    data class DisplayMessage(val message: String) : SaveMovieUIEvent
    object CreateNewList : SaveMovieUIEvent
    object DismissSheet : SaveMovieUIEvent
    object NavigateToListsScreen : SaveMovieUIEvent
    object ShowLoginDialog: SaveMovieUIEvent
    object NavigateToLoginScreen: SaveMovieUIEvent
}