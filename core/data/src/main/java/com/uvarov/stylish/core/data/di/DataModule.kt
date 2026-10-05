package com.uvarov.stylish.core.data.di

import com.uvarov.stylish.core.data.repository.DefaultHomeRepository
import com.uvarov.stylish.core.data.repository.DefaultProductRepository
import com.uvarov.stylish.core.data.repository.DefaultUserDataRepository
import com.uvarov.stylish.core.data.repository.DefaultWishlistRepository
import com.uvarov.stylish.core.data.repository.HomeRepository
import com.uvarov.stylish.core.data.repository.ProductRepository
import com.uvarov.stylish.core.data.repository.UserDataRepository
import com.uvarov.stylish.core.data.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindUserDataRepository(
        userDataRepository: DefaultUserDataRepository
    ): UserDataRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepository: DefaultHomeRepository
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepository: DefaultProductRepository
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(
        wishlistRepository: DefaultWishlistRepository
    ): WishlistRepository
}
