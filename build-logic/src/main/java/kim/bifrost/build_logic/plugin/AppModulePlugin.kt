@file:Suppress("UnstableApiUsage")

package kim.bifrost.build_logic.plugin

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import kim.bifrost.build_logic.Config
import kim.bifrost.build_logic.dependency.dependAndroidBase
import kim.bifrost.build_logic.plugin.base.BaseModulePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

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
    }

    override fun Project.applyPlugin() {
        extensions.configure<BaseAppModuleExtension> {
            defaultConfig {
                applicationId = Config.getApplicationId(project)
                versionCode = Config.versionCode
                versionName = Config.versionName
                targetSdk = Config.targetSdk
            }
        }
        dependAndroidBase()
    }

    override fun Project.initAndroid() {
        extensions.configure<BaseAppModuleExtension> {
            uniformConfigAndroid()
        }
    }
}