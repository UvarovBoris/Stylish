package com.uvarov.stylish.feature.category.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRoute(
    val categoryId: String,
    val categoryTitle: String = "",
) : NavKey
