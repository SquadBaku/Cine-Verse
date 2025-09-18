package com.karrar.movieapp.ui.explore

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karrar.movieapp.data.Constants
import com.karrar.movieapp.ui.category.CategoryFragment
import com.karrar.movieapp.utilities.Constants.MOVIE_CATEGORIES_ID
import com.karrar.movieapp.utilities.Constants.TV_CATEGORIES_ID

class ExplorePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CategoryFragment.newInstance(MOVIE_CATEGORIES_ID)
            else -> CategoryFragment.newInstance(TV_CATEGORIES_ID)
        }
    }

}