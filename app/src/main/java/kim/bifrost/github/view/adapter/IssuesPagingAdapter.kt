package kim.bifrost.github.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kim.bifrost.github.databinding.ItemIssueBinding
import kim.bifrost.github.repository.network.model.Issue
import kim.bifrost.lib_common.base.adapter.BasePagingAdapter
import kim.bifrost.lib_common.utils.asString

/**
 * kim.bifrost.github.view.adapter.IssuesPagingAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 11:36
 */
class IssuesPagingAdapter(private val repoFullName: String? = null) : BasePagingAdapter<ItemIssueBinding, Issue>() {
    override fun getDataBinding(parent: ViewGroup, viewType: Int): ItemIssueBinding {
        return ItemIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: Holder<ItemIssueBinding>, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            Glide.with(ivAvatar)
                .load(data.user.avatarUrl)
                .into(ivAvatar)
            tvAuthor.text = data.user.login
            tvTitle.text = data.title
            tvRepoAndId.text = repoFullName ?: data.repository.fullName
            tvTime.text = data.createdAt.asString()
        }
    }
}