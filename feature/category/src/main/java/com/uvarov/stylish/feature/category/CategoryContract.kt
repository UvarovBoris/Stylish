package com.uvarov.stylish.feature.category

import androidx.compose.runtime.Immutable
import com.uvarov.stylish.core.model.Product

sealed interface CategoryUiState {
    data object Loading : CategoryUiState

    @Immutable
    data class Success(
        val categoryId: String,
        val categoryTitle: String,
        val products: List<Product>,
    ) : CategoryUiState

    data class Error(
        val message: String,
    ) : CategoryUiState
}

sealed interface CategoryIntent {
    data class LoadCategory(val categoryId: String, val categoryTitle: String) : CategoryIntent
    data class ProductClicked(val product: Product) : CategoryIntent
    data object BackClicked : CategoryIntent
    data object RetryClicked : CategoryIntent
}

sealed interface CategorySideEffect {
    data class NavigateToProductDetails(val productId: String) : CategorySideEffect
    data object NavigateBack : CategorySideEffect
    data class ShowToast(val message: String) : CategorySideEffect
}
