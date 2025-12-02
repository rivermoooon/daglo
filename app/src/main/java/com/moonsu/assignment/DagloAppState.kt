package com.moonsu.assignment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberDagloAppState(
    navController: NavHostController = rememberNavController(),
): DagloAppState {
    return remember(navController) {
        DagloAppState(navController = navController)
    }
}

@Stable
class DagloAppState(
    val navController: NavHostController,
)
