package com.uvarov.stylish.feature.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.ProductRepository
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
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ProductDetailSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProductDetailSideEffect> = _sideEffect.receiveAsFlow()

    private var currentProductId: String = ""

    fun onIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> {
                val state = _uiState.value
                if (state is ProductDetailUiState.Success && state.product.id == intent.productId) {
                    return
                }
                currentProductId = intent.productId
                loadProduct(intent.productId)
            }
            is ProductDetailIntent.SelectSize -> {
                _uiState.update { state ->
                    if (state is ProductDetailUiState.Success) {
                        state.copy(selectedSize = intent.size)
                    } else {
                        state
                    }
                }
            }
            is ProductDetailIntent.ToggleDescription -> {
                _uiState.update { state ->
                    if (state is ProductDetailUiState.Success) {
                        state.copy(isDescriptionExpanded = !state.isDescriptionExpanded)
                    } else {
                        state
                    }
                }
            }
            is ProductDetailIntent.AddToCartClicked -> {
                emitSideEffect(ProductDetailSideEffect.ShowToast("Added to cart"))
            }
            is ProductDetailIntent.BuyNowClicked -> {
                emitSideEffect(ProductDetailSideEffect.ShowToast("Proceeding to checkout"))
            }
            is ProductDetailIntent.BackClicked -> {
                emitSideEffect(ProductDetailSideEffect.NavigateBack)
            }
            is ProductDetailIntent.CartClicked -> {
                emitSideEffect(ProductDetailSideEffect.NavigateToCart)
            }
            is ProductDetailIntent.RetryClicked -> {
                if (currentProductId.isNotBlank()) {
                    loadProduct(currentProductId)
                }
            }
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { ProductDetailUiState.Loading }
            productRepository.getProductById(productId)
                .catch { throwable ->
                    _uiState.update {
                        ProductDetailUiState.Error(
                            message = throwable.message ?: "Failed to load product"
                        )
                    }
                }
                .collect { product ->
                    _uiState.update {
                        ProductDetailUiState.Success(
                            product = product,
                        )
                    }
                }
        }
    }

    private fun emitSideEffect(effect: ProductDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
