package kim.bifrost.github

import kim.bifrost.github.view.activity.CrashActivity
import kim.bifrost.lib_common.BaseApp.Companion.appContext

/**
 * kim.bifrost.github.GitHubAppExceptionHandler
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/25 21:38
 */
class GitHubAppExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
        CrashActivity.start(appContext)
    }
}