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
dependModule(":lib_common")
