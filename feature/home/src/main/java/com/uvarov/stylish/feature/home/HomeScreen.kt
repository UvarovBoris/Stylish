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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeTopBar(
                        onAvatarClick = { /* Profile action */ }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeSearchBar(
                        modifier = Modifier.padding(top = 16.dp),
                        onSearchClick = { onIntent(HomeIntent.SearchBarClicked) },
                        onVoiceClick = { onIntent(HomeIntent.VoiceSearchClicked) }
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeFeaturedHeader(
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HomeCategoriesRow(
                        modifier = Modifier.padding(top = 16.dp),
                        categories = feed.categories,
                        onCategoryClick = { category ->
                            onIntent(HomeIntent.CategoryClicked(categoryId = category.id, title = category.name))
                        }
                    )
                }
                if (feed.heroBanners.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        HomeHeroBanner(
                            banners = feed.heroBanners,
                            modifier = Modifier.padding(top = 16.dp),
                            onBannerClick = { banner ->
                                onIntent(HomeIntent.HeroBannerClicked(banner))
                            }
                        )
                    }
                }
                if (feed.products.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    itemsIndexed(
                        items = feed.products,
                        key = { _, product -> product.id },
                        span = { _, _ -> GridItemSpan(1) }
                    ) { index, product ->
                        val isLeft = index % 2 == 0
                        ProductCard(
                            product = product,
                            onClick = { onIntent(HomeIntent.ProductClicked(product)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = if (isLeft) 16.dp else 6.dp,
                                    end = if (isLeft) 6.dp else 16.dp,
                                    bottom = 12.dp
                                )
                        )
                    }
                }
            }
        }
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
            imageResName = "placeholder_kurta"
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
            imageResName = "placeholder_shoes"
        )
    )

    val sampleCategories = listOf(
        Category("1", "Beauty", "placeholder_cat_beauty"),
        Category("2", "Fashion", "placeholder_cat_fashion"),
        Category("3", "Kids", "placeholder_cat_kids"),
        Category("4", "Mens", "placeholder_cat_mens"),
        Category("5", "Womens", "placeholder_cat_womens"),
        Category("6", "Gifts", "placeholder_cat_gifts")
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
