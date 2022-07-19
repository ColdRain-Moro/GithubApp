package kim.bifrost.github.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kim.bifrost.github.databinding.ItemPathBinding
import kim.bifrost.github.repository.network.model.RepoFile

/**
 * kim.bifrost.github.view.adapter.PathAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 12:39
 */
class PathAdapter(private val onClick: (String) -> Unit) : ListAdapter<String, PathAdapter.Holder>(DiffCallback()) {
    inner class Holder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(layoutPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemPathBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.apply {
            tvPath.text = getItem(position).split("/").last().ifEmpty { "." } + " >"
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {

        // 这个方法可以避免 ViewHolder 的互换，减少性能消耗
        override fun getChangePayload(oldItem: String, newItem: String): Any = ""

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}