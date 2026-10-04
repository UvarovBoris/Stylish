package com.uvarov.stylish.core.ui.util

import com.uvarov.stylish.core.designsystem.R
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductImageResolverTest {

    @Test
    fun resolveImageModel_whenHttpUrl_returnsUrlString() {
        val url = "http://images.example.com/shoes.jpg"
        val model = ProductImageResolver.resolveImageModel(url)
        assertEquals(url, model)
    }

    @Test
    fun resolveImageModel_whenHttpsUrl_returnsUrlString() {
        val url = "https://images.example.com/kurta.jpg"
        val model = ProductImageResolver.resolveImageModel(url)
        assertEquals(url, model)
    }

    @Test
    fun resolveImageModel_whenPlaceholderName_returnsDrawableRes() {
        val model = ProductImageResolver.resolveImageModel("placeholder_kurta")
        assertEquals(R.drawable.placeholder_kurta, model)
    }

    @Test
    fun resolveImageModel_whenUnknownName_returnsDefaultProductPlaceholder() {
        val model = ProductImageResolver.resolveImageModel("unknown_image_res")
        assertEquals(R.drawable.placeholder_product, model)
    }

    @Test
    fun resolveDrawable_mapsKnownNamesCorrectly() {
        assertEquals(R.drawable.placeholder_shoes, ProductImageResolver.resolveDrawable("placeholder_shoes"))
        assertEquals(R.drawable.placeholder_watch, ProductImageResolver.resolveDrawable("placeholder_watch"))
        assertEquals(R.drawable.placeholder_sneakers, ProductImageResolver.resolveDrawable("placeholder_sneakers"))
        assertEquals(R.drawable.placeholder_cat_beauty, ProductImageResolver.resolveDrawable("placeholder_cat_beauty"))
        assertEquals(R.drawable.placeholder_banner_hero, ProductImageResolver.resolveDrawable("placeholder_banner_hero"))
    }
}
