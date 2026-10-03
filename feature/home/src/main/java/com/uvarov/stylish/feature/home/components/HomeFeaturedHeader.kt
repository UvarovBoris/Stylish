package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeFeaturedHeader(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = HomeR.string.all_featured),
        style = StylishTheme.typography.sectionTitle,
        color = StylishTheme.colors.textPrimary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeFeaturedHeaderPreview() {
    StylishTheme {
        HomeFeaturedHeader()
    }
}
