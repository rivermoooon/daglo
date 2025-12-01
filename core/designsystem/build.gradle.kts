import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.library")
    id("moonsu.android.compose")
}

android {
    setNamespace("core.designsystem")
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.coil.svg)
}
