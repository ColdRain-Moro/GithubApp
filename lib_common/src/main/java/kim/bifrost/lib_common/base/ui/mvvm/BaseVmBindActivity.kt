package kim.bifrost.lib_common.base.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass
import kim.bifrost.lib_common.extensions.lazyUnlock
import kim.bifrost.lib_common.base.ui.BaseBindActivity

/**
 * kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/14 11:49
 */
abstract class BaseVmBindActivity<VM: ViewModel, VB: ViewBinding>(
    isPortraitScreen: Boolean = true,
    isCancelStatusBar: Boolean = true,
) : BaseBindActivity<VB>(isPortraitScreen, isCancelStatusBar) {
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