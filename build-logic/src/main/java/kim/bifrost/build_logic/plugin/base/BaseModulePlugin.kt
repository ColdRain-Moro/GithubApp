@file:Suppress("UnstableApiUsage")

package kim.bifrost.build_logic.plugin.base

import com.android.build.api.dsl.*
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.model.KotlinAndroidExtension

/**
 * kim.bifrost.build_logic.plugin.base.BaseModulePlugin
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 19:55
 */
abstract class BaseModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            initPlugins()
            initAndroid()
            applyPlugin()
        }
    }

    abstract fun Project.initPlugins()

    abstract fun Project.applyPlugin()

    abstract fun Project.initAndroid()

    protected fun <A : BuildFeatures, B : BuildType, C : DefaultConfig, D : ProductFlavor>
            CommonExtension<A, B, C, D>.uniformConfigAndroid() {

        compileSdk = 31
        defaultConfig {
            minSdk = 21
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
            release {
                // 他妈的，开了混淆就寄了
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
            debug {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        lint {
            // 编译遇到错误不退出
            abortOnError = false
        }

        buildFeatures {
            viewBinding = true
        }

        (this as ExtensionAware).extensions.configure<KotlinJvmOptions> {
            jvmTarget = "1.8"
            // 允许使用context receiver
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }
}