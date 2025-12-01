
plugins {
    id("moonsu.android.application")
    id("moonsu.android.compose")
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.moonsu.assignment"

    defaultConfig {
        versionCode = 1
        versionName = "1.0"
        targetSdk = 35
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.feature)
}
