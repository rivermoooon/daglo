import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.navigation")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
