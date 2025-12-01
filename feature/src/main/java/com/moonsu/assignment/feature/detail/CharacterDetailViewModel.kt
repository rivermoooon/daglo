package com.moonsu.assignment.feature.detail

import androidx.lifecycle.ViewModel
import com.moonsu.assignment.core.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
) : ViewModel() {
    // TODO: 구현
}
