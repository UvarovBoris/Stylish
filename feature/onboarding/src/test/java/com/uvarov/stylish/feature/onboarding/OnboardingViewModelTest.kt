package com.uvarov.stylish.feature.onboarding

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = OnboardingViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialUiState_containsDefaultPages() {
        val state = viewModel.uiState.value

        assertEquals(3, state.totalPages)
        assertEquals(defaultOnboardingPages, state.pages)
        assertFalse(state.pages.isEmpty())
    }

    @Test
    fun skipClickedIntent_emitsNavigateToAuthSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(OnboardingIntent.SkipClicked)
            testScheduler.advanceUntilIdle()
            assertEquals(OnboardingSideEffect.NavigateToAuth, awaitItem())
        }
    }

    @Test
    fun getStartedClickedIntent_emitsNavigateToAuthSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(OnboardingIntent.StartClicked)
            testScheduler.advanceUntilIdle()
            assertEquals(OnboardingSideEffect.NavigateToAuth, awaitItem())
        }
    }
}
