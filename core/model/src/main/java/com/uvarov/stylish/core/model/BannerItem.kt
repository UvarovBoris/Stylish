package com.uvarov.stylish.core.model

data class BannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val actionText: String = "",
    val imageUrl: String = "",
)
