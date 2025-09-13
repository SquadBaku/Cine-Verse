package com.karrar.movieapp.ui.profile.myratings

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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

        collectLast(viewModel.myRatingUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }

        // مراقبة تغيير حالة التابات
        lifecycleScope.launch {
            viewModel.isMoviesTab.collect { isMoviesTab ->
                animateTabTransition(isMoviesTab)
            }
        }
    }

    private fun setupTabAnimations() {
        // إعداد النقرات مع منع النقرات المتعددة أثناء الأنيميشن
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

        // تحديد العناصر المراد تحريكها
        val activeTextView = if (isMoviesTab) binding.tvMovies else binding.tvSeries
        val inactiveTextView = if (isMoviesTab) binding.tvSeries else binding.tvMovies
        val activeIndicator = if (isMoviesTab) binding.indicatorMovies else binding.indicatorSeries
        val inactiveIndicator = if (isMoviesTab) binding.indicatorSeries else binding.indicatorMovies

        // أنيميشن تغيير اللون للنصوص
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), tertiaryColor, primaryColor)
        colorAnimator.duration = 300
        colorAnimator.interpolator = AccelerateDecelerateInterpolator()

        colorAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            val reverseValue = ArgbEvaluator().evaluate(
                1f - animator.animatedFraction,
                tertiaryColor,
                primaryColor
            ) as Int

            activeTextView.setTextColor(animatedValue)
            inactiveTextView.setTextColor(reverseValue)
        }

        // أنيميشن إظهار/إخفاء المؤشرات
        val showIndicatorAnimator = ValueAnimator.ofFloat(0f, 1f)
        val hideIndicatorAnimator = ValueAnimator.ofFloat(1f, 0f)

        showIndicatorAnimator.duration = 300
        hideIndicatorAnimator.duration = 300

        showIndicatorAnimator.interpolator = AccelerateDecelerateInterpolator()
        hideIndicatorAnimator.interpolator = AccelerateDecelerateInterpolator()

        showIndicatorAnimator.addUpdateListener { animator ->
            activeIndicator.alpha = animator.animatedValue as Float
            activeIndicator.scaleX = animator.animatedValue as Float
        }

        hideIndicatorAnimator.addUpdateListener { animator ->
            inactiveIndicator.alpha = animator.animatedValue as Float
            inactiveIndicator.scaleX = animator.animatedValue as Float
        }

        // أنيميشن تكبير وتصغير النص
        val scaleUpAnimator = ValueAnimator.ofFloat(1f, 1.1f, 1f)
        scaleUpAnimator.duration = 300
        scaleUpAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            activeTextView.scaleX = scale
            activeTextView.scaleY = scale
        }

        // بدء الأنيميشنات
        colorAnimator.start()
        showIndicatorAnimator.start()
        hideIndicatorAnimator.start()
        scaleUpAnimator.start()

        // إعداد حالة الرؤية للمؤشرات
        activeIndicator.visibility = View.VISIBLE
        inactiveIndicator.visibility = View.INVISIBLE

        // انتهاء الأنيميشن
        colorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
            }
        })
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