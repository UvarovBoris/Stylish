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
            is HomeIntent.SearchBarClicked -> emitSideEffect(HomeSideEffect.NavigateToSearch)
            is HomeIntent.CategoryClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(categoryId = intent.categoryId, title = intent.title)
            )
            is HomeIntent.ProductClicked -> emitSideEffect(
                HomeSideEffect.NavigateToProductDetails(intent.product.id)
            )
            is HomeIntent.HeroBannerClicked -> emitSideEffect(
                HomeSideEffect.NavigateToCatalog(title = intent.banner.title)
            )
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

    private fun emitSideEffect(effect: HomeSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
