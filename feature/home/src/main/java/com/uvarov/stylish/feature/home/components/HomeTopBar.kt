package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    onAvatarClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_stylish_logo),
            contentDescription = stringResource(id = HomeR.string.app_name_stylish)
        )

        IconButton(
            onClick = onAvatarClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "User",
                tint = StylishTheme.colors.textSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    StylishTheme {
        HomeTopBar()
    }
}
