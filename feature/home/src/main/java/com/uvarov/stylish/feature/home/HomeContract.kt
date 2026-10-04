package com.uvarov.stylish.feature.home

import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val homeFeed: HomeFeed,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

sealed interface HomeIntent {
    data object LoadHomeFeed : HomeIntent
    data object SearchBarClicked : HomeIntent
    data class CategoryClicked(val categoryId: String, val title: String? = null) : HomeIntent
    data class ProductClicked(val product: Product) : HomeIntent
    data class HeroBannerClicked(val banner: BannerItem) : HomeIntent
    data object VoiceSearchClicked : HomeIntent
}

sealed interface HomeSideEffect {
    data class NavigateToProductDetails(val productId: String) : HomeSideEffect
    data class NavigateToCatalog(val categoryId: String? = null, val title: String? = null) : HomeSideEffect
    data object NavigateToSearch : HomeSideEffect
    data class ShowToast(val message: String) : HomeSideEffect
}
