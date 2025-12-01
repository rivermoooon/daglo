package com.moonsu.assignment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.feature.characterNavigation

@Composable
fun AppNavHost(
    appState: DagloAppState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = appState.navController,
        startDestination = DagloRoute.CharacterList,
        modifier = modifier,
    ) {
        characterNavigation()
    }
}
