package org.roger.kotify.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.http.*
import kotify.composeapp.generated.resources.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.jetbrains.compose.resources.stringResource
import org.roger.kotify.favorite.AddFavoriteDialog
import org.roger.kotify.favorite.FavoriteItem
import org.roger.kotify.favorite.FavoritesPanel
import org.roger.kotify.favorite.FavoritesStorage
import org.roger.kotify.platform.SettingsUtil
import org.roger.kotify.platform.UserSettings
import org.roger.kotify.record.RecordsPanel
import org.roger.kotify.record.SendRecord
import org.roger.kotify.record.SendRecordStorage
import org.roger.kotify.ui.component.LabeledTextField
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.body_md
import org.roger.kotify.ui.theme.body_sm
import org.roger.kotify.ui.theme.orange_a80
import org.roger.kotify.utils.FcmClient
import org.roger.kotify.utils.FcmMessage
import org.roger.kotify.utils.FcmNotification
import org.roger.kotify.utils.GoogleAuthProviderImpl
import java.awt.FileDialog
import java.awt.Frame

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

@NonRestartableComposable
@Composable
actual fun NotificationSenderScreen(
    viewModel: NotificationSenderViewModel,
    httpClient: HttpClient
) {
    var serviceAccountPath by remember { mutableStateOf(SettingsUtil.loadSettings().serviceAccountPath) }
    var projectId by remember { mutableStateOf(SettingsUtil.loadSettings().projectId) }
    var targetDeviceToken by remember { mutableStateOf(SettingsUtil.loadSettings().targetDeviceToken) }
    var externalJsonData by remember { mutableStateOf(SettingsUtil.loadSettings().externalJson) }
    val authProvider = GoogleAuthProviderImpl(httpClient)
    val fcmClient = FcmClient(httpClient, authProvider)
    val scope = rememberCoroutineScope()

    var sendStatusCode by remember { mutableStateOf<Int?>(null) }
    var isSendSuccessful by remember { mutableStateOf<Boolean?>(null) }

    var favorites by remember { mutableStateOf(FavoritesStorage.loadFavorites()) }
    var showAddFavoriteDialog by remember { mutableStateOf(false) }

    var sendRecords by remember { mutableStateOf(SendRecordStorage.loadRecords()) }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        NotificationForm(
            modifier = Modifier.weight(0.7f),
            serviceAccountPath = serviceAccountPath,
            onServiceAccountPathChange = { serviceAccountPath = it },
            projectId = projectId,
            onProjectIdChange = { projectId = it },
            targetDeviceToken = targetDeviceToken,
            onTargetDeviceTokenChange = { targetDeviceToken = it },
            externalJsonData = externalJsonData,
            onExternalJsonDataChange = { externalJsonData = it },
            onBrowseServiceAccountPath = {
                val fileDialog = FileDialog(Frame(), "Select Service Account JSON Path", FileDialog.LOAD)
                fileDialog.isVisible = true
                val selectedFile = fileDialog.files.firstOrNull()
                if (selectedFile != null) {
                    serviceAccountPath = selectedFile.absolutePath
                }
            },
            onSendNotification = {
                sendStatusCode = null
                isSendSuccessful = null

                val combinedJsonMessage = createCombinedJsonMessage(
                    externalJsonData
                )

                SettingsUtil.saveSettings(
                    UserSettings(
                        serviceAccountPath = serviceAccountPath,
                        projectId = projectId,
                        targetDeviceToken = targetDeviceToken,
                        externalJson = combinedJsonMessage
                    )
                )

                scope.launch {
                    try {
                        val jsonElement = Json.parseToJsonElement(combinedJsonMessage)

                        println("jsonElement: $jsonElement")

                        val response = fcmClient.sendNotification(
                            projectId = projectId,
                            messageBody = FcmMessage(
                                FcmNotification(
                                    targetDeviceToken,
                                    jsonElement,
                                )
                            ),
                            serviceAccountJson = serviceAccountPath
                        )
                        sendStatusCode = response.status.value
                        isSendSuccessful = response.status.isSuccess()
                        val statusDescription = response.status.description

                        val newRecord = SendRecord(
                            statusCode = response.status.value,
                            statusDescription = statusDescription,
                            isSuccess = isSendSuccessful == true,
                            serviceAccountPath = serviceAccountPath,
                            projectId = projectId,
                            targetDeviceToken = targetDeviceToken,
                            externalJson = combinedJsonMessage
                        )

                        sendRecords = (listOf(newRecord) + sendRecords).take(100)
                        SendRecordStorage.saveRecords(sendRecords)

                        println("Notification sent successfully: ${response.status}")
                    } catch (e: Exception) {
                        println("Failed to send notification: ${e.message}")
                    }
                }
            },
            sendStatusCode = sendStatusCode,
            isSendSuccessful = isSendSuccessful
        )

        // 紀錄面板
        RecordsPanel(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .background(color = AppTheme.colorScheme.main_3)
                .padding(16.dp),
            sendRecords = sendRecords,
            // 用紀錄內容替換左側面板
            onRecordClick = { record ->
                serviceAccountPath = record.serviceAccountPath
                projectId = record.projectId
                targetDeviceToken = record.targetDeviceToken
                externalJsonData = record.externalJson
            },
            onDeleteAllRecords = {
                sendRecords = emptyList()
                SendRecordStorage.saveRecords(sendRecords)
            }
        )
    }

    if (showAddFavoriteDialog) {
        AddFavoriteDialog(
            onDismiss = { showAddFavoriteDialog = false },
            onSave = { newFavorite ->
                favorites = favorites + newFavorite
                FavoritesStorage.saveFavorites(favorites)
                showAddFavoriteDialog = false
            },
            initialServiceAccountPath = serviceAccountPath,
            initialProjectId = projectId
        )
    }
}

@Composable
fun NotificationForm(
    modifier: Modifier,
    serviceAccountPath: String,
    onServiceAccountPathChange: (String) -> Unit,
    projectId: String,
    onProjectIdChange: (String) -> Unit,
    targetDeviceToken: String,
    onTargetDeviceTokenChange: (String) -> Unit,
    externalJsonData: String,
    onExternalJsonDataChange: (String) -> Unit,
    onBrowseServiceAccountPath: () -> Unit,
    onSendNotification: () -> Unit,
    sendStatusCode: Int?,
    isSendSuccessful: Boolean?
) {
    Column(
        modifier = modifier
            .background(color = AppTheme.colorScheme.main_2)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabeledTextField(
                label = stringResource(Res.string.service_account_json_path),
                value = serviceAccountPath,
                onValueChange = onServiceAccountPathChange,
                fieldWeight = 0.65f,
                additionalContent = {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = orange_a80),
                        onClick = onBrowseServiceAccountPath
                    ) {
                        Text(
                            color = AppTheme.colorScheme.main_3,
                            text = stringResource(Res.string.browse),
                            style = body_sm
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))


            LabeledTextField(
                label = stringResource(Res.string.project_id),
                value = projectId,
                onValueChange = onProjectIdChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextField(
                label = stringResource(Res.string.target_device_token),
                value = targetDeviceToken,
                onValueChange = onTargetDeviceTokenChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextField(
                label = stringResource(Res.string.external_json_data),
                value = externalJsonData,
                onValueChange = onExternalJsonDataChange,
                isSingleLine = false,
                isExpandable = true,
                modifier = Modifier.weight(1f)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.3f),
            colors = ButtonDefaults.buttonColors(backgroundColor = orange_a80),
            onClick = onSendNotification
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(Res.string.send_notification),
                style = body_md,
                color = AppTheme.colorScheme.main_3
            )
        }

        if (sendStatusCode != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Status Code: $sendStatusCode",
                    color = AppTheme.colorScheme.main_7,
                    style = body_md
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = if (isSendSuccessful == true) Color.Green else Color.Red,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

fun createCombinedJsonMessage(
    externalJsonData: String
): String {
    return try {
        val parsedJson = json.parseToJsonElement(externalJsonData).jsonObject
        json.encodeToString(JsonObject.serializer(), parsedJson)
    } catch (e: Exception) {
        println("Invalid JSON format in externalJsonData: ${e.message}")
        externalJsonData // 如果解析失敗，返回原始格式的 externalJsonData
    }
}

@Composable
fun FavoritesAndRecordsPanel(
    modifier: Modifier,
    favorites: List<FavoriteItem>,
    onFavoriteClick: (FavoriteItem) -> Unit,
    onFavoriteDelete: (FavoriteItem) -> Unit,
    onAddFavoriteClick: () -> Unit,
    sendRecords: List<SendRecord>,
    onRecordClick: (SendRecord) -> Unit,
    onDeleteAllRecords: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = AppTheme.colorScheme.main_3)
            .padding(16.dp)
    ) {
        FavoritesPanel(
            modifier = Modifier.weight(0.5f),
            favorites = favorites,
            onFavoriteClick = onFavoriteClick, // 用收藏內容替換左側面板
            onFavoriteDelete = onFavoriteDelete,
            onAddFavoriteClick = onAddFavoriteClick
        )

        RecordsPanel(
            modifier = Modifier.weight(0.5f),
            sendRecords = sendRecords,
            onRecordClick = onRecordClick, // 用紀錄內容替換左側面板
            onDeleteAllRecords = onDeleteAllRecords
        )
    }
}