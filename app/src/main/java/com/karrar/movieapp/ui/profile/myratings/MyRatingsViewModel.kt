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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    companion object {
        private const val TAG = "MyRatingsViewModel"
    }

    private val _ratedUiState = MutableStateFlow(MyRateUIState())
    val ratedUiState: StateFlow<MyRateUIState> = _ratedUiState

    private val _myRatingUIEvent: MutableStateFlow<Event<MyRatingUIEvent?>> =
        MutableStateFlow(Event(null))
    val myRatingUIEvent = _myRatingUIEvent.asStateFlow()

    private val _isMoviesTab = MutableStateFlow(true)
    val isMoviesTab: StateFlow<Boolean> = _isMoviesTab.asStateFlow()
    val isSeriesTab: StateFlow<Boolean> = isMoviesTab.map { !it }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _isTabAnimating = MutableStateFlow(false)
    val isTabAnimating: StateFlow<Boolean> = _isTabAnimating.asStateFlow()

    fun getIsMoviesTab(): Boolean = _isMoviesTab.value
    fun getIsSeriesTab(): Boolean = !_isMoviesTab.value

    private var allRatedList: List<RatedUIState> = emptyList()

    init {
        Log.d(TAG, "ViewModel initialized")
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
            Log.d(TAG, "Getting rated data...")

            _ratedUiState.update {
                it.copy(
                    isLoading = true,
                    isInitialLoad = !it.hasLoadedOnce,
                    error = emptyList()
                )
            }

            try {
                val startTime = System.currentTimeMillis()

                val ratedData = getRatedUseCase()


                val baseList = ratedData.map { rate ->
                    ratedUIStateMapper.map(
                        input = rate,
                        categoryIdList = ratedUiState.value.genreList
                    )
                }
                Log.d(TAG, "Mapped data size: ${baseList.size}")

                val listWithDurations = baseList.map { item ->

                    val minutes = if (item.mediaType == Constants.MOVIE) {
                        getMovieDurationUseCase(movieId = item.id)
                    } else {
                        getTvShowDurationUseCase(tvShowId = item.id)
                    }

                    val pretty = TimeFormatters.formatMinutes(total = minutes)
                    item.copy(duration = pretty)
                }

                allRatedList = listWithDurations

                allRatedList.forEachIndexed { index, item ->
                    Log.d(TAG, "Item $index: ${item.title} - ${item.mediaType} - Rating: ${item.userRating}")
                }

                val elapsedTime = System.currentTimeMillis() - startTime
                val minimumLoadingTime = 500L
                if (elapsedTime < minimumLoadingTime) {
                    kotlinx.coroutines.delay(minimumLoadingTime - elapsedTime)
                }

                filterData()

                _ratedUiState.update {
                    it.copy(
                        isLoading = false,
                        error = emptyList(),
                        isListEmpty = allRatedList.isEmpty(),
                        hasLoadedOnce = true,
                        isInitialLoad = false
                    )
                }


            } catch (t: Throwable) {
                val startTime = System.currentTimeMillis()
                val minimumLoadingTime = 500L
                val elapsedTime = System.currentTimeMillis() - startTime
                if (elapsedTime < minimumLoadingTime) {
                    kotlinx.coroutines.delay(minimumLoadingTime - elapsedTime)
                }

                _ratedUiState.update {
                    it.copy(
                        isLoading = false,
                        error = listOf(t.message ?: "Unknown error"),
                        hasLoadedOnce = true,
                        isInitialLoad = false
                    )
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

        filteredList.forEachIndexed { index, item ->

        }

        _ratedUiState.update { it.copy(ratedList = filteredList) }

    }

    fun onTabMovies() {
        if (_isMoviesTab.value || _isTabAnimating.value || _ratedUiState.value.isLoading) {
            Log.d(TAG, "onTabMovies ignored - already movies tab or animating or loading")
            return
        }
        _isTabAnimating.value = true
        _isMoviesTab.value = true

        showTabLoadingAndFilter()
    }

    fun onTabSeries() {
        if (!_isMoviesTab.value || _isTabAnimating.value || _ratedUiState.value.isLoading) {

            return
        }

        Log.d(TAG, "Switching to Series tab")
        _isTabAnimating.value = true
        _isMoviesTab.value = false

        showTabLoadingAndFilter()
    }

    private fun showTabLoadingAndFilter() {
        viewModelScope.launch {


            _ratedUiState.update { it.copy(isLoading = true) }

            kotlinx.coroutines.delay(200)

            filterData()

            _ratedUiState.update { it.copy(isLoading = false) }

            kotlinx.coroutines.delay(350)
            _isTabAnimating.value = false

        }
    }

    override fun onClickMovie(movieId: Int) {
        ratedUiState.value.ratedList.let { list ->
            val item = list.find { it.id == movieId }
            item?.let {
                Log.d(TAG, "Found item: ${it.title} - ${it.mediaType}")
                if (it.mediaType == Constants.MOVIE) {
                    _myRatingUIEvent.update { Event(MyRatingUIEvent.MovieEvent(movieId)) }
                } else {
                    _myRatingUIEvent.update { Event(MyRatingUIEvent.TVShowEvent(movieId)) }
                }
            } ?: Log.w(TAG, "Item not found for movieId: $movieId")
        }
    }

    fun retryConnect() {
        Log.d(TAG, "Retrying connection...")
        _ratedUiState.update {
            it.copy(
                error = emptyList(),
                isInitialLoad = false
            )
        }
        getData()
    }

    fun refreshData() {
        Log.d(TAG, "Refreshing data...")
        _ratedUiState.update {
            it.copy(isInitialLoad = false)
        }
        getData()
    }
}