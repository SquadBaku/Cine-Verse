package com.karrar.movieapp.ui.movieDetails

import android.view.View
import com.karrar.movieapp.ui.base.BaseInteractionListener

interface DetailInteractionListener :BaseInteractionListener {

    fun onclickBack()

    fun onClickSave()

    fun onClickPlayTrailer()

    fun onclickViewReviews()

    fun onClickRate()

    fun onClickEscButton(view: View)
}