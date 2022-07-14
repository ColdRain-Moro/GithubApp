package kim.bifrost.lib_common.ui.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass
import kim.bifrost.lib_common.extensions.lazyUnlock
import kim.bifrost.lib_common.ui.BaseFragment

/**
 * kim.bifrost.lib_common.ui.mvvm.BaseVmFragment
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/14 11:32
 */
abstract class BaseVmFragment<VM: ViewModel> : BaseFragment() {
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