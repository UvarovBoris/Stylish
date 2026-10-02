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
import org.junit.Assert.assertTrue
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
    fun initialUiState_hasDefaultValues() {
        val state = viewModel.uiState.value

        assertEquals(0, state.currentPage)
        assertEquals(3, state.totalPages)
        assertTrue(state.isFirstPage)
        assertFalse(state.isLastPage)
    }

    @Test
    fun pageChangedIntent_updatesCurrentPage() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals(0, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.PageChanged(1))
            assertEquals(1, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.PageChanged(2))
            val lastPage = awaitItem()
            assertEquals(2, lastPage.currentPage)
            assertTrue(lastPage.isLastPage)
        }
    }

    @Test
    fun pageChangedIntent_ignoresOutOfBoundsPage() {
        viewModel.onIntent(OnboardingIntent.PageChanged(5))
        assertEquals(0, viewModel.uiState.value.currentPage)

        viewModel.onIntent(OnboardingIntent.PageChanged(-1))
        assertEquals(0, viewModel.uiState.value.currentPage)
    }

    @Test
    fun nextClickedIntent_incrementsPage() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals(0, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.NextClicked)
            assertEquals(1, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.NextClicked)
            assertEquals(2, awaitItem().currentPage)
        }
    }

    @Test
    fun nextClickedIntent_onLastPage_emitsNavigateToAuthSideEffect() = runTest(testDispatcher) {
        viewModel.onIntent(OnboardingIntent.PageChanged(2))

        viewModel.sideEffect.test {
            viewModel.onIntent(OnboardingIntent.NextClicked)
            testScheduler.advanceUntilIdle()
            assertEquals(OnboardingSideEffect.NavigateToAuth, awaitItem())
        }
    }

    @Test
    fun prevClickedIntent_decrementsPage() = runTest(testDispatcher) {
        viewModel.onIntent(OnboardingIntent.PageChanged(2))

        viewModel.uiState.test {
            assertEquals(2, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.PrevClicked)
            assertEquals(1, awaitItem().currentPage)

            viewModel.onIntent(OnboardingIntent.PrevClicked)
            assertEquals(0, awaitItem().currentPage)

            // At first page, prev does not go below 0
            viewModel.onIntent(OnboardingIntent.PrevClicked)
            expectNoEvents()
        }
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
            viewModel.onIntent(OnboardingIntent.GetStartedClicked)
            testScheduler.advanceUntilIdle()
            assertEquals(OnboardingSideEffect.NavigateToAuth, awaitItem())
        }
    }
}
