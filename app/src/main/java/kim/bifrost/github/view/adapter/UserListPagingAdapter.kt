package kim.bifrost.github.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemUserBinding
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter

/**
 * kim.bifrost.github.view.adapter.UserListPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 14:55
 */
class UserListPagingAdapter(context: Context, private val onClick: (data: User) -> Unit) : BasePagingAdapter<ItemUserBinding, User>() {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemUserBinding {
        return ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val holderInit: Holder<ItemUserBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                val data = getItem(bindingAdapterPosition)!!
                onClick(data)
            }
        }

    override fun onBindViewHolder(holder: Holder<ItemUserBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            Glide.with(ivAvatar)
                .load(data.avatarUrl)
                .into(ivAvatar)
            tvName.text = data.login
        }
    }
}