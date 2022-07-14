package kim.bifrost.build_logic.dependency.module

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * kim.bifrost.build_logic.dependency.module.Module
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 22:49
 */
fun Project.dependModule(name: String) {
    dependencies {
        "implementation"(project(name))
    }
}