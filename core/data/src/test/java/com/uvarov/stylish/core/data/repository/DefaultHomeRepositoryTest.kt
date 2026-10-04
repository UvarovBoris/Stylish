package com.uvarov.stylish.core.data.repository

import app.cash.turbine.test
import com.uvarov.stylish.core.network.api.StylishApiService
import com.uvarov.stylish.core.network.model.BannerItemDto
import com.uvarov.stylish.core.network.model.CategoryDto
import com.uvarov.stylish.core.network.model.HomeFeedResponseDto
import com.uvarov.stylish.core.network.model.ProductDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultHomeRepositoryTest {

    private val apiService: StylishApiService = mockk()
    private lateinit var repository: DefaultHomeRepository

    @Before
    fun setUp() {
        repository = DefaultHomeRepository(apiService)
    }

    @Test
    fun getHomeFeed_emitsMappedDomainHomeFeed() = runTest {
        val mockResponse = HomeFeedResponseDto(
            categories = listOf(
                CategoryDto("cat_1", "Beauty", "https://api.stylish.app/images/cat_beauty.png"),
                CategoryDto("cat_gifts", "Gifts", "https://api.stylish.app/images/cat_gifts.png")
            ),
            heroBanners = listOf(
                BannerItemDto("banner_1", "50% OFF", "All colors", "Shop Now", "https://api.stylish.app/images/banner_hero.png")
            ),
            products = listOf(
                ProductDto(
                    id = "prod_1",
                    title = "Women Printed Kurta",
                    description = "Description",
                    currentPrice = 1500,
                    originalPrice = 2499,
                    discountPercent = 40,
                    rating = 4.0f,
                    reviewCount = 56890,
                    imageUrl = "https://api.stylish.app/images/kurta.jpg"
                )
            )
        )

        coEvery { apiService.getHomeFeed() } returns mockResponse

        repository.getHomeFeed().test {
            val feed = awaitItem()
            assertEquals(2, feed.categories.size)
            assertEquals("Beauty", feed.categories[0].name)
            assertEquals("Gifts", feed.categories[1].name)
            assertEquals("https://api.stylish.app/images/cat_gifts.png", feed.categories[1].imageUrl)
            assertEquals(1, feed.heroBanners.size)
            assertEquals("50% OFF", feed.heroBanners[0].title)
            assertEquals(1, feed.products.size)
            assertEquals("Women Printed Kurta", feed.products[0].title)
            assertEquals(1500, feed.products[0].currentPrice)
            assertEquals("https://api.stylish.app/images/kurta.jpg", feed.products[0].imageUrl)
            awaitComplete()
        }
    }
}
