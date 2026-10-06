package com.uvarov.stylish.feature.search

import androidx.compose.runtime.Immutable
import com.uvarov.stylish.core.model.Product

@Immutable
sealed interface SearchUiState {
    val query: String

    data class Loading(
        override val query: String = "",
    ) : SearchUiState

    data class Success(
        override val query: String,
        val products: List<Product>,
        val totalCount: Int = products.size,
    ) : SearchUiState

    data class Empty(
        override val query: String,
    ) : SearchUiState

    data class Error(
        override val query: String,
        val message: String,
    ) : SearchUiState
}

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data object SearchSubmitted : SearchIntent
    data object ClearQuery : SearchIntent
    data class ProductClicked(val product: Product) : SearchIntent
    data class ToggleFavorite(val productId: String) : SearchIntent
    data object RetryClicked : SearchIntent
    data object VoiceSearchClicked : SearchIntent
}

sealed interface SearchSideEffect {
    data class NavigateToProductDetails(val productId: String) : SearchSideEffect
    data class ShowToast(val message: String) : SearchSideEffect
}
