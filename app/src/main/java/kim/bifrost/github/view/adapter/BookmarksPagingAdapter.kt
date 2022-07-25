package kim.bifrost.github.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemRepoBinding
import kim.bifrost.github.databinding.ItemUserBinding
import kim.bifrost.github.repository.database.entity.BookmarksQueryResult
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.visible

/**
 * kim.bifrost.github.view.adapter.LocalRepoPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 10:41
 */
class BookmarksPagingAdapter(private val onClick: ViewBinding.(data: BookmarksQueryResult) -> Unit) : BasePagingAdapter<ViewBinding, BookmarksQueryResult>() {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        return when (viewType) {
            ITEM_REPO -> ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ITEM_USER -> ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override val holderInit: Holder<ViewBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                val data = getItem(bindingAdapterPosition)!!
                binding.onClick(data)
            }
        }

    override fun onBindViewHolder(holder: Holder<ViewBinding>, position: Int) {
        val data = getItem(position)!!
        when (getItemViewType(position)) {
            ITEM_REPO -> {
                (holder.binding as ItemRepoBinding).apply {
                    val repo = data.repo!!
                    tvRepo.text = repo.name
                    tvAuthor.text = repo.owner
                    tvDesc.text = repo.desc
                    if (repo.language != null) {
                        llLang.visible()
                        tvLang.text = repo.language
                        if (repo.languageColor != null) {
                            ivDot.setColorFilter(Color.parseColor(repo.languageColor))
                        }
                    } else {
                        llLang.gone()
                    }
                    tvStars.text = repo.star.toString()
                    tvForks.text = repo.forks.toString()
                    Glide.with(ivAvatar)
                        .load(repo.avatarUrl)
                        .into(ivAvatar)
                }
            }
            ITEM_USER -> {
                (holder.binding as ItemUserBinding).apply {
                    val user = data.user!!
                    tvName.text = user.name
                    Glide.with(ivAvatar)
                        .load(user.avatarUrl)
                        .into(ivAvatar)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.entity.type == "repo") {
            ITEM_REPO
        } else {
            ITEM_USER
        }
    }

    companion object {
        private const val ITEM_USER = 0
        private const val ITEM_REPO = 1
    }
}