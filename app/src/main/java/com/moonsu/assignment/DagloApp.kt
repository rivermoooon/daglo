package com.moonsu.assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.moonsu.assignment.core.designsystem.component.DagloSnackBarHost
import com.moonsu.assignment.core.designsystem.component.SnackBarType
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper

@Composable
fun DagloApp(
    appState: DagloAppState = rememberDagloAppState(),
    navigationHelper: NavigationHelper,
    isDarkTheme: Boolean,
) {
    LaunchedEffect(navigationHelper, appState.navController) {
        navigationHelper.navigationFlow.collect { event ->
            when (event) {
                is NavigationEvent.To -> {
                    appState.navController.navigate(event.route) {
                        launchSingleTop = true
                    }
                }

                is NavigationEvent.Up -> {
                    appState.navController.navigateUp()
                }

                is NavigationEvent.TopLevelTo -> {
                    appState.navController.navigate(event.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is NavigationEvent.PopUpTo -> {
                    val target = event.routeKClass.qualifiedName
                    if (target != null) {
                        appState.navController.popBackStack(
                            route = target,
                            inclusive = event.inclusive,
                        )
                    }
                }
            }
            appState.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    DagloTheme(darkTheme = isDarkTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppNavHost(
                appState = appState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(DagloTheme.colors.background)
                    .windowInsetsPadding(WindowInsets.systemBars),
            )

            DagloSnackBarHost(
                hostState = appState.snackbarHostState,
                type = SnackBarType.ERROR,
                duration = 3000L,
            )
        }
    }
}
