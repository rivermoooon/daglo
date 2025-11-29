import com.moonsu.assignment.configureHiltAndroid
import com.moonsu.assignment.findLibrary

plugins {
    id("moonsu.android.library")
    id("moonsu.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:designsystem"))
    implementation(findLibrary("kotlinx.serialization.json").get())
    implementation(findLibrary("androidx.compose.navigation").get())
    implementation(findLibrary("androidx.lifecycle.viewModelCompose").get())
    implementation(findLibrary("androidx.lifecycle.runtimeCompose").get())
    androidTestImplementation(findLibrary("androidx.compose.ui.test").get())
    debugImplementation(findLibrary("androidx.compose.ui.test.manifest").get())
}