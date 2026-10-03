package com.uvarov.stylish.core.model

data class DealOfTheDay(
    val title: String,
    val remainingTimeSeconds: Long,
    val products: List<Product>,
)
