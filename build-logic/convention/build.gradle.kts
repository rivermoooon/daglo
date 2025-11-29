plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "moonsu.android.hilt"
            implementationClass = "com.moonsu.assignment.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "moonsu.kotlin.hilt"
            implementationClass = "com.moonsu.assignment.HiltKotlinPlugin"
        }
    }
}