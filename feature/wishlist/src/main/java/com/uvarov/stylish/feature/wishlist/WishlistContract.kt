package com.uvarov.stylish.feature.wishlist

import androidx.compose.runtime.Immutable
import com.uvarov.stylish.core.model.Product

sealed interface WishlistUiState {
    data object Loading : WishlistUiState

    @Immutable
    data class Success(
        val products: List<Product>,
    ) : WishlistUiState

    data class Error(
        val message: String,
    ) : WishlistUiState
}

sealed interface WishlistIntent {
    data object LoadWishlist : WishlistIntent
    data class ProductClicked(val productId: String) : WishlistIntent
    data class ToggleFavorite(val productId: String) : WishlistIntent
    data object RetryClicked : WishlistIntent
}

sealed interface WishlistSideEffect {
    data class NavigateToProductDetails(val productId: String) : WishlistSideEffect
}
