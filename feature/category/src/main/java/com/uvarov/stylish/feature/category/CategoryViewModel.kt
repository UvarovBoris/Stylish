package com.uvarov.stylish.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.ProductRepository
import com.uvarov.stylish.core.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<CategorySideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<CategorySideEffect> = _sideEffect.receiveAsFlow()

    private var currentCategoryId: String = ""
    private var currentCategoryTitle: String = ""

    fun onIntent(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.LoadCategory -> {
                val state = _uiState.value
                if (state is CategoryUiState.Success && state.categoryId == intent.categoryId) {
                    // Already loaded, do not reset to Loading
                    return
                }
                currentCategoryId = intent.categoryId
                currentCategoryTitle = intent.categoryTitle
                loadProducts(intent.categoryId, intent.categoryTitle)
            }
            is CategoryIntent.ProductClicked -> {
                emitSideEffect(CategorySideEffect.NavigateToProductDetails(intent.product.id))
            }
            is CategoryIntent.BackClicked -> {
                emitSideEffect(CategorySideEffect.NavigateBack)
            }
            is CategoryIntent.RetryClicked -> {
                if (currentCategoryId.isNotBlank()) {
                    loadProducts(currentCategoryId, currentCategoryTitle)
                }
            }
            is CategoryIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    wishlistRepository.toggleFavorite(intent.productId)
                }
            }
        }
    }

    private fun loadProducts(categoryId: String, categoryTitle: String) {
        viewModelScope.launch {
            _uiState.update { CategoryUiState.Loading }
            combine(
                productRepository.getProductsByCategory(categoryId),
                wishlistRepository.getFavoriteProductIds(),
            ) { products, favoriteIds ->
                products.map { product ->
                    product.copy(isFavorite = favoriteIds.contains(product.id))
                }
            }
                .catch { throwable ->
                    _uiState.update {
                        CategoryUiState.Error(
                            message = throwable.message ?: "Failed to load products"
                        )
                    }
                }
                .collect { products ->
                    _uiState.update {
                        CategoryUiState.Success(
                            categoryId = categoryId,
                            categoryTitle = categoryTitle,
                            products = products,
                        )
                    }
                }
        }
    }

    private fun emitSideEffect(effect: CategorySideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
