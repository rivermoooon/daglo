package com.moonsu.assignment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DagloApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    // TODO: Timber 초기화 설정
}
