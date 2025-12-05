import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.library")
    id("moonsu.android.hilt")
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)
}
