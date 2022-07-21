package kim.bifrost.github.repository.database

import androidx.room.TypeConverter
import java.util.*

/**
 * kim.bifrost.github.repository.database.Converters
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 12:48
 */
class Converters {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}