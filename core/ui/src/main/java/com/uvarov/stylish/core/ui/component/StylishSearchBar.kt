package com.uvarov.stylish.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.uvarov.stylish.core.designsystem.R as DesignR
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.ui.R as UiR

@Composable
fun StylishSearchBar(
    query: String,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(id = UiR.string.search_placeholder),
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null,
    onQueryChange: (String) -> Unit = {},
    onClick: (() -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null,
    onVoiceClick: (() -> Unit)? = null,
    onClearClick: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val isInteractive = enabled && onClick == null

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .dropShadow(
                shape = RoundedCornerShape(6.dp),
                shadow = Shadow(
                    radius = 9.dp,
                    offset = DpOffset(0.dp, 2.dp),
                    color = StylishTheme.colors.searchShadow,
                ),
            )
            .clip(shape = RoundedCornerShape(6.dp))
            .then(
                if (!isInteractive && onClick != null) {
                    Modifier.clickable(
                        role = Role.Button,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(6.dp),
        color = StylishTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_search),
                contentDescription = null,
                tint = StylishTheme.colors.textSecondary,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (isInteractive) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (focusRequester != null) {
                                    Modifier.focusRequester(focusRequester)
                                } else {
                                    Modifier
                                }
                            ),
                        textStyle = StylishTheme.typography.searchHint.copy(
                            color = StylishTheme.colors.textPrimary,
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(StylishTheme.colors.brandPrimary),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                onSearch?.invoke(query)
                            },
                        ),
                        decorationBox = { innerTextField ->
                            if (query.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = StylishTheme.typography.searchHint,
                                    color = StylishTheme.colors.textSecondary,
                                    maxLines = 1,
                                )
                            }
                            innerTextField()
                        },
                    )
                } else {
                    Text(
                        text = query.ifEmpty { placeholder },
                        style = StylishTheme.typography.searchHint,
                        color = if (query.isEmpty()) {
                            StylishTheme.colors.textSecondary
                        } else {
                            StylishTheme.colors.textPrimary
                        },
                        maxLines = 1,
                    )
                }
            }

            if (isInteractive && query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearClick?.invoke() ?: onQueryChange("")
                    },
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        painter = painterResource(id = DesignR.drawable.ic_close),
                        contentDescription = stringResource(id = UiR.string.cd_clear_search),
                        tint = StylishTheme.colors.textSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            } else if (onVoiceClick != null) {
                IconButton(
                    onClick = onVoiceClick,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        painter = painterResource(id = DesignR.drawable.ic_mic),
                        contentDescription = stringResource(id = UiR.string.cd_voice_search),
                        tint = StylishTheme.colors.textSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Interactive Search Bar - Empty")
@Composable
private fun StylishSearchBarInteractiveEmptyPreview() {
    StylishTheme {
        StylishSearchBar(
            query = "",
            enabled = true,
            onQueryChange = {},
        )
    }
}

@Preview(showBackground = true, name = "Interactive Search Bar - With Query")
@Composable
private fun StylishSearchBarInteractiveWithQueryPreview() {
    StylishTheme {
        StylishSearchBar(
            query = "Shoes",
            enabled = true,
            onQueryChange = {},
        )
    }
}

@Preview(showBackground = true, name = "Inactive Search Bar - Home Button")
@Composable
private fun StylishSearchBarInactivePreview() {
    StylishTheme {
        StylishSearchBar(
            query = "",
            enabled = false,
            onClick = {},
        )
    }
}
