package com.karrar.movieapp.ui.profile.watchhistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentWatchHistoryBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchHistoryFragment : BaseFragment<FragmentWatchHistoryBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_watch_history
    override val viewModel: WatchHistoryViewModel by viewModels()

    private lateinit var adapter: WatchHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, getString(R.string.watch_history))

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = WatchHistoryAdapter(emptyList(), viewModel)
        binding.recyclerViewWatchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWatchHistory.adapter = adapter

        attachSwipeToDelete()

        binding.emptyLayout.btnFindSomething.setOnClickListener {
            findNavController().navigate(R.id.action_watchHistoryFragment_to_exploringFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.setItems(state.allMedia)
            }
        }

        collectEvent()
    }

    private fun attachSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val pos = vh.bindingAdapterPosition
                val item = adapter.getItemAt(pos)

                adapter.removeAt(pos)

                viewModel.deleteHistory(item)
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerViewWatchHistory)
    }

    private fun collectEvent() {
        collectLast(viewModel.watchHistoryUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: WatchHistoryUIEvent) {
        val action = when (event) {
            is WatchHistoryUIEvent.MovieEvent ->
                WatchHistoryFragmentDirections
                    .actionWatchHistoryFragmentToMovieDetailFragment(event.movieID)
            is WatchHistoryUIEvent.TVShowEvent ->
                WatchHistoryFragmentDirections
                    .actionWatchHistoryFragmentToTvShowDetailsFragment(event.tvShowID)
        }
        findNavController().navigate(action)
    }
}
