package org.roger.kotify.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import kotify.composeapp.generated.resources.Res
import kotify.composeapp.generated.resources.notification
import kotify.composeapp.generated.resources.ios
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestinations(
    val route: String,
    val label: StringResource,
    val icon: ImageVector,
) {
    Notification(NavigationItem.Notification.route, Res.string.notification, Icons.Default.Home),
    Ios(NavigationItem.Ios.route,Res.string.ios, Icons.Default.Home),
}