package com.moonsu.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonsu.assignment.core.navigation.NavigationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigationHelper: NavigationHelper

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val savedTheme by viewModel.savedTheme.collectAsStateWithLifecycle()
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = savedTheme ?: systemTheme

            DagloApp(
                navigationHelper = navigationHelper,
                isDarkTheme = isDarkTheme,
            )
        }
    }
}
