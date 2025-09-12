package com.karrar.movieapp.ui.category

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.karrar.movieapp.utilities.setSpanSize
import com.karrar.movieapp.utilities.setupViewModeToggle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    override val layoutIdFragment = R.layout.fragment_category
    override val viewModel: CategoryViewModel by viewModels()
    private val allMediaAdapter by lazy { CategoryAdapter(viewModel) }
    private val listAdapter by lazy { ExploreListAdapter(viewModel) }

    private var currentMode = ViewMode.GRID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMediaAdapter()
        collectEvent()
        collectData()
        setupToggleButton()
    }

    private fun setMediaAdapter() {
//        val footerAdapter = LoadUIStateAdapter(allMediaAdapter::retry)
//        binding.recyclerMedia.adapter = allMediaAdapter.withLoadStateFooter(footerAdapter)
//
//        val mManager = binding.recyclerMedia.layoutManager as GridLayoutManager
//        mManager.setSpanSize(footerAdapter, allMediaAdapter, mManager.spanCount)
//
//        collect(flow = allMediaAdapter.loadStateFlow,
//            action = { viewModel.setErrorUiState(it) })
        val footerAdapter = LoadUIStateAdapter(allMediaAdapter::retry)
        binding.recyclerMedia.adapter = allMediaAdapter.withLoadStateFooter(footerAdapter)

        // Default: Grid Layout
        binding.recyclerMedia.layoutManager = GridLayoutManager(requireContext(), 2)
        (binding.recyclerMedia.layoutManager as GridLayoutManager)
            .setSpanSize(footerAdapter, allMediaAdapter, 2)

        collect(
            flow = allMediaAdapter.loadStateFlow,
            action = { viewModel.setErrorUiState(it) }
        )
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
                binding.recyclerMedia.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.recyclerMedia.adapter = allMediaAdapter
            }
            ViewMode.LIST -> {
                binding.recyclerMedia.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerMedia.adapter = listAdapter
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                collectLast(viewModel.uiState.value.media) { pagingData ->
                    allMediaAdapter.submitData(pagingData)
                    listAdapter.submitData(pagingData)
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
                allMediaAdapter.retry()
            }
            is CategoryUIEvent.SelectedCategory -> {
                viewModel.getMediaList(event.categoryID)
            }
        }
    }

    private fun navigateToMovieDetails(movieId: Int) {
        val action = ExploringFragmentDirections.actionExploringFragmentToMovieDetailFragment(movieId)
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
