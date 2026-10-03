package com.uvarov.stylish.feature.home

import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val homeFeed: HomeFeed,
        val selectedCategoryId: String? = null,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

sealed interface HomeIntent {
    data object LoadHomeFeed : HomeIntent
    data object SearchBarClicked : HomeIntent
    data class CategorySelected(val categoryId: String) : HomeIntent
    data class ProductClicked(val product: Product) : HomeIntent
    data object DealOfTheDayViewAllClicked : HomeIntent
    data object TrendingViewAllClicked : HomeIntent
    data object SpecialOffersClicked : HomeIntent
    data object HeelsBannerClicked : HomeIntent
    data object NewArrivalsClicked : HomeIntent
    data object SponsoredBannerClicked : HomeIntent
    data object VoiceSearchClicked : HomeIntent
}

sealed interface HomeSideEffect {
    data class NavigateToProductDetails(val productId: String) : HomeSideEffect
    data class NavigateToCatalog(val categoryId: String? = null, val title: String? = null) : HomeSideEffect
    data object NavigateToSearch : HomeSideEffect
    data class ShowToast(val message: String) : HomeSideEffect
}
