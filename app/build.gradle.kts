import kim.bifrost.build_logic.dependency.*
import kim.bifrost.build_logic.dependency.module.dependModule

plugins {
    id("app-module")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

buildscript {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.github.whataa:pandora-plugin:1.0.0")
    }
}

apply(plugin = "pandora-plugin")

dependAndroidKtx()
dependAndroidView()
dependNetwork()
dependNetworkInternal()
dependCoroutines()
dependGlide()
dependLifecycleKtx()
dependPaging()
dependRoomPaging()
dependRoom()
dependModule(":lib_common")
dependModule(":ksp_inject:annotations")

dependencies {
    // markdown解析
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:image-glide:4.6.2")
    implementation("io.noties.markwon:linkify:4.6.2")
    implementation("com.github.whataa:pandora:androidx_v2.1.0")
    ksp(project(":ksp_inject"))
}
