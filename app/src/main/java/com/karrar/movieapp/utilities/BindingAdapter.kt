package com.karrar.movieapp.utilities

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.enums.MediaType
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.category.CategoryInteractionListener
import com.karrar.movieapp.ui.category.GenreAdapter
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.ui.category.uiState.GenreUIState
import com.karrar.movieapp.utilities.Constants.FIRST_CATEGORY_ID
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@BindingAdapter("app:handelHomeRecycler")
fun handelHomeRecycler(view: RecyclerView, homeRecycler: String) {
    val overlapOffset = 80

    view.apply {
        clipToPadding = false
        clipChildren = false
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setPadding(overlapOffset, 0, overlapOffset, 0)
    }

    view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
            val center = recyclerView.width / 2

            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i) ?: continue
                val childCenter = (child.left + child.right) / 2
                val distanceFromCenter = (center - childCenter).toFloat()
                val ratio = kotlin.math.abs(distanceFromCenter) / center

                val scaleY = 1f + ratio * 0.1f
                child.scaleY = scaleY
                child.scaleX = 0.9f

                val alpha = 0.6f + (1 - ratio) * 0.4f
                child.alpha = alpha

                child.translationX = distanceFromCenter * 0.2f

                child.translationY = ratio * 60f

                child.translationZ = 1 - ratio

                val movieTitle = child.findViewById<TextView>(R.id.text_movie_title)
                val movieCategory = child.findViewById<TextView>(R.id.text_category)

                val textAlpha = 1 - ratio
                movieTitle?.alpha = textAlpha
                movieCategory?.alpha = textAlpha
            }
        }
    })
}

@BindingAdapter("app:showWhenListNotEmpty")
fun <T> showWhenListNotEmpty(view: View, list: List<T>) {
    view.isVisible = list.isNotEmpty() == true
}

@BindingAdapter("app:showWhenListEmpty")
fun <T> showWhenListEmpty(view: View, list: List<T>) {
    view.isVisible = list.isEmpty() == true
}

@BindingAdapter("app:hideWhenListIsEmpty")
fun <T> hideWhenListIsEmpty(view: View, list: List<T>?) {
    if (list?.isEmpty() == true) {
        view.visibility = View.INVISIBLE
    }
}

@BindingAdapter(value = ["app:error", "app:loading"])
fun <T> showWhenSuccess(view: View, error: List<T>?, loading: Boolean) {
    view.isVisible = error?.isEmpty() == true && !loading
}

@BindingAdapter(value = ["app:noError", "app:doneLoad", "app:emptyData"])
fun <T, M> showWhenNoData(view: View, error: List<T>?, loading: Boolean, data: List<M>?) {
    view.isVisible = error.isNullOrEmpty() && !loading && data.isNullOrEmpty()
}

@BindingAdapter(value = ["app:errorNotEmpty", "app:doneLoading"])
fun <T> hidWhenFail(view: View, error: List<T>?, loading: Boolean) {
    view.visibility = if (!error.isNullOrEmpty() && !loading) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("app:isListEmpty")
fun showWhenDoneLoadingAndListIsEmpty(view: View, emptyList: Boolean) {
    view.isVisible = emptyList
}

@BindingAdapter(value = ["app:showWhenNoInternet"])
fun showWhenNoInternet(view: View, error: List<ErrorUIState>) {
    view.isVisible = !error.none { it.code != ErrorUI.NEED_LOGIN }
}

@BindingAdapter(value = ["app:showWhenNoLogin"])
fun showWhenNoLogin2(view: View, error: List<ErrorUIState>) {
    view.isVisible = !error.none { it.code == ErrorUI.NEED_LOGIN }
}

@BindingAdapter("app:showWhenNoLoggedIn")
fun showWhenNoLoggedIn(view: View, isLoggedIn: Boolean) {
    view.isVisible = !isLoggedIn
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("app:hideIfTrue")
fun hideIfTrue(view: View, value: Boolean) {
    view.isVisible = !value
}

@BindingAdapter("app:isLoggedIn", "app:isFail")
fun showWhenLoggedInAndFail(view: View, isLoggedIn: Boolean, isFail: Boolean) {
    if (isLoggedIn && isFail) {
        view.isVisible = true
    } else if (isLoggedIn) {
        view.isVisible = false
    }
}

@BindingAdapter("isLogged", "isFailure")
fun showWhenIsLoggedInWithoutFail(view: View, isLoggedIn: Boolean, isFail: Boolean) {
    if (isLoggedIn && !isFail) {
        view.isVisible = true
    } else if (isFail) {
        view.isVisible = false
    }
}

//Search
@BindingAdapter(value = ["app:showWhenSearch"])
fun showWhenSearch(view: View, text: String) {
    view.isVisible = text.isNotBlank()
}

@BindingAdapter(value = ["app:hideWhenSearch"])
fun hideWhenSearch(view: View, text: String) {
    view.isVisible = text.isBlank()
}


@BindingAdapter("app:hideWhileTyping")
fun hideWhileTyping(view: View, text: String?) {
    (view.getTag() as? Runnable)?.let { view.removeCallbacks(it) }

    if (!text.isNullOrBlank()) {
        view.isVisible = false

        val showRunnable = Runnable { view.isVisible = true }
        view.postDelayed(showRunnable, 1500)

        view.setTag(showRunnable)
    } else {
        view.isVisible = false
    }
}


@BindingAdapter(value = ["app:showWhileTyping"])
fun showWhileTyping(view: View, text: String?) {
    (view.getTag() as? Runnable)?.let { view.removeCallbacks(it) }
    if (!text.isNullOrBlank()) {
        view.isVisible = true
        val hideRunnable = Runnable { view.isVisible = false }
        view.postDelayed(hideRunnable, 1500)
        view.setTag(hideRunnable)
    } else {
        view.isVisible = false
    }
}

@BindingAdapter(value = ["app:hideWhenBlankSearch"])
fun hideWhenBlankSearch(view: View, text: String) {
    if (text.isBlank()) {
        view.visibility = View.INVISIBLE
    }
}


@BindingAdapter(value = ["app:searchInput", "app:errorSearch", "app:loadingSearch"])
fun <T> hideWhenSuccessSearch(view: View, text: String, error: List<T>?, loading: Boolean) {
    view.visibility = if (text.isNotBlank() && error.isNullOrEmpty() && !loading) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

// different

@BindingAdapter(value = ["app:items"])
fun <T> setRecyclerItems(view: RecyclerView, items: List<T>?) {
    (view.adapter as BaseAdapter<T>?)?.setItems(items ?: emptyList())
    view.scrollToPosition(0)
}


@BindingAdapter(value = ["app:usePagerSnapHelper"])
fun usePagerSnapHelperWithRecycler(recycler: RecyclerView, useSnapHelper: Boolean = false) {
    if (useSnapHelper)
        PagerSnapHelper().attachToRecyclerView(recycler)
}

@BindingAdapter("app:posterImage")
fun bindMovieImage(image: ImageView, imageURL: String?) {
    imageURL?.let {
        image.load(imageURL) {
            placeholder(R.drawable.loading)
            error(R.drawable.ic_profile_place_holder)
        }
    }
}

@BindingAdapter("app:mediaPoster")
fun loadMediaPoster(image: ImageView, imageURL: String?) {
    imageURL?.let {
        image.load(imageURL) {
            placeholder(R.drawable.loading)
            error(R.drawable.media_place_holder)
        }
    }
}

@BindingAdapter("app:showProfileWhenSuccess")
fun showWhenProfileSuccess(view: View, userName: String) {
    view.isVisible = userName.isNotEmpty()
}

@BindingAdapter("app:overviewText")
fun setOverViewText(view: TextView, text: String) {
    if (text.isNotEmpty()) {
        view.text = text
    } else {
        view.text = view.context.getString(R.string.empty_overview_text)
    }
}

@BindingAdapter("app:setVideoId")
fun setVideoId(view: YouTubePlayerView, videoId: String?) {
    view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            videoId?.let { youTubePlayer.cueVideo(it, 0f) }
        }
    })
}

@BindingAdapter("app:setReleaseDate")
fun setReleaseDate(text: TextView, date: String?) {
    text.text = date?.take(4)
}

@BindingAdapter("app:convertToHoursPattern")
fun convertToHoursPattern(view: TextView, duration: Int) {
    duration.let {
        val hours = (duration / 60).toString()
        val minutes = (duration % 60).toString()
        if (hours == "0") {
            view.text = view.context.getString(R.string.minutes_pattern, minutes)
        } else if (minutes == "0") {
            view.text = view.context.getString(R.string.hours_pattern, hours)
        } else {
            view.text = view.context.getString(R.string.hours_minutes_pattern, hours, minutes)
        }
    }
}

@BindingAdapter(value = ["app:movieHours", "app:movieMinutes"])
fun setDuration(view: TextView, hours: Int?, minutes: Int?) {
    if (hours == 0) {
        view.text = String.format(view.context.getString(R.string.minutes_pattern), minutes)
    } else if (minutes == 0) {
        view.text = String.format(view.context.getString(R.string.hours_pattern), hours)
    } else {
        view.text =
            String.format(view.context.getString(R.string.hours_minutes_pattern), hours, minutes)
    }
}

@BindingAdapter("app:setGenres", "app:listener", "app:selectedChip")
fun <T> setGenresChips(
    view: ChipGroup, chipList: List<GenreUIState>?, listener: T,
    selectedChip: Int?
) {
    chipList?.let {
        it.forEach { genre -> view.addView(view.createChip(genre, listener)) }
    }
    val index = chipList?.indexOf(chipList.find { it.genreID == selectedChip }) ?: FIRST_CATEGORY_ID
    view.getChildAt(index)?.id?.let { view.check(it) }
}

@BindingAdapter("app:genre")
fun setAllGenre(textView: TextView, genreList: List<String>?) {
    genreList?.let {
        textView.text = genreList.joinToString(" . ") { it }
    }
}

@BindingAdapter("app:hideIfNotTypeOfMovie")
fun hideIfNotTypeOfMovie(view: View, mediaType: MediaType?) {
    if (mediaType != MediaType.MOVIE) view.isVisible = false
}

@BindingAdapter("android:rating")
fun setRating(view: RatingBar?, rating: Float) {
    view?.let {
        view.rating = rating
    }
}

@BindingAdapter("showWhenTextNotEmpty")
fun <T> showWhenTextNotEmpty(view: View, text: String) {
    view.isVisible = text.isNotEmpty()
}

@BindingAdapter("setGenres", "selectedChip", "listener", requireAll = true)
fun RecyclerView.setGenres(
    genres: List<GenreUIState>?,
    selectedChip: Int?,
    listener: CategoryInteractionListener?
) {
    if (adapter == null && listener != null) {
        adapter = GenreAdapter(listener)
    }

    val genreAdapter = adapter as? GenreAdapter
    if (genres != null && listener != null) {
        genreAdapter?.submitList(genres, selectedChip ?: -1)
    }
}


@BindingAdapter("icon")
fun setButtonIcon(button: MaterialButton, icon: Int) {
    if (icon != 0) {
        button.icon = ContextCompat.getDrawable(button.context, icon)
    } else {
        button.icon = null
    }
}

@BindingAdapter("adapterRecycler")
fun bindRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>?
) {
    recyclerView.adapter = adapter
}

@BindingAdapter("app:adapterRecycler")
fun bindRecyclerAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@BindingAdapter("layoutWidth", "layoutHeight", requireAll = false)
fun setLayoutSize(view: View, width: Float?, height: Float?) {
    val params = view.layoutParams
    width?.let { params.width = it.toInt() }
    height?.let { params.height = it.toInt() }
    view.layoutParams = params
}