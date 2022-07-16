package kim.bifrost.lib_common.base.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass
import kim.bifrost.lib_common.extensions.lazyUnlock
import kim.bifrost.lib_common.base.ui.BaseActivity

/**
 * kim.bifrost.lib_common.base.ui.mvvm.BaseVmActivity
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/14 11:19
 */
abstract class BaseVmActivity<VM: ViewModel>(
    isPortraitScreen: Boolean = true,
    isCancelStatusBar: Boolean = true,
) : BaseActivity(isPortraitScreen, isCancelStatusBar) {
    protected val viewModel by lazyUnlock {
        val clazz = getGenericClassFromSuperClass<VM, ViewModel>(javaClass)
        if (viewModelFactory == null) {
            ViewModelProvider(this)[clazz]
        } else {
            ViewModelProvider(this, viewModelFactory!!)[clazz]
        }
    }

    protected open val viewModelFactory: ViewModelProvider.Factory? = null
}