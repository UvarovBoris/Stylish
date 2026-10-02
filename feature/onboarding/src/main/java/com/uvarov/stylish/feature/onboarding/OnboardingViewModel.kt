package com.uvarov.stylish.feature.onboarding

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvarov.stylish.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface OnboardingIntent {
    data object SkipClicked : OnboardingIntent
    data object StartClicked : OnboardingIntent
}

sealed interface OnboardingSideEffect {
    data object NavigateToAuth : OnboardingSideEffect
}

@Immutable
data class OnboardingUiState(
    val pages: List<OnboardingPage> = defaultOnboardingPages
) {
    val totalPages: Int get() = pages.size
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<OnboardingSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<OnboardingSideEffect> = _sideEffect.receiveAsFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.SkipClicked -> completeOnboarding()
            OnboardingIntent.StartClicked -> completeOnboarding()
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            userDataRepository.setOnboardingCompleted(true)
            _sideEffect.send(OnboardingSideEffect.NavigateToAuth)
        }
    }
}
