package com.karrar.movieapp.ui.match.matchChoices

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchChoicesBinding
import com.karrar.movieapp.ui.base.BaseFragment

class MatchChoicesFragment : BaseFragment<FragmentMatchChoicesBinding>() {
    override val layoutIdFragment = R.layout.fragment_match_choices
    override val viewModel: MatchChoicesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
    }

}