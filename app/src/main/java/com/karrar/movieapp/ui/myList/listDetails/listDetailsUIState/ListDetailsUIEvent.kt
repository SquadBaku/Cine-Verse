package com.karrar.movieapp.ui.myList.listDetails.listDetailsUIState

sealed interface ListDetailsUIEvent {
    data class OnItemSelected(val savedMediaUIState: SavedMediaUIState) : ListDetailsUIEvent
    data class ShowMessage(val message: String) : ListDetailsUIEvent
}