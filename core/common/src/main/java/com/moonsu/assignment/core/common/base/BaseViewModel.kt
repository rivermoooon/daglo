package com.moonsu.assignment.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, I : UiIntent, SE : SideEffect>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    private val currentState: S get() = _state.value

    private val _intent = Channel<I>(Channel.BUFFERED)
    private val _reducer = Channel<S.() -> S>(Channel.BUFFERED)
    private val _sideEffect = Channel<SE>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        _intent.receiveAsFlow()
            .onEach(::processIntent)
            .launchIn(viewModelScope)

        _reducer.receiveAsFlow()
            .onEach { reduce -> _state.value = currentState.reduce() }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: I) = viewModelScope.launch { _intent.send(intent) }

    protected abstract suspend fun processIntent(intent: I)

    protected fun setState(reduce: S.() -> S) = viewModelScope.launch { _reducer.send(reduce) }

    protected fun postSideEffect(effect: SE) = viewModelScope.launch { _sideEffect.send(effect) }
}
