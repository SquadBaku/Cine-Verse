package com.karrar.movieapp.ui.movieDetails.saveMovie

import android.util.Log
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.CheckIfLoggedInUseCase
import com.karrar.movieapp.domain.usecases.mylist.GetMyListUseCase
import com.karrar.movieapp.domain.usecases.mylist.SaveMovieToMyListUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.ui.movieDetails.saveMovie.uiState.MySavedListUIState
import com.karrar.movieapp.ui.movieDetails.saveMovie.uiState.SaveMovieUIEvent
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveMovieViewModel @Inject constructor(
    private val saveMovieToMyListUseCase: SaveMovieToMyListUseCase,
    private val getMyListUseCase: GetMyListUseCase,
    private val myListItemUIStateMapper: MyListItemUIStateMapper,
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase,
    state: SavedStateHandle,
) : BaseViewModel(), SaveListInteractionListener {

    private val args = SaveMovieDialogArgs.fromSavedStateHandle(state)

    private val _myListsUIState = MutableStateFlow(MySavedListUIState())
    val myListsUIState = _myListsUIState.asStateFlow()

    private val _saveMovieUIEvent = MutableStateFlow<Event<SaveMovieUIEvent?>>(Event(null))
    val saveMovieUIEvent = _saveMovieUIEvent.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        viewModelScope.launch {
            _myListsUIState.update { it.copy(isLoading = true, error = emptyList()) }
            try {
                _myListsUIState.update {
                    it.copy(
                        isLoading = false,
                        myListItemUI = getMyListUseCase().map { myListItemUIStateMapper.map(it) }
                    )
                }
            } catch (t: Throwable) {
                _myListsUIState.update {
                    it.copy(
                        isLoading = false,
                        error = listOf(ErrorUIState(404, t.message.toString()))
                    )
                }
            }
        }
    }

    override fun onClickSaveList(listID: Int) {
        Log.d("TAG bob", "onClickSaveList: movieId ${args.movieId} listID $listID")
        viewModelScope.launch {
            val message = try {
                saveMovieToMyListUseCase(listID, args.movieId)
            } catch (t: Throwable) {
                t.message.toString()
            }
            _saveMovieUIEvent.update { Event(SaveMovieUIEvent.DisplayMessage(message ?: "")) }
        }
    }

    override fun onClickCreateNewList(view: View) {
        _saveMovieUIEvent.update { Event(SaveMovieUIEvent.CreateNewList) }
    }

    override fun onClickEscButton(view: View) {
        _saveMovieUIEvent.update { Event(SaveMovieUIEvent.DismissSheet) }
    }

    override fun onClickAddButton(view: View) {
        _saveMovieUIEvent.update { Event(SaveMovieUIEvent.NavigateToListsScreen) }
    }

    override fun onClickLogin(view: View) {
        _saveMovieUIEvent.update { Event(SaveMovieUIEvent.NavigateToLoginScreen) }
    }

}