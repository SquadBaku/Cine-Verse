package com.karrar.movieapp.utilities

import android.view.View
import android.widget.ImageButton
import com.karrar.movieapp.R

enum class ViewMode { GRID, LIST }

fun setupViewModeToggle(root: View, initialMode: ViewMode, onModeChanged: (ViewMode) -> Unit) {
    val indicator = root.findViewById<View>(R.id.toggleIndicator)
    val btnGrid = root.findViewById<ImageButton>(R.id.btnGrid)
    val btnList = root.findViewById<ImageButton>(R.id.btnList)

    fun updateUI(mode: ViewMode) {
        val targetX = if (mode == ViewMode.GRID) 0f else indicator.width.toFloat()

        indicator.animate()
            .translationX(targetX)
            .setDuration(300)
            .start()

        btnGrid.setImageResource(if (mode == ViewMode.GRID) R.drawable.grid_view_active else R.drawable.grid_view_not_active)
        btnList.setImageResource(if (mode == ViewMode.LIST) R.drawable.list_view_active else R.drawable.list_view_not_active)
    }

    root.post { updateUI(initialMode) }

    btnGrid.setOnClickListener {
        updateUI(ViewMode.GRID)
        onModeChanged(ViewMode.GRID)
    }
    btnList.setOnClickListener {
        updateUI(ViewMode.LIST)
        onModeChanged(ViewMode.LIST)
    }
}