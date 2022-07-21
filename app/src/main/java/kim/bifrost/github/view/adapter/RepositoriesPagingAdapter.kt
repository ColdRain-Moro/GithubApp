package kim.bifrost.github.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemRepoBinding
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.view.activity.RepositoryActivity
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.visible

/**
 * kim.bifrost.github.view.adapter.RepositoriesPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 18:36
 */
class RepositoriesPagingAdapter : BasePagingAdapter<ItemRepoBinding, Repository>() {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemRepoBinding {
        return ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val holderInit: Holder<ItemRepoBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                val data = getItem(bindingAdapterPosition)!!
                RepositoryActivity.start(itemView.context, data)
            }
        }

    override fun onBindViewHolder(holder: Holder<ItemRepoBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            tvRepo.text = data.name
            tvAuthor.text = data.owner.login
            tvDesc.text = data.description
            if (data.language != null) {
                llLang.visible()
                tvLang.text = data.language
                if (data.languageColor != null) {
                    ivDot.setColorFilter(Color.parseColor(data.languageColor))
                }
            } else {
                llLang.gone()
            }
            tvStars.text = data.stargazersCount.toString()
            tvForks.text = data.forksCount.toString()
            Glide.with(ivAvatar)
                .load(data.owner.avatarUrl)
                .into(ivAvatar)
        }
    }
}