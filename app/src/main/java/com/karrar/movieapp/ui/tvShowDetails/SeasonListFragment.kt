package com.karrar.movieapp.ui.tvShowDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.karrar.movieapp.databinding.FragmentSeasonListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SeasonListFragment : Fragment() {

    private lateinit var binding: FragmentSeasonListBinding
    private val viewModel: TvShowDetailsViewModel by activityViewModels()
    private lateinit var seasonAdapter: SeasonAdapterUIState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeasonListBinding.inflate(inflater, container, false)

        seasonAdapter = SeasonAdapterUIState(
            items = emptyList(),
            listener = object : SeasonInteractionListener {
                override fun onClickSeason(seasonNumber: Int) {
                    // TODO: Handle click
                }
            }
        )

        binding.viewModel = viewModel
        binding.adapterRecycler = seasonAdapter
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collectLatest { state ->
                seasonAdapter.setItems(state.seriesSeasonsResult)
            }
        }
    }
}
