package com.uvarov.stylish.core.model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val currentPrice: Int,
    val originalPrice: Int,
    val discountPercent: Int,
    val rating: Float,
    val reviewCount: Int,
    val imageUrl: String,
    val importance: ProductImportance = ProductImportance.NORMAL,
    val categoryId: String? = null,
)
