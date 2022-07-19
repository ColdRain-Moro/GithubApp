package kim.bifrost.lib_common.extensions

import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

/**
 * kim.bifrost.lib_common.extensions.Intent
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 14:17
 */
fun Intent.argument(key: String, value: Any?): Intent {
    if (value == null) return this
    return when {
        value.javaClass == String::class.java -> putExtra(key, value as String)
        value.javaClass == Int::class.java -> putExtra(key, value as Int)
        value.javaClass == Boolean::class.java -> putExtra(key, value as Boolean)
        value.javaClass == Long::class.java -> putExtra(key, value as Long)
        value.javaClass == Float::class.java -> putExtra(key, value as Float)
        value.javaClass == Double::class.java -> putExtra(key, value as Double)
        value.javaClass.isEnum -> putExtra(key, value.toString())
        Parcelable::class.java.isAssignableFrom(value.javaClass) -> putExtra(key, value as Parcelable)
        Serializable::class.java.isAssignableFrom(value.javaClass) -> putExtra(key, value as Serializable)
        else -> putExtra(key, value.toJson())
    }
}