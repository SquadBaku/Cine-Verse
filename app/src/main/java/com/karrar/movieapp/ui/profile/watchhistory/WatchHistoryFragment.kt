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
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min

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

        adapter = WatchHistoryAdapter(items = emptyList(), listener = viewModel)
        binding.recyclerViewWatchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWatchHistory.adapter = adapter

        attachSwipeToDelete()

        binding.emptyLayout.btnFindSomething.setOnClickListener {
            findNavController().navigate(
                R.id.action_watchHistoryFragment_to_exploringFragment
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.setItems(state.allMedia)
            }
        }

        collectEvent()
    }

    private fun collectEvent() {
        collectLast(viewModel.watchHistoryUIEvent) {
            it.getContentIfNotHandled()?.let { event -> onEvent(event) }
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

    private fun attachSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.60f

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val fg: View? = adapter.foregroundOf(viewHolder as BaseAdapter.BaseViewHolder)
                if (fg != null) {

                    val limitedDx = min(0f, dX)
                    fg.translationX = limitedDx

                    val width = fg.width.takeIf { it > 0 } ?: 1
                    val progress = min(1f, abs(limitedDx) / width.toFloat())
                    fg.alpha = 1f - (0.25f * progress)
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

                (adapter.foregroundOf(viewHolder as BaseAdapter.BaseViewHolder))?.apply {
                    translationX = 0f
                    alpha = 1f
                }
                super.clearView(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return

                val item = adapter.getItemAt(pos)

                adapter.removeAt(pos)

                viewModel.deleteHistory(item)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewWatchHistory)
    }
}
