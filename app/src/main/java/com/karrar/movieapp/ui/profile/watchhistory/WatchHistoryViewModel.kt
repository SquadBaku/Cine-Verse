package com.karrar.movieapp.ui.profile.watchhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.enums.MediaType
import com.karrar.movieapp.domain.mappers.WatchHistoryMapper
import com.karrar.movieapp.domain.usecases.DeleteWatchHistoryUseCase
import com.karrar.movieapp.domain.usecases.GetWatchHistoryUseCase
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchHistoryViewModel @Inject constructor(
    private val getWatchHistoryUseCase: GetWatchHistoryUseCase,
    private val watchHistoryMapper: WatchHistoryMapper,
    private val deleteWatchHistoryUseCase: DeleteWatchHistoryUseCase,
) : ViewModel(), WatchHistoryInteractionListener {

    private val _uiState = MutableStateFlow(WatchHistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableStateFlow(Event<WatchHistoryUIEvent?>(null))
    val watchHistoryUIEvent = _events.asStateFlow()

    private val _history = MutableStateFlow<List<MediaHistoryUiState>>(emptyList())
    val history = _history.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private fun onError(message: String) {
        _errorMessage.value = message
    }


    init { getWatchHistoryData() }
    fun retryConnect() = getWatchHistoryData()

    private fun getWatchHistoryData() {
        viewModelScope.launch {
            getWatchHistoryUseCase()
                .onStart { _uiState.update { it.copy(loading = true, error = emptyList()) } }
                .catch { t ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = listOf(Error(code = 400, message = t.message.orEmpty()))
                        )
                    }
                }
                .collect { list ->
                    val mapped = list.map(watchHistoryMapper::map)
                    _uiState.update { it.copy(loading = false, error = emptyList(), allMedia = mapped) }
                }
        }
    }

    override fun onClickMovie(item: MediaHistoryUiState) {
        if (item.mediaType.equals(Constants.MOVIE, true)) {
            _events.update { Event(WatchHistoryUIEvent.MovieEvent(item.id)) }
        } else {
            _events.update { Event(WatchHistoryUIEvent.TVShowEvent(item.id)) }
        }
    }

    override fun onSwipeDelete(item: MediaHistoryUiState) {
        val prev = _history.value

        _history.value = prev.filterNot { it.id == item.id }

        val type = if (item.mediaType.equals(Constants.MOVIE, true)) {
            MediaType.MOVIE
        } else {
            MediaType.TV_SHOW
        }
        viewModelScope.launch {
            runCatching { deleteWatchHistoryUseCase(item.id, type) }
                .onFailure { err ->
                    _history.value = prev
                    onError(err.message ?: "Delete failed")
                }
        }
    }


    fun deleteHistory(item: MediaHistoryUiState) {
        val type = if (item.mediaType.equals(Constants.MOVIE, ignoreCase = true)) {
            MediaType.MOVIE
        } else {
            MediaType.TV_SHOW
        }

        viewModelScope.launch {
            runCatching {
                deleteWatchHistoryUseCase(id = item.id, type = type)
            }.onFailure { err ->
                onError(err.message ?: "Delete failed")
            }
        }
    }


}



