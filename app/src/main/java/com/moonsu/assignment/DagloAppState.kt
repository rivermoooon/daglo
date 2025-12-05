package com.moonsu.assignment

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberDagloAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): DagloAppState {
    return remember(navController, snackbarHostState) {
        DagloAppState(
            navController = navController,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Stable
class DagloAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
)
