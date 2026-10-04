package com.uvarov.stylish.core.network

import com.uvarov.stylish.core.network.model.HomeFeedResponseDto
import com.uvarov.stylish.core.network.model.ProductDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class NetworkSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Test
    fun parseHomeFeedJson_succeeds() {
        val file = File("src/main/assets/mock/home_feed.json")
        val content = if (file.exists()) {
            file.readText()
        } else {
            // When run from root project directory
            File("core/network/src/main/assets/mock/home_feed.json").readText()
        }

        val dto = json.decodeFromString<HomeFeedResponseDto>(content)
        assertNotNull(dto)
        assertEquals(6, dto.categories.size)
        assertTrue(dto.categories.any { it.id == "cat_gifts" && it.name == "Gifts" })
        assertEquals("https://api.stylish.app/images/cat_beauty.png", dto.categories[0].imageUrl)
        assertEquals(3, dto.heroBanners.size)
        assertEquals("https://api.stylish.app/images/banner_hero.png", dto.heroBanners[0].imageUrl)
        assertTrue(dto.products.isNotEmpty())
        assertEquals("Women Printed Kurta", dto.products[0].title)
        assertEquals(1500, dto.products[0].currentPrice)
        assertEquals(40, dto.products[0].discountPercent)
        assertEquals("https://api.stylish.app/images/kurta.jpg", dto.products[0].imageUrl)
    }

    @Test
    fun parseProductsJson_succeeds() {
        val file = File("src/main/assets/mock/products.json")
        val content = if (file.exists()) {
            file.readText()
        } else {
            File("core/network/src/main/assets/mock/products.json").readText()
        }

        val products = json.decodeFromString<List<ProductDto>>(content)
        assertNotNull(products)
        assertEquals(20, products.size)
        assertEquals("https://api.stylish.app/images/kurta.jpg", products[0].imageUrl)
        assertEquals("https://api.stylish.app/images/shoes.jpg", products[1].imageUrl)
    }
}
