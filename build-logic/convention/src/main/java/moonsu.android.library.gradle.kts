import com.moonsu.assignment.configureHiltAndroid
import com.moonsu.assignment.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()