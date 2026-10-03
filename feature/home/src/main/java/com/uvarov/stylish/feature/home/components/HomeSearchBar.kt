package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeSearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    onVoiceClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .dropShadow(
                shape = RoundedCornerShape(6.dp),
                shadow = Shadow(
                    radius = 9.dp,
                    offset = DpOffset(0.dp, 2.dp),
                    color = StylishTheme.colors.dropShadow,
                ),
            )
            .clickable(
                role = Role.Button,
                onClick = onSearchClick
            ),
        shape = RoundedCornerShape(6.dp),
        color = StylishTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = StylishTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(id = HomeR.string.search_placeholder),
                style = StylishTheme.typography.searchHint,
                color = StylishTheme.colors.textSecondary,
                modifier = Modifier.weight(1f)
            )

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

@Preview(showBackground = true)
@Composable
private fun HomeSearchBarPreview() {
    StylishTheme {
        HomeSearchBar(
            onSearchClick = {},
            onVoiceClick = {}
        )
    }
}
