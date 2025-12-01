package com.moonsu.assignment.feature

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.feature.detail.CharacterDetailRoute
import com.moonsu.assignment.feature.list.CharacterListRoute
import com.moonsu.assignment.feature.search.CharacterSearchRoute

fun NavGraphBuilder.characterNavigation() {
    // 캐릭터 목록 화면
    composable<DagloRoute.CharacterList> {
        CharacterListRoute()
    }

    // 캐릭터 상세 화면
    composable<DagloRoute.CharacterDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<DagloRoute.CharacterDetail>()
        CharacterDetailRoute(characterId = route.characterId)
    }

    // 캐릭터 검색 화면
    composable<DagloRoute.CharacterSearch> {
        CharacterSearchRoute()
    }
}
