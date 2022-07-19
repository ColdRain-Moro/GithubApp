package kim.bifrost.github.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ItemFileBinding
import kim.bifrost.github.repository.network.model.RepoFile
import kim.bifrost.lib_common.extensions.gone

/**
 * kim.bifrost.github.view.adapter.FilesAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 18:54
 */
class FilesAdapter(private val onClick: RepoFile.() -> Unit) : ListAdapter<RepoFile, FilesAdapter.Holder>(DiffCallback()) {
    inner class Holder(val binding: ItemFileBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val data = getItem(layoutPosition)!!
                data.onClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            tvFileName.text = item.name
            when (item.type) {
                RepoFile.Type.FILE -> {
                    Glide.with(ivFile)
                        .load(R.drawable.ic_file)
                        .into(ivFile)
                    tvFileSize.text = item.size.calculateFileSize()
                }
                RepoFile.Type.DIRECTORY -> {
                    Glide.with(ivFile)
                        .load(R.drawable.ic_folder)
                        .into(ivFile)
                    tvFileSize.gone()
                }
            }
        }
    }

    private fun Int.calculateFileSize(): String {
        val kb = this / 1024
        val mb = kb / 1024
        val gb = mb / 1024
        return when {
            gb > 0 -> "$gb GB"
            mb > 0 -> "$mb MB"
            kb > 0 -> "$kb KB"
            else -> "$this B"
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RepoFile>() {
        override fun areItemsTheSame(oldItem: RepoFile, newItem: RepoFile): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RepoFile, newItem: RepoFile): Boolean {
            return oldItem == newItem
        }

        // 这个方法可以避免 ViewHolder 的互换，减少性能消耗
        override fun getChangePayload(oldItem: RepoFile, newItem: RepoFile): Any = ""
    }
}