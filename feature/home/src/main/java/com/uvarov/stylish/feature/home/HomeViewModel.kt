package com.uvarov.stylish.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<HomeSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadHomeFeed()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadHomeFeed -> loadHomeFeed()
            is HomeIntent.SearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is HomeIntent.CategorySelected -> handleCategorySelected(intent.categoryId)
            is HomeIntent.ProductClicked -> emitSideEffect(
                HomeSideEffect.NavigateToProductDetails(intent.product.id)
            )
            is HomeIntent.DealOfTheDayViewAllClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "Deal of the Day")
            )
            is HomeIntent.TrendingViewAllClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "Trending Products")
            )
            is HomeIntent.SpecialOffersClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "Special Offers")
            )
            is HomeIntent.HeelsBannerClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "Flat and Heels")
            )
            is HomeIntent.NewArrivalsClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "New Arrivals")
            )
            is HomeIntent.SponsoredBannerClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = "Sponsored")
            )
            is HomeIntent.SortClicked -> emitSideEffect(HomeSideEffect.OpenSortSheet)
            is HomeIntent.FilterClicked -> emitSideEffect(HomeSideEffect.OpenFilterSheet)
            is HomeIntent.VoiceSearchClicked -> emitSideEffect(
                HomeSideEffect.ShowToast("Voice search activated")
            )
        }
    }

    private fun loadHomeFeed() {
        viewModelScope.launch {
            _uiState.update { HomeUiState.Loading }
            homeRepository.getHomeFeed()
                .catch { throwable ->
                    _uiState.update {
                        HomeUiState.Error(throwable.message ?: "Failed to load home feed")
                    }
                }
                .collect { feed ->
                    _uiState.update {
                        HomeUiState.Success(homeFeed = feed)
                    }
                }
        }
    }

    private fun handleSearchQueryChanged(query: String) {
        _uiState.update { current ->
            if (current is HomeUiState.Success) {
                current.copy(searchQuery = query)
            } else {
                current
            }
        }
    }

    private fun handleCategorySelected(categoryId: String) {
        _uiState.update { current ->
            if (current is HomeUiState.Success) {
                val newSelected = if (current.selectedCategoryId == categoryId) null else categoryId
                current.copy(selectedCategoryId = newSelected)
            } else {
                current
            }
        }
        emitSideEffect(HomeSideEffect.NavigateToCatalog(categoryId = categoryId))
    }

    private fun emitSideEffect(effect: HomeSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
