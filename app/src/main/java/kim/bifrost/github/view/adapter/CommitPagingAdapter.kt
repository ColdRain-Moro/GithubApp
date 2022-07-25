package kim.bifrost.github.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemCommitBinding
import kim.bifrost.github.repository.network.model.Commit
import kim.bifrost.lib_common.base.adapter.BaseItemCallback
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.utils.getNewsTimeStr

/**
 * kim.bifrost.github.view.adapter.CommitAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 20:06
 */
class CommitPagingAdapter(private val onClickAvatar: View.(Commit) -> Unit) : BasePagingAdapter<ItemCommitBinding, Commit>(
    BaseItemCallback { t1, t2 -> t1 == t2 }
) {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemCommitBinding {
        return ItemCommitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val holderInit: Holder<ItemCommitBinding>.() -> Unit
        get() = {
            binding.ivAvatar.setOnClickListener {
                val data = getItem(bindingAdapterPosition)!!
                it.onClickAvatar(data)
            }
        }

    override fun onBindViewHolder(holder: Holder<ItemCommitBinding>, position: Int) {
        val commit = getItem(position)!!
        holder.binding.apply {
            tvAuthor.text = commit.author.login
            tvDesc.text = commit.commit.message
            tvId.text = commit.sha
            tvTime.text = getNewsTimeStr(commit.commit.author.date)
            Glide.with(ivAvatar)
                .load(commit.author.avatarUrl)
                .into(ivAvatar)
        }
    }
}