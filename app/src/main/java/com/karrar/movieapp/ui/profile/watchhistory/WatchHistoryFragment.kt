package com.karrar.movieapp.ui.profile.watchhistory

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
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
    private var isItemSwiped = false
    private lateinit var adapter: WatchHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, getString(R.string.watch_history))

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        handleInterceptedRecyclerItemTouchEvent()

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

    fun handleInterceptedRecyclerItemTouchEvent() {
        binding.recyclerViewWatchHistory.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    val holder = rv.getChildViewHolder(child) as BaseAdapter.BaseViewHolder
                    val trashView = adapter.deleteViewOf(holder)
                    val rect = Rect()
                    trashView?.getGlobalVisibleRect(rect)
                    if (rect.contains(e.rawX.toInt(), e.rawY.toInt()) && isItemSwiped) {
                        val item = adapter.getItemAt(holder.bindingAdapterPosition)
                        viewModel.onSwipeDelete(item)
                        isItemSwiped = false
//                        trashView?.performClick()
                        return true
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
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
                    val maxSwipe = -fg.width * 0.3f
                    if (isCurrentlyActive) {
                        val limitedDx = min(0f, dX)
                        if (isDragged.not())
                            fg.translationX = limitedDx
                        else if (dX < maxSwipe) {
                            val adjustedDx =
                                abs(dX / resources.displayMetrics.widthPixels) * maxSwipe
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
                                    isItemSwiped = true
                                    val trashIcon = adapter.deleteViewOf(viewHolder)
                                    trashIcon?.setOnClickListener {
                                        val pos = viewHolder.bindingAdapterPosition
                                        if (pos != RecyclerView.NO_POSITION) {
                                            adapter.removeAt(pos)
                                        }
                                    }
                                }
                                .start()

                        } else {
                            fg.animate()
                                .translationX(dX)
                                .withEndAction {
                                    fg.translationX = 0f
                                    isDragged = false
                                    isItemSwiped = false
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

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return defaultValue * 10
            }

            override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
                return defaultValue * 10
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                Log.d("Swipe", "onSwiped: $direction at pos: $pos")
                if (pos == RecyclerView.NO_POSITION) return
                val trashIcon = adapter.deleteViewOf(viewHolder as BaseAdapter.BaseViewHolder)
                trashIcon?.let {
                    trashIcon.setOnClickListener {
                        adapter.removeAt(pos)
                    }
                }

//                adapter.removeAt(pos)

//                viewModel.deleteHistory(item)
            }

        }

        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerViewWatchHistory)
    }
}
