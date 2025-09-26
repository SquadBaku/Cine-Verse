package com.karrar.movieapp.ui.match.matchChoices

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.GetMatchMovieListUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.home.homeUiState.HomeUiState
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchChoicesViewModel @Inject constructor(
    private val getMatchMovieListUseCase : GetMatchMovieListUseCase
) : BaseViewModel() {

    private val _matchUiState = MutableStateFlow(MatchChoicesUiState())
    val matchUiState = _matchUiState.asStateFlow()


    private val _matchChoicesEvent = MutableStateFlow<Event<MatchChoicesUIEvent?>>(Event(null))
    val matchChoicesEvent = _matchChoicesEvent.asStateFlow()

    private var fetchJob: Job? = null

    fun getMatchMovie(
        genre: List<String>,
        time: String,
        classicOrRecent: String,
    ) {
        val withRuntimeGte : Int? = getWithRunTimeGte(time)
        val withRuntimeLte : Int?= getWithRunTimeLte(time)
        val primaryReleaseDateGte: String? = getPrimaryReleaseDateGte(classicOrRecent)
        val primaryReleaseDateLte : String?= getPrimaryReleaseDateLte(classicOrRecent)

        fetchJob =viewModelScope.launch {
            try {
               val result =  getMatchMovieListUseCase(
                    genres = genre,
                    withRuntimeGte = withRuntimeGte,
                    withRuntimeLte = withRuntimeLte,
                    primaryReleaseDateGte = primaryReleaseDateGte,
                    primaryReleaseDateLte = primaryReleaseDateLte
                )

                _matchUiState.value = _matchUiState.value.copy(
                    result = result
                )

                _matchChoicesEvent.update {
                    Event(MatchChoicesUIEvent.DoneLoadingDataEvent)
                }

            } catch (th: Throwable) {
                _matchUiState.value = _matchUiState.value.copy(
                    result = null ,
                    isLoading = false,
                    error = listOf(th.message.toString())
                )
            }
        }
    }

    fun clearError(){
        _matchUiState.update {
            it.copy(error = emptyList())
        }
    }

    fun clearData(){
        _matchUiState.update {
            it.copy(
                error = emptyList(),
                result = null ,
                isLoading = false
            )
        }
    }

    fun stopLoadingData() {
        fetchJob?.cancel()
    }

    private fun getPrimaryReleaseDateLte(classicOrRecent: String): String? {
        return when (classicOrRecent) {
            "Classic" -> {
                "2000-01-01"
            }
            "Recent" -> {
                null
            }
            else -> {
                null
            }
        }
    }

    private fun getPrimaryReleaseDateGte(classicOrRecent: String): String? {
        return when (classicOrRecent) {
            "Classic" -> {
                null
            }
            "Recent" -> {
                "2023-01-01"
            }
            else -> {
                null
            }
        }
    }

    private fun getWithRunTimeLte(time : String ): Int? {
        return when (time) {
            "Short" -> {
                90
            }
            "Medium" -> {
                120
            }
            "long" -> {
                null
            }
            else -> {
                null
            }
        }
    }

    private fun getWithRunTimeGte(time : String ): Int? {
       return when (time) {
            "Short" -> {
                 null
            }
            "Medium" -> {
                 90
            }
            "long" -> {
                 120
            }
            else -> {
                null
            }
        }
    }

    init {
        getData()
    }

    override fun getData() {

    }

}
