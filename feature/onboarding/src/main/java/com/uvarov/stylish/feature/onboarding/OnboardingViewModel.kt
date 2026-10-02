package com.uvarov.stylish.feature.onboarding

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class OnboardingUiState(
    val currentPage: Int = 0,
    val pages: List<OnboardingPage> = defaultOnboardingPages,
    val isCompleted: Boolean = false
) {
    val totalPages: Int get() = pages.size
    val isFirstPage: Boolean get() = currentPage == 0
    val isLastPage: Boolean get() = currentPage == totalPages - 1
}

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onPageChanged(page: Int) {
        if (page in 0 until _uiState.value.totalPages) {
            _uiState.update { it.copy(currentPage = page) }
        }
    }

    fun onNextClicked() {
        val state = _uiState.value
        if (state.currentPage < state.totalPages - 1) {
            _uiState.update { it.copy(currentPage = it.currentPage + 1) }
        } else {
            completeOnboarding()
        }
    }

    fun onPrevClicked() {
        val state = _uiState.value
        if (state.currentPage > 0) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1) }
        }
    }

    fun onSkipClicked() {
        completeOnboarding()
    }

    fun onGetStartedClicked() {
        completeOnboarding()
    }

    private fun completeOnboarding() {
        _uiState.update { it.copy(isCompleted = true) }
    }
}
