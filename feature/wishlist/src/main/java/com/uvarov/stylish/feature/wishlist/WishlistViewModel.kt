package com.uvarov.stylish.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.WishlistRepository
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
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WishlistUiState>(WishlistUiState.Loading)
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<WishlistSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<WishlistSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadWishlist()
    }

    fun onIntent(intent: WishlistIntent) {
        when (intent) {
            is WishlistIntent.LoadWishlist -> loadWishlist()
            is WishlistIntent.ProductClicked -> {
                emitSideEffect(WishlistSideEffect.NavigateToProductDetails(intent.productId))
            }
            is WishlistIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    wishlistRepository.toggleFavorite(intent.productId)
                }
            }
            is WishlistIntent.RetryClicked -> loadWishlist()
        }
    }

    private fun loadWishlist() {
        viewModelScope.launch {
            _uiState.update { WishlistUiState.Loading }
            wishlistRepository.getFavoriteProducts()
                .catch { throwable ->
                    _uiState.update {
                        WishlistUiState.Error(
                            message = throwable.message ?: "Failed to load wishlist"
                        )
                    }
                }
                .collect { products ->
                    _uiState.update {
                        WishlistUiState.Success(products = products)
                    }
                }
        }
    }

    private fun emitSideEffect(effect: WishlistSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
