package com.karrar.movieapp.ui.profile.watchhistory

import android.os.Bundle
import android.util.Log
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
            private var isDragged = false
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.30f

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                val fg: View? = adapter.foregroundOf(viewHolder as BaseAdapter.BaseViewHolder)
                fg?.let {
                    val currentTranslation = fg.translationX
                    val maxSwipe = -fg.width * 0.3f
                    if (isCurrentlyActive) {
                        Log.d(
                            "Swipe",
                            "onChildDrawDX: $dX,deviceWidth:${resources.displayMetrics.widthPixels}"
                        )
                        Log.d("currentTranslation", "onChildDraw: $currentTranslation")
                        val limitedDx = min(0f, dX)
                        if (isDragged.not())
                            fg.translationX = limitedDx
                        else if (dX < maxSwipe) {
                            val adjustedDx = abs(dX / resources.displayMetrics.widthPixels) * maxSwipe
                            Log.d("adjustedDx","dx divided by device width: ${abs(dX / resources.displayMetrics.widthPixels)}")
                            Log.d("adjustedDx", "onChildDrawDX: $adjustedDx")
                            fg.translationX = min(0f, adjustedDx)
                        }
                    } else
                        if (dX < -fg.width * 0.3f) {
                            fg.animate()
                                .translationX(-fg.width * 0.3f)
                                .withEndAction {
                                    Log.d("Swipe", "onChildDraw: $dX")
                                    fg.translationX = -fg.width * 0.3f
                                    isDragged = true
                                }
                                .start()

                        } else {
                            fg.animate()
                                .translationX(dX)
                                .withEndAction {
                                    fg.translationX = 0f
                                    isDragged = false
                                }
                                .start()
                        }
                } ?: {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

//            override fun clearView(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//            ) {
//                val fg = adapter.foregroundOf(viewHolder as BaseAdapter.BaseViewHolder)
//                fg?.let {
//                    val maxSwipe = -fg.width * 0.3f
//                    Log.d("Swipe", "clearView: ${fg.translationX}")
//                    if (fg.translationX <= maxSwipe) {
//                        fg.animate()
//                            .translationX(maxSwipe)
//                            .setDuration(200)
//                            .withEndAction { }
//                            .start()
//                    } else {
//                        fg.animate().translationX(0f).setDuration(200).start()
//                    }
//                }
//            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                Log.d("Swipe", "onSwiped: $direction at pos: $pos")
                if (pos == RecyclerView.NO_POSITION) return
                val foreground = adapter.foregroundOf(viewHolder as BaseAdapter.BaseViewHolder)

                val item = adapter.getItemAt(pos)

//                adapter.removeAt(pos)

//                viewModel.deleteHistory(item)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewWatchHistory)
    }
}
