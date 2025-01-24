package org.roger.kotify.ui.navigation

sealed class NavigationItem(val route: String) {
    data object Main : NavigationItem("main")
    data object Notification : NavigationItem("notification")
    data object Ios : NavigationItem("ios")
}