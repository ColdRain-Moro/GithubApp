package kim.bifrost.lib_common.base.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * kim.bifrost.lib_common.base.adapter.BasePagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:18
 */
abstract class BasePagingAdapter<DB : ViewBinding, D : Any>(
    callback: BaseItemCallback<D> = BaseItemCallback(),
) : PagingDataAdapter<D, BasePagingAdapter.Holder<DB>>(callback) {

    open val holderInit: Holder<DB>.() -> Unit = {}

    class Holder<DB : ViewBinding>(val binding: DB, holderInit: Holder<DB>.() -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            holderInit(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<DB> {
        return Holder(getDataBinding(parent, viewType), holderInit)
    }

    abstract fun getDataBinding(parent: ViewGroup, viewType: Int): DB

    fun getItemOut(position: Int): D? {
        return getItem(position)
    }
}