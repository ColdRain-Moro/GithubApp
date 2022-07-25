@file:Suppress("UnstableApiUsage")

package kim.bifrost.build_logic.plugin

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import kim.bifrost.build_logic.Config
import kim.bifrost.build_logic.dependency.dependAndroidBase
import kim.bifrost.build_logic.plugin.base.BaseModulePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.plugins
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

/**
 * kim.bifrost.build_logic.plugin.AppModulePlugin
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 19:58
 */
class AppModulePlugin : BaseModulePlugin() {
    override fun Project.initPlugins() {
        apply(plugin = "com.android.application")
        apply(plugin = "kotlin-android")
        apply(plugin = "kotlin-kapt")
        apply(plugin = "com.google.devtools.ksp")
    }

    override fun Project.applyPlugin() {
        extensions.configure<BaseAppModuleExtension> {
            defaultConfig {
                applicationId = Config.getApplicationId(project)
                versionCode = Config.versionCode
                versionName = Config.versionName
                targetSdk = Config.targetSdk
            }
            buildTypes {
                getByName("release") {
                    sourceSets {
                        getByName("main") {
                            java {
                                srcDirs("build/generated/ksp/release/kotlin")
                            }
                        }
                    }
                }
                getByName("debug") {
                    sourceSets {
                        getByName("main") {
                            java {
                                srcDirs("build/generated/ksp/debug/kotlin")
                            }
                        }
                    }
                }
            }
//            sourceSets {
//                getByName("main") {
//                    java {
//                        srcDirs("build/generated/ksp/debug/kotlin")
//                    }
//                }
//            }
        }
        dependAndroidBase()
        extensions.configure<KaptExtension> {
            arguments {
                arg("room.schemaLocation", "${project.projectDir}/schemas") // room 的架构导出目录
            }
        }
    }

    override fun Project.initAndroid() {
        extensions.configure<BaseAppModuleExtension> {
            uniformConfigAndroid()
        }
    }
}