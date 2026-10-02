package com.uvarov.stylish.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.uvarov.stylish.feature.main.MainScreen
import com.uvarov.stylish.feature.main.navigation.MainRoute
import com.uvarov.stylish.feature.onboarding.OnboardingScreen
import com.uvarov.stylish.feature.onboarding.navigation.OnboardingRoute

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(OnboardingRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<OnboardingRoute> {
                OnboardingScreen(
                    onComplete = {
                        backStack.clear()
                        backStack.add(MainRoute)
                    }
                )
            }
            entry<MainRoute> {
                MainScreen()
            }
        }
    )
}
