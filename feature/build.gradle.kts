import com.moonsu.assignment.setNamespace

plugins {
    id("moonsu.android.feature")
}

android {
    setNamespace("feature")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.domain)
}
