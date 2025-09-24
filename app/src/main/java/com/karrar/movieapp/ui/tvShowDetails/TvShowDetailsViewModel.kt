package com.karrar.movieapp.ui.tvShowDetails

import android.util.Log
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.models.TvShowDetails
import com.karrar.movieapp.domain.usecases.CheckIfLoggedInUseCase
import com.karrar.movieapp.domain.usecases.GetSessionIDUseCase
import com.karrar.movieapp.domain.usecases.movieDetails.SetRatingUseCase
import com.karrar.movieapp.domain.usecases.tvShowDetails.GetTvShowDetailsUseCase
import com.karrar.movieapp.domain.usecases.tvShowDetails.InsertTvShowUserCase
import com.karrar.movieapp.domain.usecases.tvShowDetails.SetRatingUesCase
import com.karrar.movieapp.ui.adapters.ActorsInteractionListener
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.movieDetails.DetailInteractionListener
import com.karrar.movieapp.ui.movieDetails.MovieDetailsUIEvent
import com.karrar.movieapp.ui.movieDetails.mapper.ActorUIStateMapper
import com.karrar.movieapp.ui.tvShowDetails.tvShowUIMapper.TvShowMapperContainer
import com.karrar.movieapp.ui.tvShowDetails.tvShowUIState.DetailItemUIState
import com.karrar.movieapp.ui.tvShowDetails.tvShowUIState.Error
import com.karrar.movieapp.ui.tvShowDetails.tvShowUIState.TvShowDetailsUIState
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
class TvShowDetailsViewModel @Inject constructor(
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getInsertTvShowUserCase: InsertTvShowUserCase,
    private val setRatingUesCase: SetRatingUesCase,
    private val sessionIDUseCase: GetSessionIDUseCase,
    private val tvShowMapperContainer: TvShowMapperContainer,
    private val actorUIStateMapper: ActorUIStateMapper,
    private val setRatingUseCase: SetRatingUseCase,
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase,
    state: SavedStateHandle,
) : BaseViewModel(), ActorsInteractionListener, SeasonInteractionListener,
    DetailInteractionListener {

    val args = TvShowDetailsFragmentArgs.fromSavedStateHandle(state)

    private val _tvShowDetailsUIEvent =
        MutableStateFlow<Event<TvShowDetailsUIEvent?>>(Event(null))
    val tvShowDetailsUIEvent = this._tvShowDetailsUIEvent.asStateFlow()

    private val _stateUI = MutableStateFlow(TvShowDetailsUIState())
    val stateFlow: StateFlow<TvShowDetailsUIState> = _stateUI.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        _stateUI.update { it.copy(isLoading = true, errorUIState = emptyList()) }
        getTvShowDetails(args.tvShowId)
        getLoginStatus()
        getTvShowCast(args.tvShowId)
        getSeasons(args.tvShowId)
        getTvShowReviews(args.tvShowId)
    }

    private fun getTvShowDetails(tvShowId: Int) {
        viewModelScope.launch {
            try {
                val result = getTvShowDetailsUseCase.getTvShowDetails(tvShowId)
                val tvShowDetailsResult = tvShowMapperContainer.tvShowDetailsUIMapper.map(result)
                _stateUI.update {
                    it.copy(
                        tvShowDetailsResult = tvShowDetailsResult,
                        isLoading = false
                    )
                }
                updateDetailItems(DetailItemUIState.Header(_stateUI.value.tvShowDetailsResult))
                insertMovieToWatchHistory(result)
            } catch (e: Exception) {
                _stateUI.update {
                    it.copy(
                        errorUIState = listOf(
                            Error(
                                code = Constants.INTERNET_STATUS,
                                message = e.message.toString()
                            )
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getTvShowCast(tvShowId: Int) {
        viewModelScope.launch {
            try {
                val result = getTvShowDetailsUseCase.getSeriesCast(tvShowId)
                _stateUI.update { it ->
                    it.copy(
                        seriesCastResult = result.map { actorUIStateMapper.map(it) },
                        isLoading = false
                    )
                }
                updateDetailItems(DetailItemUIState.Cast(_stateUI.value.seriesCastResult))
            } catch (e: Exception) {
            }

        }
    }

    private fun getSeasons(tvShowId: Int) {
        viewModelScope.launch {
            try {
                val seasons = getTvShowDetailsUseCase.getSeasons(tvShowId)
                _stateUI.update { it ->
                    it.copy(
                        seriesSeasonsResult = seasons.map {
                            tvShowMapperContainer.tvShowSeasonUIMapper.map(it)
                        },
                        isLoading = false
                    )
                }
                updateDetailItems(DetailItemUIState.Seasons(_stateUI.value.seriesSeasonsResult))
            } catch (e: Exception) {
            }
        }
    }

    private fun getLoginStatus() {
        if (!sessionIDUseCase().isNullOrEmpty()) {
            _stateUI.update { it.copy(isLogin = true) }
            getRatedTvShows(args.tvShowId)
        }
    }

    private fun getRatedTvShows(tvShowId: Int) {
        viewModelScope.launch {
            try {
                _stateUI.update {
                    it.copy(
                        ratingValue = getTvShowDetailsUseCase.getTvShowRated(
                            tvShowId
                        )
                    )
                }
                updateDetailItems(DetailItemUIState.Rating(this@TvShowDetailsViewModel))
            } catch (e: Throwable) {
            }
        }
    }

    fun onChangeRating(value: Float) {
        _stateUI.update { it.copy(ratingValue = value) }
    }

    fun submitRating() {
        val rating = _stateUI.value.ratingValue
        if (rating <= 0f) return

        viewModelScope.launch {
            try {
                setRatingUseCase(args.tvShowId, rating)
                _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.MessageAppear) }
            } catch (e: Throwable) {

            }
        }
    }

    private fun getTvShowReviews(tvShowId: Int) {
        viewModelScope.launch {
            try {
                val result = getTvShowDetailsUseCase.getTvShowReviews(tvShowId)
                _stateUI.update {
                    it.copy(
                        seriesReviewsResult = result.reviews.map { review ->
                            tvShowMapperContainer.tvShowReviewUIMapper.map(review)
                        }, isLoading = false
                    )
                }
                if (result.reviews.isNotEmpty()) {
                    setReviews(result.isMoreThanMax)
                }
            } catch (e: Throwable) {
            }
        }
    }

    private fun setReviews(showSeeAll: Boolean) {
        _stateUI.value.seriesReviewsResult
            .forEach { updateDetailItems(DetailItemUIState.Comment(it)) }
        updateDetailItems(DetailItemUIState.ReviewText)

        if (showSeeAll) {
            updateDetailItems(DetailItemUIState.SeeAllReviewsButton)
        }
    }

    private fun updateDetailItems(item: DetailItemUIState) {
        val list = _stateUI.value.detailItemResult.toMutableList()
        list.add(item)
        _stateUI.update { it.copy(detailItemResult = list.toList()) }
    }

    private suspend fun insertMovieToWatchHistory(tvShow: TvShowDetails) {
        getInsertTvShowUserCase(tvShow)
    }

    override fun onClickSave() {}

    override fun onClickPlayTrailer() {
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.ClickPlayTrailerEvent) }
    }

    override fun onclickBack() {
        this._tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.ClickBackEvent) }
    }

    override fun onclickViewReviews() {
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.ClickReviewsEvent) }
    }

    override fun onClickRate() {
        Log.d("testRating","clicked")
        val isLoggedIn = checkIfLoggedInUseCase()
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.RateTheMovie(isLoggedIn)) }
    }

    override fun onClickEscButton(view: View) {
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.DismissSheet) }
    }

    override fun onClickActor(actorID: Int) {
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.ClickCastEvent(actorID)) }
    }

    override fun onClickSeason(seasonNumber: Int) {
        _tvShowDetailsUIEvent.update { Event(TvShowDetailsUIEvent.ClickSeasonEvent(seasonNumber)) }
    }

    fun onClickShowMoreSeasons() {
        _tvShowDetailsUIEvent.tryEmit(Event(TvShowDetailsUIEvent.ClickShowMoreSeasonsEvent))
    }
}
