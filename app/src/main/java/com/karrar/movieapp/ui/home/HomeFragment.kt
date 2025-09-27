package com.karrar.movieapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentHomeBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.featuredCollection.FeaturedCollectionsAdapter
import com.karrar.movieapp.ui.featuredCollection.FeaturedItem
import com.karrar.movieapp.ui.home.adapter.HomeAdapter
import com.karrar.movieapp.ui.home.homeUiState.HomeUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutIdFragment = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModels()
    lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        setAdapter()
        collectEvent()
        collectHomeData()
       // setupFeaturedCollectionsRecycler()
    }
//    private lateinit var featuredCollectionsAdapter: FeaturedCollectionsAdapter
//    private fun setupFeaturedCollectionsRecycler() {
//        val list = listOf(
//            FeaturedItem(R.drawable.late_night_thrills, "Late Night Thrillers",
//                FeaturedItem(R.drawable.family_night_picks, "Family Night Picks"),
//                FeaturedItem(R.drawable.cinematic_masterpieces, "Cinematic Masterpieces"),
//                FeaturedItem(R.drawable.mind_bending_stories, "Mind-Bending Stories"),
//                FeaturedItem(R.drawable.based_on_true_events, "Based on True Events"),
//                FeaturedItem(R.drawable.feel_good_favorites, "Feel-Good Favorites")
//            )
//
//    }



    override fun onResume() {
        super.onResume()
        viewModel.refreshHomeData()
    }

    private fun collectHomeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeUiState.collect {
                homeAdapter.setItems(
                    mutableListOf(
                        it.popularMovies,
                        HomeItem.WhatShouldIWatch(position = 1),
                        HomeItem.NeedMoreToWatch(position = 12),
                        it.nowStreamingMovies,
                        it.upcomingMovies,
                        it.matchVibeMovie,
                        it.topRatedMovie,
                        it.recentlyViewed,
                        it.collections,

//                        it.tvShowsSeries,
//                        it.onTheAiringSeries,
//                        it.airingTodaySeries,
//                        it.mysteryMovies,
//                        it.adventureMovies,
//                        it.trendingMovies,
//                        it.actors,
                    )
                )
            }
        }
    }

    private fun setAdapter() {
        homeAdapter = HomeAdapter(mutableListOf(), viewModel)
        binding.recyclerView.adapter = homeAdapter
    }

    private fun collectEvent() {
        collectLast(viewModel.homeUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: HomeUIEvent) {
        val action = when (event) {
            is HomeUIEvent.ClickActorEvent -> {
                HomeFragmentDirections.actionHomeFragmentToActorDetailsFragment(
                    event.actorID
                )
            }

            is HomeUIEvent.ClickMovieEvent -> {
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(
                    event.movieID
                )
            }

            HomeUIEvent.ClickSeeAllActorEvent -> {
                HomeFragmentDirections.actionHomeFragmentToActorsFragment()
            }

            is HomeUIEvent.ClickSeeAllMovieEvent -> {
                HomeFragmentDirections.actionHomeFragmentToAllMovieFragment(
                    -1, event.mediaType
                )
            }

            is HomeUIEvent.ClickSeeAllTVShowsEvent -> {
                HomeFragmentDirections.actionHomeFragmentToAllMovieFragment(
                    -1,
                    event.mediaType
                )
            }

            is HomeUIEvent.ClickSeriesEvent -> {
                HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                    event.seriesID
                )
            }

            HomeUIEvent.ClickSeeAllRecentlyViewed -> HomeFragmentDirections.actionHomeFragmentToWatchHistoryFragment()
            HomeUIEvent.ClickSeeAllCollections -> HomeFragmentDirections.actionHomeFragmentToSavedListFragment()

            is HomeUIEvent.ClickListEvent -> HomeFragmentDirections.actionHomeFragmentToListDetailsFragment(
                event.createdListUIState.listID,
                event.createdListUIState.name
            )

            HomeUIEvent.ClickWhatShouldIWatch ->
                HomeFragmentDirections.actionHomeFragmentToMatchFragment()

            HomeUIEvent.ClickNeedMoreToWatch ->
                HomeFragmentDirections.actionHomeFragmentToExploringFragment()
        }
        findNavController().navigate(action)
    }

}