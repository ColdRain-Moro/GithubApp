package kim.bifrost.build_logic

import org.gradle.api.Project

/**
 * kim.bifrost.build_logic.Config
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 20:51
 */
object Config {

    fun getApplicationId(project: Project): String {
        return when (project.name) {
            "app" -> baseAppId
            else -> "$baseAppId.${project.name}"
        }
    }

    private const val baseAppId = "kim.bifrost.github"
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val targetSdk = 31
    const val compileSdk = 31
}