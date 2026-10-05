package com.uvarov.stylish.feature.wishlist

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.WishlistRepository
import com.uvarov.stylish.core.model.Product
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val wishlistRepository: WishlistRepository = mockk(relaxed = true)
    private lateinit var viewModel: WishlistViewModel

    private val sampleProducts = listOf(
        Product(
            id = "prod_1",
            title = "Kurta",
            description = "Desc",
            currentPrice = 1500,
            originalPrice = 2500,
            discountPercent = 40,
            rating = 4.5f,
            reviewCount = 100,
            imageUrl = "url1",
            isFavorite = true,
        ),
        Product(
            id = "prod_2",
            title = "Sneakers",
            description = "Desc",
            currentPrice = 2000,
            originalPrice = 4000,
            discountPercent = 50,
            rating = 4.8f,
            reviewCount = 200,
            imageUrl = "url2",
            isFavorite = true,
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { wishlistRepository.getFavoriteProducts() } returns flowOf(sampleProducts)
        viewModel = WishlistViewModel(wishlistRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsAndEmitsSuccessState() = runTest(testDispatcher) {
        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()
            val state = awaitItem() as WishlistUiState.Success
            assertEquals(sampleProducts, state.products)
        }
    }

    @Test
    fun loadWishlist_failure_emitsError() = runTest(testDispatcher) {
        every { wishlistRepository.getFavoriteProducts() } returns flow {
            throw RuntimeException("Database error")
        }
        val failingViewModel = WishlistViewModel(wishlistRepository)

        failingViewModel.uiState.test {
            assertEquals(WishlistUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            val state = awaitItem() as WishlistUiState.Error
            assertEquals("Database error", state.message)
        }
    }

    @Test
    fun productClicked_emitsNavigateToProductDetailsSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(WishlistIntent.ProductClicked("prod_1"))
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as WishlistSideEffect.NavigateToProductDetails
            assertEquals("prod_1", effect.productId)
        }
    }

    @Test
    fun toggleFavorite_callsWishlistRepository() = runTest(testDispatcher) {
        viewModel.onIntent(WishlistIntent.ToggleFavorite("prod_1"))
        testScheduler.advanceUntilIdle()

        coVerify { wishlistRepository.toggleFavorite("prod_1") }
    }

    @Test
    fun retryClicked_reloadsWishlist() = runTest(testDispatcher) {
        every { wishlistRepository.getFavoriteProducts() } returns flow {
            throw RuntimeException("Temporary error")
        }
        val retryViewModel = WishlistViewModel(wishlistRepository)
        testScheduler.advanceUntilIdle()

        every { wishlistRepository.getFavoriteProducts() } returns flowOf(sampleProducts)
        retryViewModel.uiState.test {
            assertTrue(awaitItem() is WishlistUiState.Error)
            retryViewModel.onIntent(WishlistIntent.RetryClicked)
            testScheduler.advanceUntilIdle()

            assertEquals(WishlistUiState.Loading, awaitItem())
            val success = awaitItem() as WishlistUiState.Success
            assertEquals(sampleProducts, success.products)
        }
    }
}
