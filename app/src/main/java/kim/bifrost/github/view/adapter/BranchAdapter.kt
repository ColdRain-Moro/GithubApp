package kim.bifrost.github.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kim.bifrost.github.databinding.ItemBranchBinding

/**
 * kim.bifrost.github.view.adapter.BranchAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 19:02
 */
class BranchAdapter(private val currentBranch: String?, private val onClick: (String) -> Unit) : ListAdapter<String, BranchAdapter.Holder>(DiffCallback()) {
    inner class Holder(val binding: ItemBranchBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val str = getItem(position)
        holder.binding.apply {
            tvBranch.text = str
            if (str == currentBranch) {
                rootLayout.setBackgroundColor(Color.parseColor("#D0D0D0"))
            } else {
                rootLayout.background = null
            }
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