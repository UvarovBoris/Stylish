package com.uvarov.stylish.feature.onboarding

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface OnboardingIntent {
    data class PageChanged(val page: Int) : OnboardingIntent
    data object NextClicked : OnboardingIntent
    data object PrevClicked : OnboardingIntent
    data object SkipClicked : OnboardingIntent
    data object GetStartedClicked : OnboardingIntent
}

sealed interface OnboardingSideEffect {
    data object NavigateToAuth : OnboardingSideEffect
}

@Immutable
data class OnboardingUiState(
    val currentPage: Int = 0,
    val pages: List<OnboardingPage> = defaultOnboardingPages
) {
    val totalPages: Int get() = pages.size
    val isFirstPage: Boolean get() = currentPage == 0
    val isLastPage: Boolean get() = currentPage == totalPages - 1
}

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<OnboardingSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<OnboardingSideEffect> = _sideEffect.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged -> handlePageChanged(intent.page)
            OnboardingIntent.NextClicked -> handleNextClicked()
            OnboardingIntent.PrevClicked -> handlePrevClicked()
            OnboardingIntent.SkipClicked -> completeOnboarding()
            OnboardingIntent.GetStartedClicked -> completeOnboarding()
        }
    }

    private fun handlePageChanged(page: Int) {
        if (page in 0 until _uiState.value.totalPages) {
            _uiState.update { it.copy(currentPage = page) }
        }
    }

    private fun handleNextClicked() {
        val state = _uiState.value
        if (state.currentPage < state.totalPages - 1) {
            _uiState.update { it.copy(currentPage = it.currentPage + 1) }
        } else {
            completeOnboarding()
        }
    }

    private fun handlePrevClicked() {
        val state = _uiState.value
        if (state.currentPage > 0) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1) }
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            _sideEffect.send(OnboardingSideEffect.NavigateToAuth)
        }
    }
}
