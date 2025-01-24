package org.roger.kotify.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.roger.kotify.ui.component.LabeledTextField
import org.roger.kotify.ui.theme.*
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun AddFavoriteDialog(
    onDismiss: () -> Unit,
    onSave: (FavoriteItem) -> Unit,
    initialServiceAccountPath: String,
    initialProjectId: String
) {
    var name by remember { mutableStateOf("") }
    var serviceAccountPath by remember { mutableStateOf(initialServiceAccountPath) }
    var projectId by remember { mutableStateOf(initialProjectId) }
    var targetDeviceToken by remember { mutableStateOf("") }
    var externalJsonData by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .background(color = AppTheme.colorScheme.main_2)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Favorite",
                        textAlign = TextAlign.Companion.Center,
                        color = AppTheme.colorScheme.main_10,
                        style = body_sm_bold,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LabeledTextField(
                        label = "Favorite Name",
                        value = name,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LabeledTextField(
                        label = "Service Account JSON Path",
                        value = serviceAccountPath,
                        onValueChange = { serviceAccountPath = it },
                        additionalContent = {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = orange_a80),
                                onClick = {
                                    val fileDialog = FileDialog(Frame(), "Select Service Account JSON", FileDialog.LOAD)
                                    fileDialog.isVisible = true
                                    val selectedFile = fileDialog.files.firstOrNull()
                                    if (selectedFile != null) {
                                        serviceAccountPath = selectedFile.absolutePath
                                    }
                                }
                            ) {
                                Text(
                                    color = AppTheme.colorScheme.main_3,
                                    text = stringResource(Res.string.browse),
                                    style = body_sm
                                )
                            }
                        },
                        fieldWeight = 0.55f,
                        additionalContentWeight = 0.2f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LabeledTextField(
                        label = "Project ID",
                        value = projectId,
                        onValueChange = { projectId = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LabeledTextField(
                        label = "Target Device Token",
                        value = targetDeviceToken,
                        onValueChange = { targetDeviceToken = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LabeledTextField(
                        label = stringResource(Res.string.external_json_data),
                        value = externalJsonData,
                        onValueChange = { externalJsonData = it },
                        isSingleLine = false,
                        isExpandable = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            color = orange_a80,
                            text = stringResource(Res.string.cancel),
                            style = body_md
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                val newFavorite = FavoriteItem(
                                    name = name,
                                    serviceAccountPath = serviceAccountPath,
                                    projectId = projectId,
                                    targetDeviceToken = targetDeviceToken,
                                    jsonMessage = externalJsonData
                                )
                                onSave(newFavorite)
                            }
                        }
                    ) {
                        Text(
                            color = orange_a80,
                            text = stringResource(Res.string.save),
                            style = body_md
                        )
                    }
                }
            }
        }
    }
}