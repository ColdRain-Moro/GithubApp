package kim.bifrost.lib_common.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * kim.bifrost.lib_common.extensions.Serialization
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 19:40
 */
val gson: Gson by lazy { GsonBuilder().create() }

fun Any.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, T::class.java)
}