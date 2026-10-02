package com.uvarov.stylish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.uvarov.stylish.core.data.repository.UserDataRepository
import com.uvarov.stylish.feature.main.navigation.MainRoute
import com.uvarov.stylish.feature.onboarding.navigation.OnboardingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val startDestination: NavKey) : MainActivityUiState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isOnboardingCompleted = userDataRepository.isOnboardingCompleted.first()
            val startDestination = if (isOnboardingCompleted) {
                MainRoute
            } else {
                OnboardingRoute
            }
            _uiState.value = MainActivityUiState.Success(startDestination = startDestination)
        }
    }
}
