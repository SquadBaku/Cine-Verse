package com.karrar.movieapp.ui.match

import com.karrar.movieapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(

) : BaseViewModel() {

    init {
        getData()
    }

    override fun getData() {

    }

}
