package com.karrar.movieapp.ui.profile.myratings

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMyRatingsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyRatingsFragment : BaseFragment<FragmentMyRatingsBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_my_ratings
    override val viewModel: MyRatingsViewModel by viewModels()

    private var isAdviceShowing = true
    private var isAnimating = false

    private lateinit var adapter: RatedMoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.infoCard.isVisible = isAdviceShowing
        binding.btnClose.setOnClickListener {
            isAdviceShowing = false
            binding.infoCard.isVisible = false
        }

        adapter = RatedMoviesAdapter(items = emptyList(), listener = viewModel)
        binding.recyclerViewRatedMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MyRatingsFragment.adapter
        }


        binding.emptyLayout.btnStartRating.setOnClickListener {
            findNavController().navigate(R.id.exploringFragment)
        }


        setupTabClickListeners()
    }

    private fun setupObservers() {

        collectLast(flow = viewModel.myRatingUIEvent) {
            it.getContentIfNotHandled()?.let(::onEvent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isMoviesTab.collect { isMoviesTab ->
                if (!isAnimating) {
                    animateTabTransition(isMoviesTab)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ratedUiState.collect { state ->
                handleUIState(state)
            }
        }
    }


    private fun handleUIState(state: MyRateUIState) {

        adapter.setItems(state.ratedList)

        binding.loadingContainer.isVisible = state.isLoading

        val hasError = state.error.isNotEmpty()
        val hasData = state.ratedList.isNotEmpty()
        val isEmpty = state.ratedList.isEmpty() && !state.isLoading

        binding.recyclerViewRatedMovies.isVisible = !state.isLoading && hasData && !hasError
        binding.emptyLayout.root.isVisible = !state.isLoading && isEmpty && !hasError

        binding.noInternetContainer.isVisible = !state.isLoading && hasError

        if (hasError) {
            binding.buttonRetry.setOnClickListener {
                viewModel.retryConnect()
            }
        }

        if (!state.isLoading && state.hasLoadedOnce) {
            animateContentAppearance()
        }
    }

    private fun animateContentAppearance() {
        val targetView = when {
            binding.recyclerViewRatedMovies.isVisible -> binding.recyclerViewRatedMovies
            binding.emptyLayout.root.isVisible -> binding.emptyLayout.root
            // تم تغيير من errorGroup إلى noInternetContainer
            binding.noInternetContainer.isVisible -> binding.noInternetContainer
            else -> null
        }

        targetView?.let { view ->
            view.alpha = 0f
            view.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    override fun onResume() {
        super.onResume()

        try {
            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.title = getString(R.string.my_ratings)
            activity.supportActionBar?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.color.background_screen
                )
            )
            view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.title = getString(R.string.my_ratings)
        } catch (e: Exception) {

        }

    }

    private fun setupTabClickListeners() {
        binding.tabMovies.setOnClickListener {
            if (!isAnimating && !viewModel.ratedUiState.value.isLoading) {
                viewModel.onTabMovies()
            }
        }

        binding.tabSeries.setOnClickListener {
            if (!isAnimating && !viewModel.ratedUiState.value.isLoading) {
                viewModel.onTabSeries()
            }
        }
    }

    private fun animateTabTransition(isMoviesTab: Boolean) {
        if (isAnimating) return

        isAnimating = true

        val primaryColor = ContextCompat.getColor(requireContext(), R.color.brand_primary)
        val tertiaryColor = ContextCompat.getColor(requireContext(), R.color.shade_tertiary)

        val activeTextView = if (isMoviesTab) binding.tvMovies else binding.tvSeries
        val inactiveTextView = if (isMoviesTab) binding.tvSeries else binding.tvMovies
        val activeIndicator = if (isMoviesTab) binding.indicatorMovies else binding.indicatorSeries
        val inactiveIndicator = if (isMoviesTab) binding.indicatorSeries else binding.indicatorMovies

        activeTextView.setTextColor(primaryColor)
        inactiveTextView.setTextColor(tertiaryColor)

        activeIndicator.visibility = View.VISIBLE
        activeIndicator.alpha = 1f
        inactiveIndicator.visibility = View.INVISIBLE
        inactiveIndicator.alpha = 0f

        activeTextView.setTypeface(activeTextView.typeface, Typeface.BOLD)
        inactiveTextView.setTypeface(inactiveTextView.typeface, Typeface.NORMAL)

        val activeScale = ObjectAnimator.ofPropertyValuesHolder(
            activeTextView,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.15f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.15f, 1f)
        ).apply {
            interpolator = OvershootInterpolator(1.2f)
            duration = 350
        }

        val indicatorSlide = ObjectAnimator.ofFloat(activeIndicator, "translationY", -10f, 0f)
            .apply {
                interpolator = OvershootInterpolator(0.8f)
                duration = 350
            }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(activeScale, indicatorSlide)

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                activeIndicator.translationY = 0f
                activeTextView.scaleX = 1f
                activeTextView.scaleY = 1f
                isAnimating = false
            }
        })

        animatorSet.start()
    }

    private fun onEvent(event: MyRatingUIEvent) {
        val action = when (event) {
            is MyRatingUIEvent.MovieEvent ->
                MyRatingsFragmentDirections
                    .actionRatedMoviesFragmentToMovieDetailFragment(event.movieID)
            is MyRatingUIEvent.TVShowEvent ->
                MyRatingsFragmentDirections
                    .actionRatedMoviesFragmentToTvShowDetailsFragment(event.tvShowID)
        }
        findNavController().navigate(action)
    }
}