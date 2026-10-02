package com.uvarov.stylish.feature.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

sealed interface MainIntent {
    data class TabSelected(val tab: MainTab) : MainIntent
}

sealed interface MainSideEffect

@Immutable
data class MainUiState(
    val currentTab: MainTab = MainTab.HOME,
)

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<MainSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<MainSideEffect> = _sideEffect.receiveAsFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.TabSelected -> selectTab(intent.tab)
        }
    }

    private fun selectTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }
}
