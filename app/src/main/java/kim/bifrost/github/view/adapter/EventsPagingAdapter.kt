package kim.bifrost.github.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemEventBinding
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.github.repository.network.model.event.handleEvent
import kim.bifrost.lib_common.base.adapter.BaseItemCallback
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.utils.getNewsTimeStr

/**
 * kim.bifrost.github.view.adapter.EventsPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 12:13
 */
class EventsPagingAdapter(
    private val onClickBody: ItemEventBinding.(Event) -> Unit,
    private val onClickAvatar: ItemEventBinding.(Event) -> Unit
) : BasePagingAdapter<ItemEventBinding, Event>(
    BaseItemCallback { item1, item2 ->
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
                    binding.onClickBody(data)
                }
                binding.ivAvatar.setOnClickListener {
                    val data = getItem(bindingAdapterPosition)!!
                    onClickAvatar(data)
                }
            }
        }

    override fun onBindViewHolder(holder: Holder<ItemEventBinding>, position: Int) {
        val data = getItem(position)!!
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