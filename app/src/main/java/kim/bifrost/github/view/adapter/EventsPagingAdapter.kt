package kim.bifrost.github.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemEventBinding
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.github.repository.network.model.event.handleEvent
import kim.bifrost.github.view.activity.RepositoryActivity
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
class EventsPagingAdapter(context: Context, private val onClick: (Event) -> Unit) : BasePagingAdapter<ItemEventBinding, Event>(
    context,
    BaseItemCallback { item1, item2 -> item1.id == item2.id }
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

    override fun onBindViewHolder(holder: Holder<ItemEventBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            Glide.with(ivAvatar)
                .load(data.actor.avatarUrl)
                .into(ivAvatar)
            tvAuthor.text = data.actor.displayLogin
            handleEvent(data, context)
            tvTime.text = getNewsTimeStr(data.createdAt)
        }
    }
}