package com.karrar.movieapp.ui.search

import android.content.Context
import android.os.Bundle
import android.transition.ChangeTransform
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentSearchBinding
import com.karrar.movieapp.ui.adapters.LoadUIStateAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.search.adapters.ActorSearchAdapter
import com.karrar.movieapp.ui.search.adapters.MediaSearchAdapter
import com.karrar.movieapp.ui.search.adapters.SearchHistoryAdapter
import com.karrar.movieapp.ui.search.adapters.SearchSuggestionAdapter
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaSearchUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaTypes
import com.karrar.movieapp.utilities.ViewMode
import com.karrar.movieapp.utilities.collect
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setupViewModeToggle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_search
    override val viewModel: SearchViewModel by viewModels()

    private val mediaSearchAdapter by lazy { MediaSearchAdapter(viewModel) }
    private val actorSearchAdapter by lazy { ActorSearchAdapter(viewModel) }

    private val oldValue = MutableStateFlow(MediaSearchUIState())

    private var currentViewMode = ViewMode.GRID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = ChangeTransform()
        setTitle(false)

        setupTabs()
        getSearchResult()
        setSearchHistoryAdapter()
        getSearchResultsBySearchTerm()

        collectLast(viewModel.searchUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }

        collectLast(viewModel.uiState) { state ->
            updateSelectedTab(state.searchTypes)
        }

        setupViewModeToggle(binding.viewModeToggle, currentViewMode) { mode ->
            currentViewMode = mode
            updateRecyclerLayout(mode)
            mediaSearchAdapter.setViewMode(mode)
        }
    }

    private fun setupTabs() {
        val tabLayout = binding.tabMediaType

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.movies_chip)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tv_shows_chip)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.actors_label)))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> viewModel.onSearchForMovie()
                    1 -> viewModel.onSearchForSeries()
                    2 -> viewModel.onSearchForActor()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun updateSelectedTab(type: MediaTypes) {
        val tabLayout = binding.tabMediaType
        when (type) {
            MediaTypes.MOVIE -> tabLayout.getTabAt(0)?.select()
            MediaTypes.TVS_SHOW -> tabLayout.getTabAt(1)?.select()
            MediaTypes.ACTOR -> tabLayout.getTabAt(2)?.select()
        }
    }

    private fun setSearchHistoryAdapter() {
        val inputMethodManager =
            binding.inputSearch.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.inputSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.recyclerSearchHistory.adapter = SearchHistoryAdapter(mutableListOf(), viewModel)
        binding.recyclerSearchSuggestion.adapter =
            SearchSuggestionAdapter(mutableListOf(), viewModel)
    }

    @OptIn(FlowPreview::class)
    private fun getSearchResultsBySearchTerm() {
        lifecycleScope.launch {
            viewModel.uiState.debounce(500).collectLatest { searchTerm ->
                if (searchTerm.searchInput.isNotBlank()
                    && oldValue.value.searchInput != viewModel.uiState.value.searchInput
                    || oldValue.value.searchTypes != viewModel.uiState.value.searchTypes
                ) {
                    getSearchResult()
                    oldValue.emit(viewModel.uiState.value)
                }
            }
        }
    }

    private fun getSearchResult() {
        when (viewModel.uiState.value.searchTypes) {
            MediaTypes.ACTOR -> bindActors()
            else -> bindMedia()
        }
    }

    private fun onEvent(event: SearchUIEvent) {
        when (event) {
            is SearchUIEvent.ClickActorEvent -> navigateToActorDetails(event.actorID)
            SearchUIEvent.ClickBackEvent -> popFragment()
            is SearchUIEvent.ClickMediaEvent -> {
                when (event.mediaUIState.mediaTypes) {
                    com.karrar.movieapp.utilities.Constants.MOVIE ->
                        navigateToMovieDetails(event.mediaUIState.mediaID)

                    com.karrar.movieapp.utilities.Constants.TV_SHOWS ->
                        navigateToSeriesDetails(event.mediaUIState.mediaID)

                    com.karrar.movieapp.utilities.Constants.ACTOR ->
                        navigateToActorDetails(actorId = event.mediaUIState.mediaID)
                }
            }

            SearchUIEvent.ClickRetryEvent -> {
                actorSearchAdapter.retry()
                mediaSearchAdapter.retry()
            }

            is SearchUIEvent.ClickMovieEvent -> navigateToMovieDetails(event.movieID)
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieId)
        )
    }

    private fun navigateToSeriesDetails(seriesId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToTvShowDetailsFragment(seriesId)
        )
    }

    private fun navigateToActorDetails(actorId: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToActorDetailsFragment(actorId)
        )
    }

    private fun bindMedia() {
        val footerAdapter = LoadUIStateAdapter(mediaSearchAdapter::retry)
        binding.recyclerMedia.adapter = mediaSearchAdapter.withLoadStateFooter(footerAdapter)
        updateRecyclerLayout(currentViewMode)

        collect(flow = mediaSearchAdapter.loadStateFlow) {
            viewModel.setErrorUiState(it, mediaSearchAdapter.itemCount)
        }

        getMediaSearchResults()
    }

    private fun bindActors() {
        val footerAdapter = LoadUIStateAdapter(actorSearchAdapter::retry)
        binding.recyclerMedia.adapter = actorSearchAdapter.withLoadStateFooter(footerAdapter)
        binding.recyclerMedia.layoutManager = GridLayoutManager(this@SearchFragment.context, 3)
        setSpanSize(footerAdapter)

        collect(flow = actorSearchAdapter.loadStateFlow) {
            viewModel.setErrorUiState(it, actorSearchAdapter.itemCount)
        }

        getActorsSearchResults()
    }

    private fun getMediaSearchResults() {
        collectLast(viewModel.uiState.value.searchResult) {
            mediaSearchAdapter.submitData(it)
        }
    }

    private fun getActorsSearchResults() {
        collectLast(viewModel.uiState.value.searchResult) {
            actorSearchAdapter.submitData(it)
        }
    }

    private fun setSpanSize(footerAdapter: LoadUIStateAdapter) {
        val mManager = binding.recyclerMedia.layoutManager as GridLayoutManager
        mManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((position == actorSearchAdapter.itemCount)
                    && footerAdapter.itemCount > 0
                ) {
                    mManager.spanCount
                } else {
                    1
                }
            }
        }
    }

    private fun updateRecyclerLayout(mode: ViewMode) {
        if (viewModel.uiState.value.searchTypes == MediaTypes.ACTOR) {
            binding.recyclerMedia.layoutManager = GridLayoutManager(requireContext(), 3)
        } else {
            when (mode) {
                ViewMode.GRID -> {
                    binding.recyclerMedia.layoutManager = GridLayoutManager(requireContext(), 2)
                }
                ViewMode.LIST -> {
                    binding.recyclerMedia.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    private fun popFragment() {
        findNavController().popBackStack()
    }
}