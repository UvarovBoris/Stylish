package com.uvarov.stylish.feature.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.ui.component.ProductCard
import com.uvarov.stylish.core.ui.component.StylishSearchBar
import com.uvarov.stylish.core.designsystem.R as DesignR

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SearchSideEffect.NavigateToProductDetails -> onProductClick(effect.productId)
                is SearchSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SearchContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    modifier: Modifier = Modifier,
    onIntent: (SearchIntent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StylishTheme.colors.background),
    ) {
        SearchTopBar(
            onAvatarClick = { /* profile */ },
        )

        Spacer(modifier = Modifier.height(4.dp))

        StylishSearchBar(
            query = uiState.query,
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            onQueryChange = { onIntent(SearchIntent.QueryChanged(it)) },
            onSearch = { onIntent(SearchIntent.SearchSubmitted) },
            onClearClick = { onIntent(SearchIntent.ClearQuery) },
            onVoiceClick = { onIntent(SearchIntent.VoiceSearchClicked) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (uiState) {
            is SearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = StylishTheme.colors.brandPrimary)
                }
            }

            is SearchUiState.Empty -> {
                SearchEmptyState(
                    query = uiState.query,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }

            is SearchUiState.Error -> {
                SearchErrorState(
                    message = uiState.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onRetry = { onIntent(SearchIntent.RetryClicked) },
                )
            }

            is SearchUiState.Success -> {
                SearchSuccessContent(
                    products = uiState.products,
                    totalCount = uiState.totalCount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onProductClick = { onIntent(SearchIntent.ProductClicked(it)) },
                    onFavoriteClick = { onIntent(SearchIntent.ToggleFavorite(it.id)) },
                    onSortClick = { onIntent(SearchIntent.SortClicked) },
                    onFilterClick = { onIntent(SearchIntent.FilterClicked) },
                )
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = DesignR.drawable.ic_stylish_logo),
            contentDescription = stringResource(id = R.string.search_title),
        )

        IconButton(
            onClick = onAvatarClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp),
        ) {
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_user),
                contentDescription = null,
                tint = StylishTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun SearchSuccessContent(
    products: List<Product>,
    totalCount: Int,
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.search_items_count, totalCount),
                style = StylishTheme.typography.sectionTitle,
                color = StylishTheme.colors.textPrimary,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SearchActionButton(
                    iconRes = DesignR.drawable.ic_sort,
                    text = stringResource(id = R.string.search_sort),
                    onClick = onSortClick,
                )
                SearchActionButton(
                    iconRes = DesignR.drawable.ic_filter,
                    text = stringResource(id = R.string.search_filter),
                    onClick = onFilterClick,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
        ) {
            items(
                items = products,
                key = { it.id },
            ) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onFavoriteClick = { onFavoriteClick(product) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun SearchActionButton(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(6.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    offset = DpOffset(0.dp, 1.dp),
                    color = StylishTheme.colors.searchShadow,
                ),
            )
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(6.dp),
        color = StylishTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = text,
                style = StylishTheme.typography.categoryName,
                color = StylishTheme.colors.textPrimary,
            )
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = StylishTheme.colors.textPrimary,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun SearchEmptyState(
    query: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = DesignR.drawable.ic_search),
            contentDescription = null,
            tint = StylishTheme.colors.brandPrimary,
            modifier = Modifier.size(56.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.search_no_results),
            style = StylishTheme.typography.sectionTitle,
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.search_no_results_desc),
            style = StylishTheme.typography.onboardingDescription,
            color = StylishTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SearchErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message.ifBlank { stringResource(id = R.string.search_error_generic) },
            style = StylishTheme.typography.sectionTitle,
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = StylishTheme.colors.brandPrimary,
            ),
        ) {
            Text(text = stringResource(id = R.string.search_retry))
        }
    }
}

@Preview(showBackground = true, name = "Search Screen - Loading")
@Composable
private fun SearchContentLoadingPreview() {
    StylishTheme {
        SearchContent(
            uiState = SearchUiState.Loading(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "Search Screen - Empty")
@Composable
private fun SearchContentEmptyPreview() {
    StylishTheme {
        SearchContent(
            uiState = SearchUiState.Empty(query = "Nonexistent product"),
            onIntent = {},
        )
    }
}
