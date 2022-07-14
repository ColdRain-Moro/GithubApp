package kim.bifrost.build_logic.plugin

import com.android.build.gradle.LibraryExtension
import kim.bifrost.build_logic.dependency.dependAndroidBase
import kim.bifrost.build_logic.plugin.base.BaseModulePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure


/**
 * kim.bifrost.build_logic.plugin.LibModulePlugin
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 19:54
 */
class LibModulePlugin : BaseModulePlugin() {
    override fun Project.initPlugins() {
        apply(plugin = "com.android.library")
        apply(plugin = "kotlin-android")
        apply(plugin = "kotlin-kapt")
    }

    override fun Project.applyPlugin() {
        dependAndroidBase()
    }

    override fun Project.initAndroid() {
        extensions.configure<LibraryExtension> {
            uniformConfigAndroid()
        }
    }
}