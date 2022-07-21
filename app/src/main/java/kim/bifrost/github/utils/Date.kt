package kim.bifrost.github.utils

import kim.bifrost.lib_common.utils.asEnglishString
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * kim.bifrost.github.utils.Date
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 23:41
 */
fun Date.asWordOrFormattedString(): String {
    val distance = System.currentTimeMillis() - time
    return when {
        distance < TimeUnit.DAYS.toMillis(1) -> "Today"
        distance < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
        else -> asEnglishString()
    }
}