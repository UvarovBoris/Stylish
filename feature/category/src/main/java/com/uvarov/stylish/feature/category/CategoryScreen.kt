package com.uvarov.stylish.feature.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.model.ProductImportance
import com.uvarov.stylish.core.ui.component.ProductCard
import com.uvarov.stylish.core.designsystem.R as DesignR

@Composable
fun CategoryScreen(
    categoryId: String,
    categoryTitle: String,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoryId, categoryTitle) {
        viewModel.onIntent(CategoryIntent.LoadCategory(categoryId = categoryId, categoryTitle = categoryTitle))
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is CategorySideEffect.NavigateBack -> onBackClick()
                is CategorySideEffect.NavigateToProductDetails -> onProductClick(effect.productId)
                is CategorySideEffect.ShowToast -> { /* toast */
                }
            }
        }
    }

    CategoryContent(
        uiState = uiState,
        categoryTitle = categoryTitle,
        modifier = modifier,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun CategoryContent(
    uiState: CategoryUiState,
    categoryTitle: String,
    modifier: Modifier = Modifier,
    onIntent: (CategoryIntent) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StylishTheme.colors.background,
        topBar = {
            CategoryTopBar(
                title = if (uiState is CategoryUiState.Success && uiState.categoryTitle.isNotBlank()) {
                    uiState.categoryTitle
                } else {
                    categoryTitle
                },
                onBackClick = { onIntent(CategoryIntent.BackClicked) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is CategoryUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = StylishTheme.colors.brandPrimary,
                    )
                }

                is CategoryUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
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
                            onClick = { onIntent(CategoryIntent.RetryClicked) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StylishTheme.colors.brandPrimary
                            )
                        ) {
                            Text(text = stringResource(id = R.string.category_retry))
                        }
                    }
                }

                is CategoryUiState.Success -> {
                    if (uiState.products.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.category_placeholder_no_products),
                            style = StylishTheme.typography.sectionTitle,
                            color = StylishTheme.colors.textSecondary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalItemSpacing = 12.dp,
                        ) {
                            items(
                                items = uiState.products,
                                key = { it.id },
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { onIntent(CategoryIntent.ProductClicked(product)) },
                                    onFavoriteClick = { onIntent(CategoryIntent.ToggleFavorite(product.id)) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                tint = StylishTheme.colors.textPrimary
            )
        }

        Text(
            text = title,
            style = StylishTheme.typography.sectionTitle,
            color = StylishTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryContentPreview() {
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
            categoryId = "cat_womens"
        ),
        Product(
            id = "prod_8",
            title = "Black Dress",
            description = "Solid Black Dress for Women",
            currentPrice = 2000,
            originalPrice = 3999,
            discountPercent = 50,
            rating = 4.0f,
            reviewCount = 523456,
            imageUrl = "https://api.stylish.app/images/dress.jpg",
            importance = ProductImportance.NORMAL,
            categoryId = "cat_womens"
        )
    )

    StylishTheme {
        CategoryContent(
            uiState = CategoryUiState.Success(
                categoryId = "cat_womens",
                categoryTitle = "Womens",
                products = sampleProducts
            ),
            categoryTitle = "Womens",
            onIntent = {}
        )
    }
}
