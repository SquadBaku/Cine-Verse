package com.karrar.movieapp.ui.match.matchPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchResultBinding
import com.karrar.movieapp.databinding.ItemMovieMatchPagerBinding
import com.karrar.movieapp.domain.models.MovieMatch
import com.karrar.movieapp.ui.base.BaseFragment


class MatchResultFragment : BaseFragment<FragmentMatchResultBinding>(

) {
    override val layoutIdFragment = R.layout.fragment_match_result
    override val viewModel: MatchPagerViewModel by viewModels()

    private val args: MatchResultFragmentArgs by navArgs()

    private lateinit var posterAdapter: PosterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val x = mutableListOf<MovieMatch>()

        for (i in 0 until args.movieNames.size) {
            x.add(
                MovieMatch(
                    movieId = args.moviesId.get(i).toInt(),
                    movieImage = args.moviesImage.get(i),
                    movieName = args.movieNames.get(i),
                    movieGenres = listOf(args.movieGenres.get(i)),
                    movieReleaseDate = args.releaseDate.get(i),
                    movieVoteAverage = args.voteAverage.get(i)
                )
            )
        }

        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.viewDetailsButton.setOnClickListener {

            val action =
                MatchResultFragmentDirections.actionMatchResultFragmentToMovieDetailFragment(
                    x[binding.posterViewPager.currentItem].movieId
                )
            findNavController().navigate(action)
        }

        posterAdapter = PosterAdapter(x)
        binding.posterViewPager.adapter = posterAdapter

        binding.posterViewPager.clipToPadding = false
        binding.posterViewPager.clipChildren = false
        binding.posterViewPager.offscreenPageLimit = 3
        binding.posterViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset)

        binding.posterViewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * pageOffset + pageMargin)
            if (binding.posterViewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.translationX = offset
            } else {
                page.translationY = offset
            }

            val scale = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
            page.scaleY = scale
            page.scaleX = scale
            page.alpha = 0.5f + (1 - kotlin.math.abs(position)) * 0.5f
            page.translationZ = 1 - kotlin.math.abs(position)
        }

        fun updateDetails(movie: MovieMatch) {
            binding.nameOfMovies.text = movie.movieName
            binding.category.text = movie.movieGenres.joinToString(", ")
            binding.ratingText.text = movie.movieVoteAverage
            binding.releaseText.text = movie.movieReleaseDate
        }



        updateDetails(x[0])

        binding.posterViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDetails(x[position])
            }
        })
    }

    class PosterAdapter(
        private val movies: List<MovieMatch>
    ) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

        inner class PosterViewHolder(val binding: ItemMovieMatchPagerBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
            val binding = ItemMovieMatchPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PosterViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
            val movie = movies[position]

            holder.binding.imageUrl = movie.movieImage
        }

        override fun getItemCount(): Int = movies.size
    }

}