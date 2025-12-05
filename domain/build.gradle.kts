plugins {
    id("moonsu.kotlin.library")
    id("moonsu.kotlin.hilt")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.coroutines.core)
    implementation(libs.androidx.paging.common)
}
