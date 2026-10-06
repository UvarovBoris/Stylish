package com.uvarov.stylish.feature.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.ui.component.StylishSearchBar
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeSearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    onVoiceClick: () -> Unit = {},
) {
    StylishSearchBar(
        query = "",
        modifier = modifier,
        placeholder = stringResource(id = HomeR.string.search_placeholder),
        enabled = false,
        onClick = onSearchClick,
        onVoiceClick = onVoiceClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeSearchBarPreview() {
    StylishTheme {
        HomeSearchBar(
            onSearchClick = {},
            onVoiceClick = {},
        )
    }
}
