package com.uvarov.stylish.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.ProductRepository
import com.uvarov.stylish.core.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading(query = ""))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SearchSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SearchSideEffect> = _sideEffect.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        performSearch(query = "", debounceMs = 0L)
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                _uiState.update { current ->
                    when (current) {
                        is SearchUiState.Loading -> current.copy(query = intent.query)
                        is SearchUiState.Success -> current.copy(query = intent.query)
                        is SearchUiState.Empty -> current.copy(query = intent.query)
                        is SearchUiState.Error -> current.copy(query = intent.query)
                    }
                }
                performSearch(query = intent.query, debounceMs = 300L)
            }

            is SearchIntent.SearchSubmitted -> {
                performSearch(query = _uiState.value.query, debounceMs = 0L)
            }

            is SearchIntent.ClearQuery -> {
                _uiState.update { current ->
                    when (current) {
                        is SearchUiState.Loading -> current.copy(query = "")
                        is SearchUiState.Success -> current.copy(query = "")
                        is SearchUiState.Empty -> current.copy(query = "")
                        is SearchUiState.Error -> current.copy(query = "")
                    }
                }
                performSearch(query = "", debounceMs = 0L)
            }

            is SearchIntent.ProductClicked -> {
                emitSideEffect(SearchSideEffect.NavigateToProductDetails(intent.product.id))
            }

            is SearchIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    wishlistRepository.toggleFavorite(intent.productId)
                }
            }

            is SearchIntent.RetryClicked -> {
                performSearch(query = _uiState.value.query, debounceMs = 0L)
            }

            is SearchIntent.VoiceSearchClicked -> {
                emitSideEffect(SearchSideEffect.ShowToast("Voice search activated"))
            }

            is SearchIntent.SortClicked -> {
                emitSideEffect(SearchSideEffect.ShowToast("Sort clicked"))
            }

            is SearchIntent.FilterClicked -> {
                emitSideEffect(SearchSideEffect.ShowToast("Filter clicked"))
            }
        }
    }

    private fun performSearch(query: String, debounceMs: Long) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (debounceMs > 0L) {
                delay(debounceMs)
            }
            _uiState.update { SearchUiState.Loading(query = query) }
            val queryParam = query.trim().ifEmpty { null }
            combine(
                productRepository.getProducts(query = queryParam),
                wishlistRepository.getFavoriteProductIds(),
            ) { products, favoriteIds ->
                products.map { product ->
                    product.copy(isFavorite = favoriteIds.contains(product.id))
                }
            }
                .catch { throwable ->
                    _uiState.update {
                        SearchUiState.Error(
                            query = query,
                            message = throwable.message ?: "Failed to search products",
                        )
                    }
                }
                .collect { products ->
                    _uiState.update {
                        if (products.isEmpty() && queryParam != null) {
                            SearchUiState.Empty(query = query)
                        } else {
                            SearchUiState.Success(
                                query = query,
                                products = products,
                                totalCount = products.size,
                            )
                        }
                    }
                }
        }
    }

    private fun emitSideEffect(effect: SearchSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
