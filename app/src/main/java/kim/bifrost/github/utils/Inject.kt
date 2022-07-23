package kim.bifrost.github.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kim.bifrost.lib_common.extensions.gson
import java.io.Serializable

/**
 * kim.bifrost.github.utils.Inject
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/23 20:57
 */
inline fun <reified T> Intent.getValue(name: String): T {
    val res: Any? = when(T::class.java) {
        String::class.java -> getStringExtra(name)
        Int::class.java -> getIntExtra(name, 0)
        Boolean::class.java -> getBooleanExtra(name, false)
        Long::class.java -> getLongExtra(name, 0)
        Float::class.java -> getFloatExtra(name, 0f)
        Double::class.java -> getDoubleExtra(name, 0.0)
        else -> {
            if (T::class.java.isEnum) {
                T::class.java.enumConstants?.firstOrNull { e -> e.toString() == getStringExtra(name)?.uppercase() }
            } else if (Parcelable::class.java.isAssignableFrom(T::class.java)) {
                getParcelableExtra(name)
            } else if (Serializable::class.java.isAssignableFrom(T::class.java)) {
                getSerializableExtra(name)
            } else {
                getStringExtra(name)?.let { s -> gson.fromJson(s, T::class.java) }
            }
        }
    }
    return res as T
}

inline fun <reified T> Bundle.getValue(name: String): T {
    val res: Any? = when(T::class.java) {
        String::class.java -> getString(name)
        Int::class.java -> getInt(name, 0)
        Boolean::class.java -> getBoolean(name, false)
        Long::class.java -> getLong(name, 0)
        Float::class.java -> getFloat(name, 0f)
        Double::class.java -> getDouble(name, 0.0)
        else -> {
            if (T::class.java.isEnum) {
                T::class.java.enumConstants?.firstOrNull { e -> e.toString() == getString(name)?.uppercase() }
            } else if (Parcelable::class.java.isAssignableFrom(T::class.java)) {
                getParcelable(name)
            } else if (Serializable::class.java.isAssignableFrom(T::class.java)) {
                getSerializable(name)
            } else {
                getString(name)?.let { s -> gson.fromJson(s, T::class.java) }
            }
        }
    }
    return res as T
}