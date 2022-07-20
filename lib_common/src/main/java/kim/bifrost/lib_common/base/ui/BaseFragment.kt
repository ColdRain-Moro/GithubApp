package kim.bifrost.lib_common.base.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kim.bifrost.lib_common.extensions.ReflectClass
import kim.bifrost.lib_common.extensions.gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * kim.bifrost.lib_common.base.ui.BaseFragment
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:58
 */
abstract class BaseFragment : Fragment() {

    abstract override fun onViewCreated(view: View, savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 结合生命周期收集 Flow 方法
     */
    protected fun <T> Flow<T>.collectLaunch(action: suspend (value: T) -> Unit): Job {
        return lifecycleScope.launch {
            collect { action.invoke(it) }
        }
    }

    private fun inject() {
        ReflectClass(this::class.java).savingField.forEach {
            if (it.isAnnotationPresent(AutoWired::class.java)) {
                it.isAccessible = true
                val annotation = it.getAnnotation(AutoWired::class.java)!!
                val name = annotation.name.ifEmpty { it.name }
                val value: Any? = when {
                    it.type == String::class.java -> arguments?.getString(name)
                    it.type == Int::class.java -> arguments?.getInt(name, 0)
                    it.type == Boolean::class.java -> arguments?.getBoolean(name, false)
                    it.type == Long::class.java -> arguments?.getLong(name, 0)
                    it.type == Float::class.java -> arguments?.getFloat(name, 0f)
                    it.type == Double::class.java -> arguments?.getDouble(name, 0.0)
                    it.type.isEnum -> it.type.enumConstants.firstOrNull { e -> e.toString() == arguments?.getString(name)?.uppercase() }
                    Parcelable::class.java.isAssignableFrom(it.type) -> arguments?.getParcelable(name)
                    Serializable::class.java.isAssignableFrom(it.type) -> arguments?.getSerializable(name)
                    else -> arguments?.getString(name)?.let { s -> gson.fromJson(s, it.type) }
                }
                it.set(this, value)
            }
        }
    }

}