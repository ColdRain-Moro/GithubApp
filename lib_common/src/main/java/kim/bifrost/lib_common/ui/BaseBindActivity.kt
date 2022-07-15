package kim.bifrost.lib_common.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass
import kim.bifrost.lib_common.extensions.lazyUnlock

/**
 * kim.bifrost.lib_common.ui.BaseBindActivity
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:34
 */
abstract class BaseBindActivity<VB: ViewBinding>(
    isPortraitScreen: Boolean = true,
    isCancelStatusBar: Boolean = true
) : BaseActivity(isPortraitScreen, isCancelStatusBar) {
    @Suppress("UNCHECKED_CAST")
    protected val binding by lazyUnlock {
        getGenericClassFromSuperClass<VB, ViewBinding>(javaClass)
            .getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(binding.root)
    }

    @Deprecated(
        "打个标记，因为使用了 ViewBinding，防止你忘记删除这个",
        level = DeprecationLevel.ERROR, replaceWith = ReplaceWith("")
    )
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
}