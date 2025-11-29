package com.moonsu.assignment

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.moonsu.assignment.$name"
    }
}