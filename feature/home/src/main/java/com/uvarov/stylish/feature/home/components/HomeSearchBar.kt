package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        color = StylishTheme.colors.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = StylishTheme.colors.textSecondary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(id = HomeR.string.search_placeholder),
                        style = StylishTheme.typography.searchHint,
                        color = StylishTheme.colors.textSecondary,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    textStyle = StylishTheme.typography.searchHint.copy(
                        color = StylishTheme.colors.textPrimary
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(
                onClick = onVoiceClick,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = "Voice Search",
                    tint = StylishTheme.colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun HomeFilterSortRow(
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = HomeR.string.all_featured),
            style = StylishTheme.typography.sectionTitle,
            color = StylishTheme.colors.textPrimary,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterSortButton(
                label = stringResource(id = HomeR.string.sort),
                iconRes = R.drawable.ic_sort,
                onClick = onSortClick
            )
            FilterSortButton(
                label = stringResource(id = HomeR.string.filter),
                iconRes = R.drawable.ic_filter,
                onClick = onFilterClick
            )
        }
    }
}

@Composable
private fun FilterSortButton(
    label: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        color = StylishTheme.colors.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = StylishTheme.typography.productTitle,
                color = StylishTheme.colors.textPrimary
            )
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = StylishTheme.colors.textPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
