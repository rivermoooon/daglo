import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.library")
    id("moonsu.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.network")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(projects.data)
}
