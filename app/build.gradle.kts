import kim.bifrost.build_logic.dependency.*
import kim.bifrost.build_logic.dependency.module.dependModule

plugins {
    id("app-module")
}

dependAndroidKtx()
dependAndroidView()
dependNetwork()
dependCoroutines()
dependGlide()
dependLifecycleKtx()
dependModule(":lib_common")