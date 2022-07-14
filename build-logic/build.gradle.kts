plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
}

gradlePlugin {
    plugins {
        create("app-module") {
            id = "app-module"
            implementationClass = "kim.bifrost.build_logic.plugin.AppModulePlugin"
        }
        create("lib-module") {
            id = "lib-module"
            implementationClass = "kim.bifrost.build_logic.plugin.LibModulePlugin"
        }
    }
}