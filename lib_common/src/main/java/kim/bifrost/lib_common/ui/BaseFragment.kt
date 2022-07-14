package kim.bifrost.lib_common.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * kim.bifrost.lib_common.ui.BaseFragment
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:58
 */
abstract class BaseFragment : Fragment() {
    /**
     * 结合生命周期收集 Flow 方法
     */
    protected fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit) {
        lifecycleScope.launch {
            flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { action.invoke(it) }
        }
    }

}