package org.roger.kotify.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotify.composeapp.generated.resources.Res
import kotify.composeapp.generated.resources.favorite_list
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.body_md_bold
import org.roger.kotify.ui.theme.orange_500
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesPanel(
    modifier: Modifier,
    favorites: List<FavoriteItem>,
    onFavoriteClick: (FavoriteItem) -> Unit,
    onFavoriteDelete: (FavoriteItem) -> Unit,
    onAddFavoriteClick: () -> Unit
) {
    Column (
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAddFavoriteClick)
        ) {
            Text(
                text = stringResource(Res.string.favorite_list),
                style = body_md_bold,
                modifier = Modifier.weight(1f),
                color = orange_500,
            )
            IconButton(onClick = onAddFavoriteClick) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = orange_500,
                )
            }
        }

        LazyColumn(
            modifier = Modifier.background(color = AppTheme.colorScheme.main_3)
        ) {
            items(favorites) { favorite ->
                FavoriteItem(
                    favorite = favorite,
                    onClick = { onFavoriteClick(favorite) },
                    onDelete = { onFavoriteDelete(favorite) }
                )
            }
        }
    }
}