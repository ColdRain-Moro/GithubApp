package kim.bifrost.lib_common.base.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kim.bifrost.lib_common.extensions.ReflectClass
import kim.bifrost.lib_common.extensions.fromJson
import kim.bifrost.lib_common.extensions.gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * kim.bifrost.lib_common.base.ui.BaseActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPortraitScreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        if (isCancelStatusBar) {
            cancelStatusBar()
        }
        // 进行依赖注入
//        inject()
    }

//    private fun inject() {
//        ReflectClass(this::class.java).savingField.forEach {
//            if (it.isAnnotationPresent(AutoWired::class.java)) {
//                it.isAccessible = true
//                val annotation = it.getAnnotation(AutoWired::class.java)!!
//                val name = annotation.name.ifEmpty { it.name }
//                val value: Any? = when {
//                    it.type == String::class.java -> intent.getStringExtra(name)
//                    it.type == Int::class.java -> intent.getIntExtra(name, 0)
//                    it.type == Boolean::class.java -> intent.getBooleanExtra(name, false)
//                    it.type == Long::class.java -> intent.getLongExtra(name, 0)
//                    it.type == Float::class.java -> intent.getFloatExtra(name, 0f)
//                    it.type == Double::class.java -> intent.getDoubleExtra(name, 0.0)
//                    it.type.isEnum -> it.type.enumConstants.firstOrNull { e -> e.toString() == intent.getStringExtra(name)?.uppercase() }
//                    Parcelable::class.java.isAssignableFrom(it.type) -> intent.getParcelableExtra(name)
//                    Serializable::class.java.isAssignableFrom(it.type) -> intent.getSerializableExtra(name)
//                    else -> intent.getStringExtra(name)?.let { s -> gson.fromJson(s, it.type) }
//                }
//                it.set(this, value)
//            }
//        }
//    }

    private fun cancelStatusBar() {
        val window = this.window
        val decorView = window.decorView
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = ViewCompat.getWindowInsetsController(decorView)
        windowInsetsController?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 替换 Fragment 的正确用法。
     * 如果不按照正确方式使用，会造成 ViewModel 失效，
     * 你可以写个 demo 看看在屏幕翻转后 Fragment 的 ViewModel 的 hashcode() 值是不是同一个
     */
    protected inline fun <reified F : Fragment> replaceFragment(@IdRes id: Int, func: () -> F): F {
        var fragment = supportFragmentManager.findFragmentById(id)
        if (fragment !is F) {
            fragment = func.invoke()
            supportFragmentManager.commit {
                replace(id, fragment)
            }
        }
        return fragment
    }

    /**
     * 结合生命周期收集 Flow 方法
     */
    fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit) {
        lifecycleScope.launch {
            collect { action.invoke(it) }
        }
    }
}

//@Target(AnnotationTarget.FIELD)
//annotation class AutoWired(val name: String = "")