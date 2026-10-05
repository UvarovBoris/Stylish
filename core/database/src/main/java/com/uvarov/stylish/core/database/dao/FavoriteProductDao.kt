package com.uvarov.stylish.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.uvarov.stylish.core.database.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {

    @Query("SELECT product_id FROM favorite_products ORDER BY added_at DESC")
    fun getFavoriteProductIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE product_id = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>

    @Upsert
    suspend fun insertFavorite(favorite: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE product_id = :productId")
    suspend fun deleteFavorite(productId: String)

    @Query("DELETE FROM favorite_products")
    suspend fun clearFavorites()
}
