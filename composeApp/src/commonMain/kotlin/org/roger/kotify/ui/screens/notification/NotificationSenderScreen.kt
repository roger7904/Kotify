package org.roger.kotify.ui.screens.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import io.ktor.client.*
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@NonRestartableComposable
expect fun NotificationSenderScreen(
    viewModel: NotificationSenderViewModel = koinViewModel<NotificationSenderViewModel>(),
    httpClient: HttpClient = koinInject(),
)