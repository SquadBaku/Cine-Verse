package com.karrar.movieapp.ui.profile.myratings

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    private var isAnimating = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false, getString(R.string.my_ratings))

        val adapter = RatedMoviesAdapter(emptyList(), viewModel)
        binding.recyclerViewRatedMovies.adapter = adapter

        setupTabAnimations()

        initializeTabs()

        collectLast(viewModel.myRatingUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }

        lifecycleScope.launch {
            viewModel.isMoviesTab.collect { isMoviesTab ->
                animateTabTransition(isMoviesTab)
            }
        }
    }

    private fun initializeTabs() {

        val primaryColor = ContextCompat.getColor(requireContext(), R.color.brand_primary)
        val tertiaryColor = ContextCompat.getColor(requireContext(), R.color.shade_tertiary)

        binding.tvMovies.setTextColor(primaryColor)
        binding.tvSeries.setTextColor(tertiaryColor)
        binding.indicatorMovies.visibility = View.VISIBLE
        binding.indicatorSeries.visibility = View.INVISIBLE
        binding.indicatorMovies.alpha = 1f
        binding.indicatorSeries.alpha = 0f
    }

    private fun setupTabAnimations() {
        binding.tabMovies.setOnClickListener {
            if (!isAnimating) {
                viewModel.onTabMovies()
            }
        }

        binding.tabSeries.setOnClickListener {
            if (!isAnimating) {
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

        val animatorSet = AnimatorSet()

        val activeTextColorAnimator = ObjectAnimator.ofObject(
            activeTextView, "textColor", ArgbEvaluator(), tertiaryColor, primaryColor
        )
        val inactiveTextColorAnimator = ObjectAnimator.ofObject(
            inactiveTextView, "textColor", ArgbEvaluator(), primaryColor, tertiaryColor
        )

        val activeTextScaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
            activeTextView,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.15f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.15f, 1f)
        )
        activeTextScaleAnimator.interpolator = OvershootInterpolator(1.2f)

        val hideIndicatorAnimator = ObjectAnimator.ofPropertyValuesHolder(
            inactiveIndicator,
            PropertyValuesHolder.ofFloat("alpha", 1f, 0f),
            PropertyValuesHolder.ofFloat("scaleX", 1f, 0.3f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 0.3f)
        )

        val showIndicatorAnimator = ObjectAnimator.ofPropertyValuesHolder(
            activeIndicator,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f)
        )

        val indicatorSlideAnimator = ObjectAnimator.ofFloat(
            activeIndicator, "translationY", -10f, 0f
        )
        indicatorSlideAnimator.interpolator = OvershootInterpolator(0.8f)

        animatorSet.playTogether(
            activeTextColorAnimator,
            inactiveTextColorAnimator,
            activeTextScaleAnimator,
            hideIndicatorAnimator,
            showIndicatorAnimator,
            indicatorSlideAnimator
        )

        animatorSet.duration = 350
        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        activeIndicator.visibility = View.VISIBLE

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {

                activeIndicator.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {

                inactiveIndicator.visibility = View.INVISIBLE
                isAnimating = false

                activeIndicator.translationY = 0f
                activeTextView.scaleX = 1f
                activeTextView.scaleY = 1f

                activeTextView.setTypeface(activeTextView.typeface, android.graphics.Typeface.BOLD)
                inactiveTextView.setTypeface(inactiveTextView.typeface, android.graphics.Typeface.NORMAL)
            }
        })

        animatorSet.start()
    }

    private fun onEvent(event: MyRatingUIEvent) {
        val action = when (event) {
            is MyRatingUIEvent.MovieEvent -> {
                MyRatingsFragmentDirections.actionRatedMoviesFragmentToMovieDetailFragment(event.movieID)
            }
            is MyRatingUIEvent.TVShowEvent -> {
                MyRatingsFragmentDirections.actionRatedMoviesFragmentToTvShowDetailsFragment(event.tvShowID)
            }
        }
        findNavController().navigate(action)
    }
}