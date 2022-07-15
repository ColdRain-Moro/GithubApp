package kim.bifrost.lib_common

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper

/**
 * kim.bifrost.lib_common.BaseApp
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:10
 */
open class BaseApp : Application() {
    @CallSuper
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}