package com.uvarov.stylish.feature.productdetail.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailRoute(
    val productId: String,
) : NavKey
