package com.uvarov.stylish.feature.search

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.ProductRepository
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
import kotlinx.coroutines.test.advanceTimeBy
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
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val productRepository: ProductRepository = mockk()
    private val wishlistRepository: WishlistRepository = mockk(relaxed = true)

    private val sampleProducts = listOf(
        Product(
            id = "p1",
            title = "Nike Sneakers",
            description = "Running shoes",
            currentPrice = 1900,
            originalPrice = 2500,
            discountPercent = 24,
            rating = 4.8f,
            reviewCount = 120,
            imageUrl = "url1",
            categoryId = "cat_shoes",
        ),
        Product(
            id = "p2",
            title = "Casual Dress",
            description = "Cotton dress",
            currentPrice = 999,
            originalPrice = 1500,
            discountPercent = 33,
            rating = 4.2f,
            reviewCount = 45,
            imageUrl = "url2",
            categoryId = "cat_womens",
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { wishlistRepository.getFavoriteProductIds() } returns flowOf(setOf("p1"))
        every { productRepository.getProducts(query = null) } returns flowOf(sampleProducts)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_loadsAllProductsWithFavoritesMapped() = runTest(testDispatcher) {
        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Success
            assertEquals("", state.query)
            assertEquals(2, state.products.size)
            assertTrue(state.products[0].isFavorite)
            assertEquals(false, state.products[1].isFavorite)
            assertEquals(2, state.totalCount)
        }
    }

    @Test
    fun queryChanged_debouncesAndSearchesProducts() = runTest(testDispatcher) {
        val searchResults = listOf(sampleProducts[0])
        every { productRepository.getProducts(query = "Nike") } returns flowOf(searchResults)

        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onIntent(SearchIntent.QueryChanged("Nike"))

            // Advance less than debounce duration (300ms)
            testScheduler.advanceTimeBy(100)
            // No search completed yet for "Nike"
            testScheduler.advanceTimeBy(250)
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Success
            assertEquals("Nike", state.query)
            assertEquals(1, state.products.size)
            assertEquals("Nike Sneakers", state.products[0].title)
        }
    }

    @Test
    fun queryChanged_noMatchingResults_emitsEmptyState() = runTest(testDispatcher) {
        every { productRepository.getProducts(query = "Nonexistent") } returns flowOf(emptyList())

        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onIntent(SearchIntent.QueryChanged("Nonexistent"))
            testScheduler.advanceTimeBy(350)
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Empty
            assertEquals("Nonexistent", state.query)
        }
    }

    @Test
    fun queryChanged_error_emitsErrorState() = runTest(testDispatcher) {
        every { productRepository.getProducts(query = "Error") } returns flow {
            throw RuntimeException("Network error")
        }

        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onIntent(SearchIntent.QueryChanged("Error"))
            testScheduler.advanceTimeBy(350)
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Error
            assertEquals("Error", state.query)
            assertEquals("Network error", state.message)
        }
    }

    @Test
    fun searchSubmitted_searchesImmediately() = runTest(testDispatcher) {
        every { productRepository.getProducts(query = "Instant") } returns flowOf(sampleProducts)

        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onIntent(SearchIntent.QueryChanged("Instant"))
            viewModel.onIntent(SearchIntent.SearchSubmitted)
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Success
            assertEquals("Instant", state.query)
            assertEquals(2, state.products.size)
        }
    }

    @Test
    fun clearQuery_resetsToEmptyQueryAndAllProducts() = runTest(testDispatcher) {
        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onIntent(SearchIntent.ClearQuery)
            testScheduler.advanceUntilIdle()

            val state = expectMostRecentItem() as SearchUiState.Success
            assertEquals("", state.query)
            assertEquals(2, state.products.size)
        }
    }

    @Test
    fun productClicked_emitsNavigateToProductDetailsSideEffect() = runTest(testDispatcher) {
        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onIntent(SearchIntent.ProductClicked(sampleProducts[0]))
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as SearchSideEffect.NavigateToProductDetails
            assertEquals("p1", effect.productId)
        }
    }

    @Test
    fun toggleFavorite_callsWishlistRepository() = runTest(testDispatcher) {
        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.onIntent(SearchIntent.ToggleFavorite("p1"))
        testScheduler.advanceUntilIdle()

        coVerify { wishlistRepository.toggleFavorite("p1") }
    }

    @Test
    fun voiceSearchClicked_emitsShowToast() = runTest(testDispatcher) {
        val viewModel = SearchViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
        testScheduler.advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onIntent(SearchIntent.VoiceSearchClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as SearchSideEffect.ShowToast
            assertEquals("Voice search activated", effect.message)
        }
    }
}
