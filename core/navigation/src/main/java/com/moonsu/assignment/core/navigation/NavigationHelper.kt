package com.moonsu.assignment.core.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class NavigationHelper @Inject constructor() {
    private val _navigationFlow = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationFlow = _navigationFlow.receiveAsFlow()

    fun navigate(event: NavigationEvent) {
        _navigationFlow.trySend(event)
    }
}

sealed class NavigationEvent {
    data class To(val route: DagloRoute) : NavigationEvent()
    data class Up(val inclusive: Boolean = false) : NavigationEvent()
    data class TopLevelTo(val route: DagloRoute) : NavigationEvent()
    data class PopUpTo(val routeKClass: KClass<*>, val inclusive: Boolean) : NavigationEvent()
}
