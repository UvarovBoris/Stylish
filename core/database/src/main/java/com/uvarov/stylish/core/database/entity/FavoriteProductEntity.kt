package com.uvarov.stylish.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis(),
)
