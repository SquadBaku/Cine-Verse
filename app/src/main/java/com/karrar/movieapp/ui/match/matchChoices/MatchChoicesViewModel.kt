package com.karrar.movieapp.ui.match.matchChoices

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.GetMatchMovieListUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.match.MatchFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchChoicesViewModel @Inject constructor(
    private val getMatchMovieListUseCase : GetMatchMovieListUseCase
) : BaseViewModel() {

    fun getMatchMovie(
        genre: List<String>,
        time: String,
        classicOrRecent: String,
    ) {
        val withRuntimeGte : Int? = getWithRunTimeGte(time)
        val withRuntimeLte : Int?= getWithRunTimeLte(time)
        val primaryReleaseDateGte: String? = getPrimaryReleaseDateGte(classicOrRecent)
        val primaryReleaseDateLte : String?= getPrimaryReleaseDateLte(classicOrRecent)

        val action = MatchFragmentDirections.actionMatchFragmentToMatchChoicesFragment()

        viewModelScope.launch {
            try {
               val result =  getMatchMovieListUseCase(
                    genres = genre,
                    withRuntimeGte = withRuntimeGte,
                    withRuntimeLte = withRuntimeLte,
                    primaryReleaseDateGte = primaryReleaseDateGte,
                    primaryReleaseDateLte = primaryReleaseDateLte
                )


            } catch (th: Throwable) {

            }
        }
    }

    private fun getPrimaryReleaseDateLte(classicOrRecent: String): String? {
        return when (classicOrRecent) {
            "Classic" -> {
                "2000-01-01"
            }
            "Recent" -> {
                null
            }
            else -> {
                null
            }
        }
    }

    private fun getPrimaryReleaseDateGte(classicOrRecent: String): String? {
        return when (classicOrRecent) {
            "Classic" -> {
                null
            }
            "Recent" -> {
                "2023-01-01"
            }
            else -> {
                null
            }
        }
    }

    private fun getWithRunTimeLte(time : String ): Int? {
        return when (time) {
            "Short" -> {
                90
            }
            "Medium" -> {
                120
            }
            "long" -> {
                null
            }
            else -> {
                null
            }
        }
    }

    private fun getWithRunTimeGte(time : String ): Int? {
       return when (time) {
            "Short" -> {
                 null
            }
            "Medium" -> {
                 90
            }
            "long" -> {
                 120
            }
            else -> {
                null
            }
        }
    }

    init {
        getData()
    }

    override fun getData() {

    }

}
