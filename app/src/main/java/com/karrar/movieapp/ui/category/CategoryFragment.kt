package com.karrar.movieapp.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentCategoryBinding
import com.karrar.movieapp.ui.adapters.LoadUIStateAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.category.uiState.CategoryUIEvent
import com.karrar.movieapp.ui.explore.ExploringFragmentDirections
import com.karrar.movieapp.utilities.Constants.TV_CATEGORIES_ID
import com.karrar.movieapp.utilities.ViewMode
import com.karrar.movieapp.utilities.collect
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setupViewModeToggle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutIdFragment = R.layout.fragment_category
    override val viewModel: CategoryViewModel by viewModels()
    private val allMediaAdapter by lazy { CategoryAdapter(viewModel, viewModel) }
    private val listAdapter by lazy { ExploreListAdapter(viewModel, viewModel) }

    private var currentMode = ViewMode.GRID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMediaAdapter()
        collectEvent()
        collectData()
        setupToggleButton()
    }

    private fun setMediaAdapter() {
        val footerAdapter = LoadUIStateAdapter(allMediaAdapter::retry)
        binding.recyclerMedia.adapter = allMediaAdapter.withLoadStateFooter(footerAdapter)

        setupGridLayoutManager(footerAdapter)

        collect(
            flow = allMediaAdapter.loadStateFlow,
            action = { viewModel.setErrorUiState(it) }
        )
    }

    private fun setupGridLayoutManager(footerAdapter: LoadUIStateAdapter) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = binding.recyclerMedia.adapter
                return when {
                    adapter != null && position == adapter.itemCount - 1 && footerAdapter.itemCount > 0 -> 2
                    adapter != null && adapter.getItemViewType(position) == CategoryAdapter.VIEW_TYPE_GENRES -> 2
                    else -> 1
                }
            }
        }
        binding.recyclerMedia.layoutManager = gridLayoutManager
    }

    private fun setupToggleButton() {
        val toggleRoot = binding.viewModeToggle

        setupViewModeToggle(toggleRoot, currentMode) { newMode ->
            currentMode = newMode
            updateRecyclerLayout(newMode)
        }
    }

    private fun updateRecyclerLayout(mode: ViewMode) {
        when (mode) {
            ViewMode.GRID -> {
                // Create new GridLayoutManager with proper span size lookup
                val footerAdapter = LoadUIStateAdapter(allMediaAdapter::retry)
                val gridLayoutManager = GridLayoutManager(requireContext(), 2)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val adapter = binding.recyclerMedia.adapter
                        return when {
                            adapter != null && position == adapter.itemCount - 1 && footerAdapter.itemCount > 0 -> 2
                            adapter != null && adapter.getItemViewType(position) == CategoryAdapter.VIEW_TYPE_GENRES -> 2
                            else -> 1
                        }
                    }
                }

                binding.recyclerMedia.layoutManager = gridLayoutManager
                binding.recyclerMedia.adapter = allMediaAdapter.withLoadStateFooter(footerAdapter)

                // Refresh genres section after switching to grid
                allMediaAdapter.refreshGenresSection()
            }

            ViewMode.LIST -> {
                binding.recyclerMedia.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerMedia.adapter = listAdapter

                // Refresh genres section after switching to list
                listAdapter.refreshGenresSection()
            }
        }

        // Ensure genres data is up to date after layout change
        refreshGenresForCurrentAdapter()
    }

    private fun refreshGenresForCurrentAdapter() {
        val currentState = viewModel.uiState.value
        when (currentMode) {
            ViewMode.GRID -> {
                allMediaAdapter.setGenres(currentState.genre, currentState.selectedCategoryID)
            }
            ViewMode.LIST -> {
                listAdapter.setGenres(currentState.genre, currentState.selectedCategoryID)
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                collectLast(state.media) { pagingData ->
                    allMediaAdapter.submitData(pagingData)
                    listAdapter.submitData(pagingData)
                }

                // Update genres for both adapters
                allMediaAdapter.setGenres(state.genre, state.selectedCategoryID)
                listAdapter.setGenres(state.genre, state.selectedCategoryID)

                // Refresh the currently visible adapter's genres section
                when (currentMode) {
                    ViewMode.GRID -> allMediaAdapter.refreshGenresSection()
                    ViewMode.LIST -> listAdapter.refreshGenresSection()
                }
            }
        }
    }

    private fun collectEvent() {
        collect(viewModel.categoryUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: CategoryUIEvent) {
        when (event) {
            is CategoryUIEvent.ClickMovieEvent -> {
                if (viewModel.args.mediaId == TV_CATEGORIES_ID) {
                    navigateToTvShowDetails(event.movieID)
                } else {
                    navigateToMovieDetails(event.movieID)
                }
            }

            CategoryUIEvent.RetryEvent -> {
                when (currentMode) {
                    ViewMode.GRID -> allMediaAdapter.retry()
                    ViewMode.LIST -> listAdapter.retry()
                }
            }

            is CategoryUIEvent.SelectedCategory -> {
                viewModel.getMediaList(event.categoryID)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val action =
            ExploringFragmentDirections.actionExploringFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    private fun navigateToTvShowDetails(tvShowId: Int) {
        val action =
            ExploringFragmentDirections.actionExploringFragmentToTvShowDetailsFragment(tvShowId)
        findNavController().navigate(action)
    }

    private fun getTitle(): String {
        return if (viewModel.args.mediaId == TV_CATEGORIES_ID) {
            resources.getString(R.string.title_tv_shows)
        } else {
            resources.getString(R.string.movies)
        }
    }

    companion object {
        private const val ARG_MEDIA_ID = "mediaId"

        fun newInstance(mediaId: Int): CategoryFragment {
            val fragment = CategoryFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_MEDIA_ID, mediaId)
            }
            return fragment
        }
    }
}