package org.roger.kotify.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.roger.kotify.ui.theme.body_md_bold
import kotify.composeapp.generated.resources.Res
import kotify.composeapp.generated.resources.record_list
import org.roger.kotify.ui.theme.orange_500
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecordsPanel(
    modifier: Modifier = Modifier,
    sendRecords: List<SendRecord>,
    onRecordClick: (SendRecord) -> Unit,
    onDeleteAllRecords: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.record_list),
                style = body_md_bold,
                color = orange_500,
            )

            IconButton(onClick = onDeleteAllRecords) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = AppTheme.colorScheme.main_7
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(sendRecords) { record ->
                SendRecordItem(
                    record = record,
                    onClick = { onRecordClick(record) }
                )
            }
        }
    }
}