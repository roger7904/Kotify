package org.roger.kotify.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.body_sm
import org.roger.kotify.ui.theme.body_sm_bold
import org.roger.kotify.ui.theme.orange_500

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.Companion,
    labelWeight: Float = 0.25f,
    fieldWeight: Float = 0.75f,
    additionalContentWeight: Float = 0.1f,
    isSingleLine: Boolean = true,
    isExpandable: Boolean = false,
    additionalContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Text(
            modifier = Modifier.Companion.weight(labelWeight),
            text = label,
            textAlign = TextAlign.Companion.Center,
            color = AppTheme.colorScheme.main_10,
            style = body_sm_bold,
        )

        Spacer(modifier = Modifier.Companion.width(8.dp))

        TextField(
            modifier = Modifier.Companion
                .weight(fieldWeight)
                .then(
                    if (isExpandable) Modifier.Companion.fillMaxHeight() else Modifier.Companion
                ),
            value = value,
            onValueChange = onValueChange,
            textStyle = body_sm,
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = AppTheme.colorScheme.main_3,
                unfocusedContainerColor = AppTheme.colorScheme.main_3,
                cursorColor = orange_500,
                focusedTextColor = AppTheme.colorScheme.main_7,
                unfocusedTextColor = AppTheme.colorScheme.main_7,
                unfocusedIndicatorColor = Color.Companion.Transparent,
                focusedIndicatorColor = Color.Companion.Transparent,
            ),
            singleLine = isSingleLine,
            maxLines = if (isExpandable) Int.MAX_VALUE else 1,
        )

        if (additionalContent != null) {
            Spacer(modifier = Modifier.Companion.width(8.dp))
            Box(
                modifier = Modifier.Companion.weight(additionalContentWeight),
            ) {
                additionalContent()
            }
        }
    }
}