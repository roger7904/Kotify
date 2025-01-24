package org.roger.kotify.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.body_md

@Composable
fun FavoriteItem(favorite: FavoriteItem, onClick: () -> Unit, onDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = favorite.name,
                style = body_md,
                modifier = Modifier.weight(1f),
                color = AppTheme.colorScheme.main_7,
            )
            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.main_7,
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}