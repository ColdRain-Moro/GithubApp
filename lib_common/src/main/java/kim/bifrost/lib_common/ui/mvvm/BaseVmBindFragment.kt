package kim.bifrost.lib_common.ui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass
import kim.bifrost.lib_common.extensions.lazyUnlock
import kim.bifrost.lib_common.ui.BaseBindFragment

/**
 * kim.bifrost.lib_common.ui.mvvm.BaseVmBindFragment
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/14 11:49
 */
abstract class BaseVmBindFragment<VM: ViewModel, VB: ViewBinding> : BaseBindFragment<VB>() {
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