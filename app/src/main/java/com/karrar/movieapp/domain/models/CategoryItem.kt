package com.karrar.movieapp.domain.models

data class CategoryItem<T>(
    val item: T,
    val type: CategoryItemType
)
