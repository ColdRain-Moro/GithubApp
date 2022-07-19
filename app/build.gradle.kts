import kim.bifrost.build_logic.dependency.*
import kim.bifrost.build_logic.dependency.module.dependModule

plugins {
    id("app-module")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

dependAndroidKtx()
dependAndroidView()
dependNetwork()
dependNetworkInternal()
dependCoroutines()
dependGlide()
dependLifecycleKtx()
dependPaging()
dependModule(":lib_common")

dependencies {
    // markdown解析
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:image-glide:4.6.2")
    implementation("io.noties.markwon:linkify:4.6.2")
}
