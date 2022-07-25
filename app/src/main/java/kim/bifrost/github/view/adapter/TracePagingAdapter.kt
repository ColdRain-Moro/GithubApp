package kim.bifrost.github.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemDividerBinding
import kim.bifrost.github.databinding.ItemRepoBinding
import kim.bifrost.github.databinding.ItemUserBinding
import kim.bifrost.github.repository.database.entity.LocalRepoEntity
import kim.bifrost.github.repository.database.entity.LocalUserEntity
import kim.bifrost.github.utils.asWordOrFormattedString
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.visible
import java.util.*

/**
 * kim.bifrost.github.view.adapter.TracePagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/21 20:55
 */
class TracePagingAdapter(
    private val onUserClick: ItemUserBinding.(LocalUserEntity) -> Unit,
    private val onRepoClick: ViewBinding.(LocalRepoEntity) -> Unit,
) : BasePagingAdapter<ViewBinding, TraceItem>() {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        return when (viewType) {
            ITEM_USER -> ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ITEM_REPO -> ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ITEM_DIVIDER -> ItemDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> error("")
        }
    }

    override val holderInit: Holder<ViewBinding>.() -> Unit
        get() = {
            binding.root.setOnClickListener {
                when (val item = getItem(bindingAdapterPosition)) {
                    is TraceItem.User -> (binding as ItemUserBinding).onUserClick(item.user)
                    is TraceItem.Repo -> binding.onRepoClick(item.repo)
                    else -> {}
                }
            }
        }

    override fun onBindViewHolder(holder: Holder<ViewBinding>, position: Int) {
        when (val data = getItem(position)!!) {
            is TraceItem.User -> {
                (holder.binding as ItemUserBinding).apply {
                    Glide.with(ivAvatar)
                        .load(data.user.avatarUrl)
                        .into(ivAvatar)
                    tvName.text = data.user.name
                }
            }
            is TraceItem.Repo -> {
                (holder.binding as ItemRepoBinding).apply {
                    val repo = data.repo
                    tvRepo.text = repo.name
                    tvAuthor.text = repo.owner
                    tvDesc.text = repo.desc
                    if (repo.language != null) {
                        llLang.visible()
                        tvLang.text = repo.language
                        if (repo.languageColor != null) {
                            ivDot.setColorFilter(Color.parseColor(repo.languageColor))
                        } else {
                            ivDot.clearColorFilter()
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
            is TraceItem.Divider -> {
                (holder.binding as ItemDividerBinding).apply {
                    tvTime.text = data.date.asWordOrFormattedString()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)!!) {
            is TraceItem.User -> ITEM_USER
            is TraceItem.Repo -> ITEM_REPO
            is TraceItem.Divider -> ITEM_DIVIDER
        }
    }

    companion object {
        const val ITEM_USER = 0
        const val ITEM_REPO = 1
        const val ITEM_DIVIDER = 2
    }
}

sealed interface TraceItem {
    data class User(val user: LocalUserEntity): TraceItem
    data class Repo(val repo: LocalRepoEntity): TraceItem
    data class Divider(val date: Date): TraceItem
}