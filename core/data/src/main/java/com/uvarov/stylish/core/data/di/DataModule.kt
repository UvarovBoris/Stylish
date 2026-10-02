package com.uvarov.stylish.core.data.di

import com.uvarov.stylish.core.data.repository.DefaultUserDataRepository
import com.uvarov.stylish.core.data.repository.UserDataRepository
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
}
