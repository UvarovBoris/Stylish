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
        assertEquals(3, dto.heroBanners.size)
        assertTrue(dto.products.isNotEmpty())
        assertEquals("Women Printed Kurta", dto.products[0].title)
        assertEquals(1500, dto.products[0].currentPrice)
        assertEquals(40, dto.products[0].discountPercent)
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
    }
}
