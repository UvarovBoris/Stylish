package com.uvarov.stylish.core.database.di

import android.content.Context
import androidx.room.Room
import com.uvarov.stylish.core.database.StylishDatabase
import com.uvarov.stylish.core.database.dao.FavoriteProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStylishDatabase(
        @ApplicationContext context: Context,
    ): StylishDatabase = Room.databaseBuilder(
        context,
        StylishDatabase::class.java,
        "stylish-database",
    ).build()

    @Provides
    @Singleton
    fun provideFavoriteProductDao(
        database: StylishDatabase,
    ): FavoriteProductDao = database.favoriteProductDao()
}
