package com.uvarov.stylish.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uvarov.stylish.core.database.dao.FavoriteProductDao
import com.uvarov.stylish.core.database.entity.FavoriteProductEntity

@Database(
    entities = [FavoriteProductEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class StylishDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
