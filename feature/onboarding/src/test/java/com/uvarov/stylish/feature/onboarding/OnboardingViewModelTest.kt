package com.uvarov.stylish.feature.onboarding

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OnboardingViewModelTest {

    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setUp() {
        viewModel = OnboardingViewModel()
    }

    @Test
    fun initialUiState_hasDefaultValues() {
        val state = viewModel.uiState.value

        assertEquals(0, state.currentPage)
        assertEquals(3, state.totalPages)
        assertTrue(state.isFirstPage)
        assertFalse(state.isLastPage)
        assertFalse(state.isCompleted)
    }

    @Test
    fun onPageChanged_updatesCurrentPage() = runTest {
        viewModel.uiState.test {
            assertEquals(0, awaitItem().currentPage)

            viewModel.onPageChanged(1)
            assertEquals(1, awaitItem().currentPage)

            viewModel.onPageChanged(2)
            val lastPage = awaitItem()
            assertEquals(2, lastPage.currentPage)
            assertTrue(lastPage.isLastPage)
        }
    }

    @Test
    fun onPageChanged_ignoresOutOfBoundsPage() {
        viewModel.onPageChanged(5)
        assertEquals(0, viewModel.uiState.value.currentPage)

        viewModel.onPageChanged(-1)
        assertEquals(0, viewModel.uiState.value.currentPage)
    }

    @Test
    fun onNextClicked_incrementsPage() = runTest {
        viewModel.uiState.test {
            assertEquals(0, awaitItem().currentPage)

            viewModel.onNextClicked()
            assertEquals(1, awaitItem().currentPage)

            viewModel.onNextClicked()
            assertEquals(2, awaitItem().currentPage)

            // On last page, next completes onboarding
            viewModel.onNextClicked()
            val completedState = awaitItem()
            assertTrue(completedState.isCompleted)
        }
    }

    @Test
    fun onPrevClicked_decrementsPage() = runTest {
        viewModel.onPageChanged(2)

        viewModel.uiState.test {
            assertEquals(2, awaitItem().currentPage)

            viewModel.onPrevClicked()
            assertEquals(1, awaitItem().currentPage)

            viewModel.onPrevClicked()
            assertEquals(0, awaitItem().currentPage)

            // At first page, prev does not go below 0
            viewModel.onPrevClicked()
            expectNoEvents()
        }
    }

    @Test
    fun onSkipClicked_marksCompleted() = runTest {
        viewModel.uiState.test {
            assertFalse(awaitItem().isCompleted)

            viewModel.onSkipClicked()
            assertTrue(awaitItem().isCompleted)
        }
    }

    @Test
    fun onGetStartedClicked_marksCompleted() = runTest {
        viewModel.uiState.test {
            assertFalse(awaitItem().isCompleted)

            viewModel.onGetStartedClicked()
            assertTrue(awaitItem().isCompleted)
        }
    }
}
