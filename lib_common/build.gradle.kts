import kim.bifrost.build_logic.dependency.*

plugins {
    id("lib-module")
}

dependCoroutines()
dependLifecycleKtx()
dependAndroidKtx()
dependNetwork()