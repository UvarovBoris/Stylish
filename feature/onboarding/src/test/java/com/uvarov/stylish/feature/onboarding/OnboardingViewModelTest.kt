package com.uvarov.stylish.feature.onboarding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class OnboardingViewModelTest {

    @Test
    fun initialUiState_hasDefaultValues() {
        val viewModel = OnboardingViewModel()
        val state = viewModel.uiState.value

        assertEquals(0, state.currentPage)
        assertFalse(state.isLoading)
    }
}
