package org.roger.kotify

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.roger.kotify.di.initKoin
import org.roger.kotify.ui.screens.main.MainScreen
import org.roger.kotify.ui.navigation.NavigationItem
import org.roger.kotify.ui.theme.AppTheme
import org.koin.core.Koin

lateinit var koin: Koin

fun main() = application {
    koin = initKoin().koin

    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(1200.dp, 700.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Kotify",
    ) {
        val navController = rememberNavController()

        AppTheme(darkTheme = true) {
            NavHost(navController = navController, startDestination = NavigationItem.Main.route) {
                composable(NavigationItem.Main.route) {
                    MainScreen()
                }
            }
        }
    }
}