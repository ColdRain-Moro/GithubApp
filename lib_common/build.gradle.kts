import kim.bifrost.build_logic.dependency.dependAndroidKtx
import kim.bifrost.build_logic.dependency.dependCoroutines
import kim.bifrost.build_logic.dependency.dependLifecycleKtx

plugins {
    id("lib-module")
}

dependCoroutines()
dependLifecycleKtx()
dependAndroidKtx()