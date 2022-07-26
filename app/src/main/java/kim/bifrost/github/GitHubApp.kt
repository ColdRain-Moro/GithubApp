package kim.bifrost.github

import android.os.Handler
import android.os.Looper
import kim.bifrost.lib_common.BaseApp
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toast
import java.lang.Exception

/**
 * kim.bifrost.github.GitHubApp
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 14:28
 */
class GitHubApp : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        // 全局抓取异常
        // debug的时候还是注释掉吧
        Handler(mainLooper).post {
            // 最多抓取三次异常
            repeat(3) {
                try {
                    Looper.loop()
                } catch (e: Exception) {
                    e.asString().toast()
                    e.printStackTrace()
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(GitHubAppExceptionHandler())
    }
}