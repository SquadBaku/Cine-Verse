package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchBinding
import com.karrar.movieapp.ui.base.BaseFragment


class MatchFragment : BaseFragment<FragmentMatchBinding>() {
    override val layoutIdFragment = R.layout.fragment_match
    override val viewModel: MatchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
    }
}