package com.uvarov.stylish.feature.productdetail

import androidx.compose.runtime.Immutable
import com.uvarov.stylish.core.model.Product

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState

    @Immutable
    data class Success(
        val product: Product,
        val selectedSize: String = "7 UK",
        val availableSizes: List<String> = listOf("6 UK", "7 UK", "8 UK", "9 UK", "10 UK"),
        val isDescriptionExpanded: Boolean = false,
    ) : ProductDetailUiState

    data class Error(
        val message: String,
    ) : ProductDetailUiState
}

sealed interface ProductDetailIntent {
    data class LoadProduct(val productId: String) : ProductDetailIntent
    data class SelectSize(val size: String) : ProductDetailIntent
    data object ToggleDescription : ProductDetailIntent
    data object AddToCartClicked : ProductDetailIntent
    data object BuyNowClicked : ProductDetailIntent
    data object BackClicked : ProductDetailIntent
    data object CartClicked : ProductDetailIntent
    data object RetryClicked : ProductDetailIntent
    data object ToggleFavorite : ProductDetailIntent
}

sealed interface ProductDetailSideEffect {
    data object NavigateBack : ProductDetailSideEffect
    data object NavigateToCart : ProductDetailSideEffect
    data class ShowToast(val message: String) : ProductDetailSideEffect
}
