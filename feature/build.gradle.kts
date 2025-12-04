import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.feature")
}

android {
    setNamespace("feature")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.common)
    implementation(projects.domain)
    implementation(libs.androidx.paging.compose)
}
