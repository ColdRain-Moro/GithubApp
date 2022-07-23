@file:Suppress("UnstableApiUsage")

include(":ksp_inject:annotations")


pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "GitHubApp"

include(":app")
include(":lib_common")
include(":ksp_inject")