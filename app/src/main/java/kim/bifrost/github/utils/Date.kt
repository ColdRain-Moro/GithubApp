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
    val calendar = Calendar.getInstance()
    val currentCalendar = Calendar.getInstance()
    calendar.time = this
    val date = calendar.get(Calendar.DATE)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    val currentDate = currentCalendar.get(Calendar.DATE)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentYear = currentCalendar.get(Calendar.YEAR)
    return when {
        date == currentDate && month == currentMonth && year == currentYear -> "Today"
        date == currentDate - 1 && month == currentMonth && year == currentYear -> "Yesterday"
        else -> asEnglishString()
    }
}