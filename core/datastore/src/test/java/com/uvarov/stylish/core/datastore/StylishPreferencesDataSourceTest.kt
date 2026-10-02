package com.uvarov.stylish.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class StylishPreferencesDataSourceTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var dataSource: StylishPreferencesDataSource

    @Before
    fun setUp() {
        val testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmpFolder.root, "test_preferences.preferences_pb") }
        )
        dataSource = StylishPreferencesDataSource(testDataStore)
    }

    @Test
    fun defaultOnboardingCompleted_isFalse() = testScope.runTest {
        dataSource.isOnboardingCompleted.test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setOnboardingCompleted_updatesFlowToTrue() = testScope.runTest {
        dataSource.isOnboardingCompleted.test {
            assertFalse(awaitItem())
            dataSource.setOnboardingCompleted(true)
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
