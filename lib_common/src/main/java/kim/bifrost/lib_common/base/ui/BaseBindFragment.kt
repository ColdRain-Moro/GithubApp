package kim.bifrost.lib_common.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import kim.bifrost.lib_common.extensions.getGenericClassFromSuperClass

/**
 * kim.bifrost.lib_common.base.ui.BaseBindFragment
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/14 0:37
 */
abstract class BaseBindFragment<VB : ViewBinding> : BaseFragment() {
    /**
     * 由于 View 的生命周期与 Fragment 不匹配，
     * 所以在 [onDestroyView] 后需要取消对 [binding] 的引用
     */
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    @CallSuper
    @Suppress("UNCHECKED_CAST")
    @Deprecated(
        "不建议重写该方法，请使用 onCreateViewBefore() 代替",
        ReplaceWith("onCreateViewBefore(container, savedInstanceState)"),
        DeprecationLevel.WARNING
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if (_binding == null) {
            val method = getGenericClassFromSuperClass<VB, ViewBinding>(javaClass).getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            _binding = method.invoke(null, inflater, container, false) as VB
        }
        onCreateViewBefore(container, savedInstanceState)
        return binding.root
    }

    /**
     * 在 [onCreateView] 中返回 View 前回调
     */
    open fun onCreateViewBefore(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}