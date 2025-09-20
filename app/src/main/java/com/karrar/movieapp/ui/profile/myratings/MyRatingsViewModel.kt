package com.karrar.movieapp.ui.profile.myratings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.GetGenreListUseCase
import com.karrar.movieapp.domain.usecases.GetListOfRatedUseCase
import com.karrar.movieapp.domain.usecases.GetTVShowDurationUseCase
import com.karrar.movieapp.domain.usecases.movieDetails.GetMovieDurationUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val getRatedUseCase: GetListOfRatedUseCase,
    private val ratedUIStateMapper: RatedUIStateMapper,
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getMovieDurationUseCase: GetMovieDurationUseCase,
    private val getTvShowDurationUseCase: GetTVShowDurationUseCase,
) : BaseViewModel(), RatedMoviesInteractionListener {

    private val _ratedUiState = MutableStateFlow(MyRateUIState())
    val ratedUiState: StateFlow<MyRateUIState> = _ratedUiState

    private val _myRatingUIEvent: MutableStateFlow<Event<MyRatingUIEvent?>> =
        MutableStateFlow(Event(null))
    val myRatingUIEvent = _myRatingUIEvent.asStateFlow()

    private val _isMoviesTab = MutableStateFlow(true)
    val isMoviesTab: StateFlow<Boolean> = _isMoviesTab.asStateFlow()

    private val _isSeriesTab = MutableStateFlow(false)
    val isSeriesTab: StateFlow<Boolean> = _isSeriesTab.asStateFlow()

    private val _isTabAnimating = MutableStateFlow(false)
    val isTabAnimating: StateFlow<Boolean> = _isTabAnimating.asStateFlow()


    fun getIsMoviesTab(): Boolean = _isMoviesTab.value
    fun getIsSeriesTab(): Boolean = _isSeriesTab.value


    private var allRatedList: List<RatedUIState> = emptyList()

    init {
        getGenres()
        getData()
    }

    private fun getGenres() {
        viewModelScope.launch {
            try {
                val movieGenreList = getGenreListUseCase(Constants.MOVIE_CATEGORIES_ID)
                val seriesGenreList = getGenreListUseCase(Constants.TV_CATEGORIES_ID)
                val mergedGenreList = movieGenreList + seriesGenreList
                _ratedUiState.update { it.copy(genreList = mergedGenreList) }
            } catch (e: Throwable) {
                _ratedUiState.update { it.copy(genreList = emptyList()) }
            }
        }
    }

    override fun getData() {
        viewModelScope.launch {
            _ratedUiState.update { it.copy(isLoading = true) }
            try {
                val baseList = getRatedUseCase().map { rate ->
                    ratedUIStateMapper.map(
                        input = rate,
                        categoryIdList = ratedUiState.value.genreList
                    )
                }

                val listWithDurations = baseList.map { item ->
                    val minutes = if (item.mediaType == Constants.MOVIE) {
                        getMovieDurationUseCase(movieId = item.id)
                    } else {
                        Log.d("TAG", "getData: ${item.id}")
                        getTvShowDurationUseCase(tvShowId = item.id)
                    }
                    if (item.mediaType != Constants.MOVIE)
                        Log.d("TAG", "getData: $minutes")
                    val pretty = TimeFormatters.formatMinutes(total = minutes)
                    item.copy(duration = pretty)
                }


                allRatedList = listWithDurations


                filterData()

                _ratedUiState.update {
                    it.copy(isLoading = false, error = emptyList(), isListEmpty = allRatedList.isEmpty())
                }
            } catch (t: Throwable) {
                _ratedUiState.update {
                    it.copy(isLoading = false, error = emptyList())
                }
            }
        }
    }

    private fun filterData() {
        val filteredList = if (_isMoviesTab.value) {
            allRatedList.filter { it.mediaType == Constants.MOVIE }
        } else {
            allRatedList.filter { it.mediaType != Constants.MOVIE }
        }

        _ratedUiState.update { it.copy(ratedList = filteredList) }
    }

    fun onTabMovies() {
        if (!_isMoviesTab.value && !_isTabAnimating.value) {
            _isTabAnimating.update { true }
            viewModelScope.launch {
                _isMoviesTab.update { true }
                _isSeriesTab.update { false }
                filterData()

                kotlinx.coroutines.delay(300)
                _isTabAnimating.update { false }
            }
        }
    }

    fun onTabSeries() {
        if (!_isSeriesTab.value && !_isTabAnimating.value) {
            _isTabAnimating.update { true }
            viewModelScope.launch {
                _isMoviesTab.update { false }
                _isSeriesTab.update { true }
                filterData()

                kotlinx.coroutines.delay(300)
                _isTabAnimating.update { false }
            }
        }
    }

    override fun onClickMovie(movieId: Int) {
        ratedUiState.value.ratedList.let { it ->
            val item = it.find { it.id == movieId }
            item?.let {
                if (it.mediaType == Constants.MOVIE) {
                    _myRatingUIEvent.update { Event(MyRatingUIEvent.MovieEvent(movieId)) }
                } else {
                    _myRatingUIEvent.update { Event(MyRatingUIEvent.TVShowEvent(movieId)) }
                }
            }
        }
    }

    fun retryConnect() {
        _ratedUiState.update { it.copy(error = emptyList()) }
        getData()
    }
}