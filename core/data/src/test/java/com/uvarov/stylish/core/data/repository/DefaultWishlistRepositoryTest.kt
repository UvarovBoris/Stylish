package com.uvarov.stylish.core.data.repository

import app.cash.turbine.test
import com.uvarov.stylish.core.database.dao.FavoriteProductDao
import com.uvarov.stylish.core.database.entity.FavoriteProductEntity
import com.uvarov.stylish.core.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultWishlistRepositoryTest {

    private val favoriteProductDao: FavoriteProductDao = mockk(relaxed = true)
    private val productRepository: ProductRepository = mockk()
    private lateinit var repository: DefaultWishlistRepository

    private val sampleProducts = listOf(
        Product(
            id = "prod_1",
            title = "Product 1",
            description = "Desc 1",
            currentPrice = 100,
            originalPrice = 200,
            discountPercent = 50,
            rating = 4.5f,
            reviewCount = 10,
            imageUrl = "url1"
        ),
        Product(
            id = "prod_2",
            title = "Product 2",
            description = "Desc 2",
            currentPrice = 200,
            originalPrice = 300,
            discountPercent = 33,
            rating = 4.0f,
            reviewCount = 5,
            imageUrl = "url2"
        ),
    )

    @Before
    fun setUp() {
        repository = DefaultWishlistRepository(favoriteProductDao, productRepository)
    }

    @Test
    fun getFavoriteProductIds_emitsSetOfIds() = runTest {
        every { favoriteProductDao.getFavoriteProductIds() } returns flowOf(listOf("prod_1", "prod_2"))

        repository.getFavoriteProductIds().test {
            val ids = awaitItem()
            assertEquals(setOf("prod_1", "prod_2"), ids)
            awaitComplete()
        }
    }

    @Test
    fun isFavorite_emitsDaoValue() = runTest {
        every { favoriteProductDao.isFavorite("prod_1") } returns flowOf(true)
        every { favoriteProductDao.isFavorite("prod_2") } returns flowOf(false)

        repository.isFavorite("prod_1").test {
            assertTrue(awaitItem())
            awaitComplete()
        }

        repository.isFavorite("prod_2").test {
            assertFalse(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getFavoriteProducts_combinesDaoIdsAndProducts() = runTest {
        every { favoriteProductDao.getFavoriteProductIds() } returns flowOf(listOf("prod_2"))
        every { productRepository.getProducts() } returns flowOf(sampleProducts)

        repository.getFavoriteProducts().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("prod_2", favorites[0].id)
            assertTrue(favorites[0].isFavorite)
            awaitComplete()
        }
    }

    @Test
    fun toggleFavorite_whenCurrentlyFavorite_deletesFromDao() = runTest {
        every { favoriteProductDao.isFavorite("prod_1") } returns flowOf(true)

        repository.toggleFavorite("prod_1")

        coVerify { favoriteProductDao.deleteFavorite("prod_1") }
    }

    @Test
    fun toggleFavorite_whenNotFavorite_insertsIntoDao() = runTest {
        every { favoriteProductDao.isFavorite("prod_1") } returns flowOf(false)

        repository.toggleFavorite("prod_1")

        coVerify {
            favoriteProductDao.insertFavorite(match { it.productId == "prod_1" })
        }
    }

    @Test
    fun addFavorite_insertsIntoDao() = runTest {
        repository.addFavorite("prod_3")

        coVerify {
            favoriteProductDao.insertFavorite(match { it.productId == "prod_3" })
        }
    }

    @Test
    fun removeFavorite_deletesFromDao() = runTest {
        repository.removeFavorite("prod_3")

        coVerify {
            favoriteProductDao.deleteFavorite("prod_3")
        }
    }
}
