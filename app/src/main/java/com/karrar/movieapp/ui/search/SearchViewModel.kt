package com.karrar.movieapp.ui.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.map
import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.domain.usecases.movieDetails.GetMovieDetailsUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.DeleteAllSearchHistoryUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.DeleteSearchHistoryItemUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.GetSearchForActorUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.GetSearchForMovieUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.GetSearchForSeriesUserCase
import com.karrar.movieapp.domain.usecases.searchUseCase.GetSearchHistoryUseCase
import com.karrar.movieapp.domain.usecases.searchUseCase.PostSaveSearchResultUseCase
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.adapters.MovieInteractionListener
import com.karrar.movieapp.ui.allMedia.Error
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.search.adapters.ActorSearchInteractionListener
import com.karrar.movieapp.ui.search.adapters.MediaSearchInteractionListener
import com.karrar.movieapp.ui.search.adapters.SearchHistoryInteractionListener
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaSearchUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaTypes
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState
import com.karrar.movieapp.ui.search.uiStatMapper.SearchHistoryUIStateMapper
import com.karrar.movieapp.ui.search.uiStatMapper.SearchMediaUIStateMapper
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryUIStateMapper: SearchHistoryUIStateMapper,
    private val searchMediaUIStateMapper: SearchMediaUIStateMapper,
    private val getSearchForMovieUseCase: GetSearchForMovieUseCase,
    private val getSearchForSeriesUserCase: GetSearchForSeriesUserCase,
    private val getSearchForActorUseCase: GetSearchForActorUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val postSaveSearchResultUseCase: PostSaveSearchResultUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryUseCase,
    private val deleteSearchHistoryItemUseCase: DeleteSearchHistoryItemUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : BaseViewModel(), MediaSearchInteractionListener, ActorSearchInteractionListener,
    SearchHistoryInteractionListener, MediaInteractionListener , MovieInteractionListener{

    private val _uiState = MutableStateFlow(MediaSearchUIState())
    val uiState = _uiState.asStateFlow()

    private val _searchUIEvent = MutableStateFlow<Event<SearchUIEvent?>>(Event(null))
    val searchUIEvent = _searchUIEvent.asStateFlow()

    init {
        getAllSearchHistory()
    }

    override fun getData() {
        _searchUIEvent.update { Event(SearchUIEvent.ClickRetryEvent) }
    }

    private fun getAllSearchHistory() {
        Log.d("TAG lol", "getAllSearchHistory: : all items are : innnnnnnnnn")

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                Log.d("TAG lol", "getAllSearchHistory: first: all items are in tryyyyyyy")
                getSearchHistoryUseCase().collect { list ->
                    Log.d("TAG lol", "getAllSearchHistory: first: all items are in collect: $list")
                    _uiState.update {
                        it.copy(searchHistory = list.map { item ->
                            searchHistoryUIStateMapper.map(
                                item
                            )
                        }, isLoading = false, isEmpty = false)
                    }
                    Log.d("TAG lol", "getAllSearchHistory: first: all items are end collect: ${_uiState.value.searchHistory}")
                    getAllRecentlyViewed()
                }
                Log.d("TAG lol", "getAllSearchHistory: first 11111: all items are : ${_uiState.value.searchHistory}")
            } catch (e: Throwable) {
                Log.d("TAG lol", "getAllSearchHistory: erroooooor: all items are : ${e.message}")
                _uiState.update {
                    it.copy(error = listOf(Error(0, e.message.toString())))
                }
            }
        }
        Log.d("TAG lol", "getAllSearchHistory: first: all items are : ${_uiState.value.searchHistory}")
    }

    private fun getAllRecentlyViewed() {
        viewModelScope.launch {
            val recentlyViewedMovies = getSearchHistoryUseCase()

            recentlyViewedMovies.collect { list ->
                list.forEach { item ->
                    val details = getMovieDetailsUseCase.getMovieDetails(item.id.toInt())
                    _uiState.update {
                        it.copy(
                            recentlyViewed = it.recentlyViewed + MediaUIState(
                                mediaID = details.movieId,
                                mediaName = details.movieName,
                                mediaImage = details.movieImage,
                                mediaTypes = MediaTypes.MOVIE.name,
                                mediaVoteAverage = 9.0f,
                                mediaReleaseDate = "",
                            )
                        )
                    }
                }
                Log.d("TAG lol", "getAllRecentlyViewed: second: in collect : ${_uiState.value.recentlyViewed}")
                _uiState.update { it.copy(items = it.searchHistory.map { historyUIState -> historyUIState.toSearchItem() } + it.recentlyViewed.toSearchItem()) }
                Log.d("TAG lol", "getAllRecentlyViewed: last: all items are : ${_uiState.value.items}")

            }
        }
        Log.d("TAG lol", "getAllRecentlyViewed: second:  : ${_uiState.value.recentlyViewed}")

    }

    fun onSearchInputChange(searchTerm: CharSequence) {
        _uiState.update { it.copy(searchInput = searchTerm.toString(), isLoading = true) }
        viewModelScope.launch {
            when (_uiState.value.searchTypes) {
                MediaTypes.MOVIE -> onSearchForMovie()
                MediaTypes.TVS_SHOW -> onSearchForSeries()
                MediaTypes.ACTOR -> onSearchForActor()
            }
        }
    }


    fun onSearchForMovie() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.MOVIE,
                    isLoading = false,
                    searchResult = getSearchForMovieUseCase(it.searchInput).map { pagingData ->
                        pagingData.map { item -> searchMediaUIStateMapper.map(item) }
                    }
                )
            }
        }
    }

    fun onSearchForSeries() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.TVS_SHOW,
                    isLoading = false,
                    searchResult = getSearchForSeriesUserCase(it.searchInput).map { pagingData ->
                        pagingData.map { item -> searchMediaUIStateMapper.map(item) }
                    }
                )
            }
        }
    }

    fun onSearchForActor() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.ACTOR,
                    isLoading = false,
                    searchResult = getSearchForActorUseCase(it.searchInput).map { pagingData ->
                        pagingData.map { item -> searchMediaUIStateMapper.map(item) }
                    }
                )
            }
        }
    }


    override fun onClickMediaResult(media: MediaUIState) {
        saveSearchResult(media.mediaID, media.mediaName)
        _searchUIEvent.update { Event(SearchUIEvent.ClickMediaEvent(media)) }
    }

    override fun onClickActorResult(personID: Int, name: String) {
        saveSearchResult(personID, name)
        _searchUIEvent.update { Event(SearchUIEvent.ClickActorEvent(personID)) }
    }

    private fun saveSearchResult(id: Int, name: String) {
        viewModelScope.launch { postSaveSearchResultUseCase(id, name) }
    }

    override fun onClickSearchHistory(name: String) {
        onSearchInputChange(name)
    }

    override fun onClickDeleteSearchHistoryItem(id: Long) {
        Log.d("onClickDelete tag", "onClickDeleteSearchHistoryItem: delete : $id")
        viewModelScope.launch {
            deleteSearchHistoryItemUseCase(id = id)
            _uiState.update {
                it.copy(searchHistory = it.searchHistory.filter { item -> item.id != id })
            }
        }
    }

    override fun onClickClearAllHistory() {
        Log.d("onClickDelete tag", "onClickDeleteSearchHistoryItem: delete : clear all")
        _uiState.update { it.copy(searchHistory = emptyList()) }
        viewModelScope.launch {
            deleteAllSearchHistoryUseCase()
        }
    }

    fun onClickBack() {
        _searchUIEvent.update { Event(SearchUIEvent.ClickBackEvent) }
    }

    fun setErrorUiState(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        when (combinedLoadStates.refresh) {
            is LoadState.Loading -> {
                _uiState.update {
                    it.copy(isLoading = true, error = emptyList(), isEmpty = false)
                }
            }

            is LoadState.Error -> {
                _uiState.update {
                    it.copy(isLoading = false, error = listOf(Error(404, "")), isEmpty = false)
                }
            }

            is LoadState.NotLoading -> {
                if (itemCount < 1) {
                    _uiState.update {
                        it.copy(
                            isEmpty = true,
                            isLoading = false,
                            error = emptyList()
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isEmpty = false,
                            isLoading = false,
                            error = emptyList()
                        )
                    }
                }
            }
        }
    }

    override fun onClickMedia(mediaId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickMovie(movieId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickSeeAllMovie(homeItemsType: HomeItemsType) {
        TODO("Not yet implemented")
    }

}

fun SearchHistoryUIState.toSearchItem(): SearchItem {
    return SearchItem.RecentSearch(
        item = SearchHistoryUIState(
            id = this.id,
            name = this.name,
        ),
        type = SearchItemType.RECENT_SEARCH
    )
}

fun List<MediaUIState>.toSearchItem(): SearchItem {
    return SearchItem.RecentlyViewed(
        media = this.map {
            MediaUiState(
                id = it.mediaID,
                imageUrl = it.mediaImage
            )
        },
        type = SearchItemType.RECENTLY_VIEWED
    )
}