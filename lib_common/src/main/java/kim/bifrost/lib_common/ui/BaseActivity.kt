package kim.bifrost.lib_common.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * kim.bifrost.lib_common.ui.BaseActivity
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:18
 */
abstract class BaseActivity(
    private val isPortraitScreen: Boolean = true,
    private val isCancelStatusBar: Boolean = true
) : AppCompatActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (isPortraitScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        if (isCancelStatusBar) {
            cancelStatusBar()
        }
    }

    private fun cancelStatusBar() {
        val window = this.window
        val decorView = window.decorView
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = ViewCompat.getWindowInsetsController(decorView)
        windowInsetsController?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 结合生命周期收集 Flow 方法
     */
    fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collect { action.invoke(it) }
        }
    }
}