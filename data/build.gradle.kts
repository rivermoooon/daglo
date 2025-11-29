import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.library")
    id("moonsu.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("data")
}

dependencies {
    implementation(projects.domain)
    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
}
