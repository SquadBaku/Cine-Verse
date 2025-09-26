package com.karrar.movieapp.domain.enums

import com.karrar.movieapp.R

enum class HomeItemsType(val value :Int) {
    ON_THE_AIR(R.string.title_on_air),
    TRENDING(R.string.title_trending),
    NOW_STREAMING(R.string.title_recently_released),
    UPCOMING(R.string.title_upcoming),
    MYSTERY(R.string.title_mystery),
    ADVENTURE(R.string.title_adventure),
    RECENTLY_VIEWED(R.string.you_recently_viewed),
    COLLECTIONS(R.string.your_collections),
    TOP_RATED_MOVIE(R.string.title_top_rated_tv_show),
    MATCH_YOUR_VIBE(R.string.matches_your_vibe),
    NON(R.string.title_non)
}