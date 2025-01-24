package org.roger.kotify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.roger.kotify.ui.screens.notification.NotificationSenderScreen
import org.roger.kotify.ui.screens.ios.IosScreen

@Composable
fun Navigation(
    navHostController: NavHostController,
) {
    NavHost(navController = navHostController, startDestination = NavigationItem.Notification.route) {
        composable(NavigationItem.Notification.route) {
            NotificationSenderScreen()
        }

        composable(NavigationItem.Ios.route) {
            IosScreen(navigator = navHostController)
        }
    }
}
