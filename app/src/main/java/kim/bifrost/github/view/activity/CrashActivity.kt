package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kim.bifrost.github.ProcessPhoenix
import kim.bifrost.lib_common.BaseApp
import kim.bifrost.lib_common.base.ui.BaseActivity
import kotlin.system.exitProcess

/**
 * kim.bifrost.github.view.activity.CrashActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/26 11:03
 */
class CrashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaterialAlertDialogBuilder(this)
            .setTitle("哦豁 App崩溃了!")
            .setMessage("好似，开香槟🍾")
            .setPositiveButton("重启应用") { _, _ ->
                ProcessPhoenix.triggerRebirth(this)
            }
            .setNegativeButton("退出应用") { _, _ ->
                exitProcess(0)
            }.show()
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, CrashActivity::class.java)
            // 清空返回栈
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(starter)
            // 退出已经崩溃的app进程
            exitProcess(0)
        }
    }
}