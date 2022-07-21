package kim.bifrost.github.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemEventBinding
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.github.repository.network.model.event.handleEvent
import kim.bifrost.lib_common.base.adapter.BaseItemCallback
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.utils.getNewsTimeStr

/**
 * kim.bifrost.github.view.adapter.EventsPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 12:13
 */
class EventsPagingAdapter(private val onClick: (Event) -> Unit) : BasePagingAdapter<ItemEventBinding, Event>(
    BaseItemCallback { item1, item2 ->
        Log.d(TAG, ": ${item1.id} ${item2.id}")
        item1.id == item2.id
    }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemEventBinding {
        return ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val holderInit: Holder<ItemEventBinding>.() -> Unit
        get() = {
            binding.apply {
                binding.root.setOnClickListener {
                    val data = getItem(bindingAdapterPosition)!!
                    onClick(data)
                }
            }
        }

    override fun onBindViewHolder(
        holder: Holder<ItemEventBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                Log.d(TAG, "onBindViewHolder: it = $it")
            }
        }
    }

    override fun onBindViewHolder(holder: Holder<ItemEventBinding>, position: Int) {
        val data = getItem(position)!!
        Log.d(TAG, "onBindViewHolder: $position")
        holder.binding.apply {
            Glide.with(ivAvatar)
                .load(data.actor.avatarUrl)
                .into(ivAvatar)
            tvAuthor.text = data.actor.displayLogin
            handleEvent(data, holder.itemView.context)
            tvTime.text = getNewsTimeStr(data.createdAt)
        }
    }
}