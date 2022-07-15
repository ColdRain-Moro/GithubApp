package kim.bifrost.lib_common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * kim.bifrost.lib_common.utils.Date
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:07
 */
fun Date.asString(format: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(this)
}