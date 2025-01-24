package org.roger.kotify.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.body_sm
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.DateFormat
import java.util.*

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

@Composable
fun SendRecordItem(record: SendRecord, onClick: () -> Unit) {
    val jsonObject = if (record.externalJson.isNotBlank()) {
        json.parseToJsonElement(record.externalJson).jsonObject
    } else {
        JsonObject(emptyMap())
    }
    val title = jsonObject["title"]?.jsonPrimitive?.content ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "status: ${record.statusCode}",
                style = body_sm,
                modifier = Modifier.weight(1f),
                color = AppTheme.colorScheme.main_7
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (record.isSuccess) Color.Green else Color.Red,
                        shape = CircleShape
                    )
            )
        }

        Text(
            text = "title: ${title}",
            style = body_sm,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppTheme.colorScheme.main_7
        )

        if (!record.isSuccess) {
            Text(
                text = "description: ${record.statusDescription}",
                style = body_sm,
                color = AppTheme.colorScheme.main_7,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = "time: ${DateFormat.getDateTimeInstance().format(Date(record.timestamp))}",
            style = body_sm,
            color = AppTheme.colorScheme.main_7
        )

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}