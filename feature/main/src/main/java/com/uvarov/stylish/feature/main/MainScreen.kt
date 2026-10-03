package com.uvarov.stylish.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.StylishTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    homeContent: @Composable (onNavigateToSearch: () -> Unit) -> Unit = {
        TabPlaceholderContent(
            titleRes = R.string.home_placeholder_title,
            iconRes = R.drawable.ic_nav_home
        )
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent,
        homeContent = homeContent,
    )
}

@Composable
fun MainContent(
    uiState: MainUiState,
    modifier: Modifier = Modifier,
    onIntent: (MainIntent) -> Unit,
    homeContent: @Composable (onNavigateToSearch: () -> Unit) -> Unit = {
        TabPlaceholderContent(
            titleRes = R.string.home_placeholder_title,
            iconRes = R.drawable.ic_nav_home
        )
    },
) {
    // When not on the Home tab, pressing system back returns to the Home tab
    BackHandler(enabled = uiState.currentTab != MainTab.HOME) {
        onIntent(MainIntent.TabSelected(MainTab.HOME))
    }

    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StylishTheme.colors.background,
        bottomBar = {
            StylishBottomBar(
                selectedTab = uiState.currentTab,
                onTabSelected = { tab -> onIntent(MainIntent.TabSelected(tab)) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            saveableStateHolder.SaveableStateProvider(key = uiState.currentTab) {
                when (uiState.currentTab) {
                    MainTab.HOME -> homeContent {
                        onIntent(MainIntent.TabSelected(MainTab.SEARCH))
                    }


                    MainTab.WISHLIST -> TabPlaceholderContent(
                        titleRes = R.string.wishlist_placeholder_title,
                        iconRes = R.drawable.ic_nav_wishlist
                    )

                    MainTab.CART -> TabPlaceholderContent(
                        titleRes = R.string.cart_placeholder_title,
                        iconRes = R.drawable.ic_nav_cart
                    )

                    MainTab.SEARCH -> TabPlaceholderContent(
                        titleRes = R.string.search_placeholder_title,
                        iconRes = R.drawable.ic_nav_search
                    )

                    MainTab.SETTINGS -> TabPlaceholderContent(
                        titleRes = R.string.settings_placeholder_title,
                        iconRes = R.drawable.ic_nav_settings
                    )
                }
            }
        }
    }
}

@Composable
fun StylishBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = StylishTheme.colors.surface,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = tab == selectedTab
                    val itemColor = if (isSelected)
                        StylishTheme.colors.bottomNavigationTabSelected
                    else
                        StylishTheme.colors.textPrimary

                    BottomNavigationTabItem(
                        tab = tab,
                        isSelected = isSelected,
                        itemColor = itemColor,
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationTabItem(
    tab: MainTab,
    isSelected: Boolean,
    itemColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabLabel = stringResource(id = tab.labelRes)
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 28.dp),
                role = Role.Tab,
                onClick = onClick
            )
            .semantics {
                selected = isSelected
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = tab.iconRes),
            contentDescription = tabLabel,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = tabLabel,
            color = itemColor,
            style = if (isSelected) StylishTheme.typography.bottomNavigationTabSelected else StylishTheme.typography.bottomNavigationTab,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TabPlaceholderContent(
    titleRes: Int,
    iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = StylishTheme.colors.brandPrimary,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = titleRes),
            style = StylishTheme.typography.onboardingTitle,
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.placeholder_subtitle),
            style = StylishTheme.typography.onboardingDescription,
            color = StylishTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, name = "Main Screen - Home Tab")
@Composable
private fun MainContentHomePreview() {
    StylishTheme {
        MainContent(
            uiState = MainUiState(currentTab = MainTab.HOME),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Main Screen - Wishlist Tab")
@Composable
private fun MainContentWishlistPreview() {
    StylishTheme {
        MainContent(
            uiState = MainUiState(currentTab = MainTab.WISHLIST),
            onIntent = {}
        )
    }
}
