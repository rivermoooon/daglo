package com.moonsu.assignment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper

@Composable
fun DagloApp(
    appState: DagloAppState = rememberDagloAppState(),
    navigationHelper: NavigationHelper,
) {
    val snackBarHostState = remember { SnackbarHostState() }

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
            snackBarHostState.currentSnackbarData?.dismiss()
        }
    }

    DagloTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        ) { innerPadding ->
            AppNavHost(
                appState = appState,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
