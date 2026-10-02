package com.uvarov.stylish.core.data.repository

import app.cash.turbine.test
import com.uvarov.stylish.core.datastore.StylishPreferencesDataSource
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultUserDataRepositoryTest {

    private val preferencesDataSource = mockk<StylishPreferencesDataSource>(relaxed = true)
    private lateinit var repository: DefaultUserDataRepository

    @Before
    fun setUp() {
        repository = DefaultUserDataRepository(preferencesDataSource)
    }

    @Test
    fun isOnboardingCompleted_delegatesToDataSource() = runTest {
        every { preferencesDataSource.isOnboardingCompleted } returns flowOf(true)

        repository.isOnboardingCompleted.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setOnboardingCompleted_delegatesToDataSource() = runTest {
        repository.setOnboardingCompleted(true)

        coVerify { preferencesDataSource.setOnboardingCompleted(true) }
    }
}
