package com.uvarov.stylish.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.uvarov.stylish.feature.category.CategoryScreen
import com.uvarov.stylish.feature.category.navigation.CategoryRoute
import com.uvarov.stylish.feature.home.HomeScreen
import com.uvarov.stylish.feature.home.navigation.HomeRoute
import com.uvarov.stylish.feature.main.MainIntent
import com.uvarov.stylish.feature.main.MainScreen
import com.uvarov.stylish.feature.main.MainTab
import com.uvarov.stylish.feature.main.MainViewModel
import com.uvarov.stylish.feature.main.TabPlaceholderContent
import com.uvarov.stylish.feature.main.navigation.MainRoute
import com.uvarov.stylish.feature.onboarding.OnboardingScreen
import com.uvarov.stylish.feature.onboarding.navigation.OnboardingRoute
import com.uvarov.stylish.feature.productdetail.ProductDetailScreen
import com.uvarov.stylish.feature.productdetail.navigation.ProductDetailRoute
import com.uvarov.stylish.feature.wishlist.WishlistScreen
import com.uvarov.stylish.feature.main.R as MainR

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: NavKey = OnboardingRoute,
) {
    val backStack = rememberNavBackStack(startDestination)

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
                MainNavigation(
                    onNavigateToProductDetail = { productId ->
                        backStack.add(ProductDetailRoute(productId = productId))
                    }
                )
            }
            entry<ProductDetailRoute> { route ->
                ProductDetailScreen(
                    productId = route.productId,
                    onBackClick = { backStack.removeLastOrNull() },
                    onCartClick = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}

@Composable
private fun MainNavigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToProductDetail: (String) -> Unit = {},
) {
    val homeBackStack = rememberNavBackStack(HomeRoute)

    val homeDecorators = listOf<NavEntryDecorator<NavKey>>(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )

    val homeEntryProvider = entryProvider {
        entry<HomeRoute> {
            HomeScreen(
                onProductClick = onNavigateToProductDetail,
                onNavigateToCatalog = { categoryId, title ->
                    homeBackStack.add(
                        CategoryRoute(
                            categoryId = categoryId.orEmpty(),
                            categoryTitle = title.orEmpty()
                        )
                    )
                },
                onNavigateToSearch = {
                    viewModel.onIntent(MainIntent.TabSelected(MainTab.SEARCH))
                },
            )
        }
        entry<CategoryRoute> { route ->
            CategoryScreen(
                categoryId = route.categoryId,
                categoryTitle = route.categoryTitle,
                onBackClick = { homeBackStack.removeLastOrNull() },
                onProductClick = onNavigateToProductDetail,
            )
        }
    }

    val homeEntries = rememberDecoratedNavEntries(
        backStack = homeBackStack,
        entryDecorators = homeDecorators,
        entryProvider = homeEntryProvider,
    )

    MainScreen(
        modifier = modifier,
        viewModel = viewModel,
        onTabReselected = { tab ->
            if (tab == MainTab.HOME) {
                while (homeBackStack.size > 1) {
                    homeBackStack.removeLastOrNull()
                }
            }
        },
        tabContent = { tab ->
            when (tab) {
                MainTab.HOME -> {
                    NavDisplay(
                        entries = homeEntries,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { homeBackStack.removeLastOrNull() },
                        transitionSpec = {
                            fadeIn(animationSpec = tween(200)) togetherWith
                                fadeOut(animationSpec = tween(200))
                        },
                        popTransitionSpec = {
                            fadeIn(animationSpec = tween(200)) togetherWith
                                fadeOut(animationSpec = tween(200))
                        },
                        predictivePopTransitionSpec = {
                            fadeIn(animationSpec = tween(200)) togetherWith
                                fadeOut(animationSpec = tween(200))
                        },
                    )
                }

                MainTab.WISHLIST -> WishlistScreen(
                    onProductClick = onNavigateToProductDetail,
                )

                MainTab.CART -> TabPlaceholderContent(
                    titleRes = MainR.string.cart_placeholder_title,
                    iconRes = MainR.drawable.ic_nav_cart,
                )

                MainTab.SEARCH -> TabPlaceholderContent(
                    titleRes = MainR.string.search_placeholder_title,
                    iconRes = MainR.drawable.ic_nav_search,
                )

                MainTab.SETTINGS -> TabPlaceholderContent(
                    titleRes = MainR.string.settings_placeholder_title,
                    iconRes = MainR.drawable.ic_nav_settings,
                )
            }
        }
    )
}
