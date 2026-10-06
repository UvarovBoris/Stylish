package com.uvarov.stylish.feature.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.model.ProductImportance
import com.uvarov.stylish.core.ui.component.ProductCard
import com.uvarov.stylish.feature.home.components.HomeCategoriesRow
import com.uvarov.stylish.feature.home.components.HomeFeaturedHeader
import com.uvarov.stylish.feature.home.components.HomeHeroBanner
import com.uvarov.stylish.feature.home.components.HomeSearchBar
import com.uvarov.stylish.feature.home.components.HomeTopBar
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
    onNavigateToCatalog: (categoryId: String?, title: String?) -> Unit = { _, _ -> },
    onNavigateToSearch: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToProductDetails -> onProductClick(effect.productId)
                is HomeSideEffect.NavigateToCatalog -> onNavigateToCatalog(effect.categoryId, effect.title)
                is HomeSideEffect.NavigateToSearch -> onNavigateToSearch()
                is HomeSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    HomeContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onIntent: (HomeIntent) -> Unit,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = StylishTheme.colors.brandPrimary)
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = StylishTheme.typography.sectionTitle,
                        color = StylishTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onIntent(HomeIntent.LoadHomeFeed) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StylishTheme.colors.brandPrimary
                        )
                    ) {
                        Text(text = stringResource(id = HomeR.string.retry))
                    }
                }
            }
        }

        is HomeUiState.Success -> {
            val feed = uiState.homeFeed
            val categoriesScrollState = rememberScrollState()
            val heroPagerState = rememberPagerState(pageCount = { feed.heroBanners.size })

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp,
            ) {
                item(
                    key = "home_top_bar",
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    HomeTopBar(
                        modifier = Modifier.fullScreenWidth(),
                        onAvatarClick = { /* Profile action */ }
                    )
                }
                item(
                    key = "home_search_bar",
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    HomeSearchBar(
                        modifier = Modifier
                            .fullScreenWidth(),
                        onSearchClick = { onIntent(HomeIntent.SearchBarClicked) },
                        onVoiceClick = { onIntent(HomeIntent.VoiceSearchClicked) }
                    )
                }
                item(
                    key = "home_featured_header",
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    HomeFeaturedHeader(
                        modifier = Modifier
                            .fullScreenWidth()
                            .padding(top = 4.dp),
                    )
                }
                item(
                    key = "home_categories_row",
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    HomeCategoriesRow(
                        modifier = Modifier
                            .fullScreenWidth()
                            .padding(top = 4.dp),
                        categories = feed.categories,
                        scrollState = categoriesScrollState,
                        onCategoryClick = { category ->
                            onIntent(HomeIntent.CategoryClicked(categoryId = category.id, title = category.name))
                        }
                    )
                }
                if (feed.heroBanners.isNotEmpty()) {
                    item(
                        key = "home_hero_banner",
                        span = StaggeredGridItemSpan.FullLine,
                    ) {
                        HomeHeroBanner(
                            banners = feed.heroBanners,
                            pagerState = heroPagerState,
                            modifier = Modifier
                                .fullScreenWidth()
                                .padding(top = 4.dp),
                            onBannerClick = { banner ->
                                onIntent(HomeIntent.HeroBannerClicked(banner))
                            }
                        )
                    }
                }
                items(
                    items = feed.products,
                    key = { it.id },
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onIntent(HomeIntent.ProductClicked(product)) },
                        onFavoriteClick = { onIntent(HomeIntent.ToggleFavorite(product.id)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun Modifier.fullScreenWidth(horizontalPadding: Dp = 16.dp): Modifier = layout { measurable, constraints ->
    val paddingPx = horizontalPadding.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.maxWidth + paddingPx * 2,
            maxWidth = constraints.maxWidth + paddingPx * 2
        )
    )
    layout(constraints.maxWidth, placeable.height) {
        placeable.placeRelative(-paddingPx, 0)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    val sampleProducts = listOf(
        Product(
            id = "prod_1",
            title = "Women Printed Kurta",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            currentPrice = 1500,
            originalPrice = 2499,
            discountPercent = 40,
            rating = 4.0f,
            reviewCount = 56890,
            imageUrl = "https://api.stylish.app/images/kurta.jpg",
            importance = ProductImportance.HIGH,
        ),
        Product(
            id = "prod_2",
            title = "HRX by Hrithik Roshan",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            currentPrice = 2499,
            originalPrice = 4999,
            discountPercent = 50,
            rating = 4.5f,
            reviewCount = 344567,
            imageUrl = "https://api.stylish.app/images/shoes.jpg",
            importance = ProductImportance.NORMAL,
        )
    )

    val sampleCategories = listOf(
        Category("1", "Beauty", "https://api.stylish.app/images/cat_beauty.png"),
        Category("2", "Fashion", "https://api.stylish.app/images/cat_fashion.png"),
        Category("3", "Kids", "https://api.stylish.app/images/cat_kids.png"),
        Category("4", "Mens", "https://api.stylish.app/images/cat_mens.png"),
        Category("5", "Womens", "https://api.stylish.app/images/cat_womens.png"),
        Category("6", "Gifts", "https://api.stylish.app/images/cat_gifts.png")
    )

    val sampleFeed = HomeFeed(
        categories = sampleCategories,
        heroBanners = listOf(
            BannerItem("1", "50-40% OFF", "Now in (product)\nAll colours", "Shop Now", "placeholder_banner_hero"),
            BannerItem("2", "Summer Wave", "Top picks on fresh styles\nLimited time", "Shop Now", "placeholder_banner_hero"),
            BannerItem("3", "Exclusive Deals", "Up to 70% off trending styles\nCheck it out", "Shop Now", "placeholder_banner_hero")
        ),
        products = sampleProducts
    )

    StylishTheme {
        HomeContent(
            uiState = HomeUiState.Success(homeFeed = sampleFeed),
            onIntent = {}
        )
    }
}
