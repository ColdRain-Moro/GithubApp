package kim.bifrost.lib_common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kim.bifrost.lib_common.extensions.invokeStatic
import java.lang.reflect.ParameterizedType

/**
 * kim.bifrost.lib_common.base.adapter.BaseRvAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:25
 */
abstract class BaseRvAdapter<D, T: ViewBinding>(
    private val items: List<D>,
    private val onHolderInit: BaseRvAdapter<D, T>.Holder.() -> Unit,
    private val onBind: T.(data: D) -> Unit
) : RecyclerView.Adapter<BaseRvAdapter<D, T>.Holder>() {
    inner class Holder(val binding: T) : RecyclerView.ViewHolder(binding.root) {
        init {
            onHolderInit()
        }
    }

    private val clazz by lazy { getGenericClassFromSuperClass<ViewBinding>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(clazz.invokeStatic<T>("inflate", LayoutInflater.from(parent.context), parent, false)!!)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.onBind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T> getGenericClassFromSuperClass(): Class<T> {
        val genericSuperclass = javaClass.genericSuperclass // 得到继承的父类填入的泛型（必须是具体的类型，不能是 T 这种东西）
        if (genericSuperclass is ParameterizedType) {
            val typeArguments = genericSuperclass.actualTypeArguments
            for (type in typeArguments) {
                if (type is Class<*> && T::class.java.isAssignableFrom(type)) {
                    return type as Class<T>
                } else if (type is ParameterizedType) { // 泛型中有泛型时并不为 Class<*>
                    val rawType = type.rawType // 这时 rawType 一定是 Class<*>
                    if (rawType is Class<*> && T::class.java.isAssignableFrom(rawType)) {
                        return rawType as Class<T>
                    }
                }
            }
        }
        throw RuntimeException("你父类的泛型为: $genericSuperclass, 其中不存在 ${T::class.java.simpleName}")
    }

}