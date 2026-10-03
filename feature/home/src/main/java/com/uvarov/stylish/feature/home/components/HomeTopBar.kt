package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.BlueAccent
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = StylishTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_stylish_logo),
                contentDescription = null,
                tint = StylishTheme.colors.brandPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = HomeR.string.app_name_stylish),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = BlueAccent
            )
        }

        IconButton(
            onClick = onAvatarClick,
            modifier = Modifier.size(36.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        }
    }
}
