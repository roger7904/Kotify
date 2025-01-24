package org.roger.kotify.ui.screens.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import org.roger.kotify.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel
import org.roger.kotify.ui.theme.body_sm_bold

@Composable
fun IosScreen(
    viewModel: IosViewModel = koinViewModel<IosViewModel>(),
    navigator: NavHostController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colorScheme.main_2),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ios 推播待實作",
            textAlign = TextAlign.Companion.Center,
            color = AppTheme.colorScheme.main_10,
            style = body_sm_bold,
        )
    }
}