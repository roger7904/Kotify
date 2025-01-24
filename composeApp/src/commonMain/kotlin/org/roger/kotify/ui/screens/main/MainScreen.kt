package org.roger.kotify.ui.screens.main

import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.roger.kotify.ui.navigation.Navigation
import org.roger.kotify.ui.navigation.TopLevelDestinations
import org.roger.kotify.ui.theme.AppTheme
import org.roger.kotify.ui.theme.orange_500
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel<MainViewModel>()) {
    val innerNavController = rememberNavController()

    val currentDestination =
        innerNavController.currentBackStackEntryAsState().value?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme(darkTheme = true) {
        val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
            navigationRailItemColors = NavigationRailItemDefaults.colors(
                selectedIconColor = orange_500,
                selectedTextColor = orange_500,
                unselectedIconColor = AppTheme.colorScheme.main_7,
                unselectedTextColor = AppTheme.colorScheme.main_7,
                indicatorColor = AppTheme.colorScheme.main_3,
            ),
        )

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                TopLevelDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(it.label),
                            )
                        },
                        selected = it.route == currentDestination,
                        colors = myNavigationSuiteItemColors,
                        onClick = {
                            if (it.route != currentDestination) {
                                innerNavController.navigate(route = it.route) {
                                    popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            },
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationRailContainerColor = AppTheme.colorScheme.main_1,
                navigationRailContentColor = AppTheme.colorScheme.main_1,
            )
        ) {
            SnackbarHost(hostState = snackbarHostState)

            Navigation(
                navHostController = innerNavController
            )
        }
    }
}