package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.FragmentCommitBinding
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.view.adapter.CommitPagingAdapter
import kim.bifrost.github.view.viewmodel.CommitViewModel
import kim.bifrost.lib_common.base.ui.AutoWired
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toJson
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.fragment.CommitFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 19:02
 */
class CommitFragment : BaseVmBindFragment<CommitViewModel, FragmentCommitBinding>() {

    @AutoWired
    private lateinit var repo: Repository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CommitPagingAdapter(requireContext())
        binding.rvCommit.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        viewModel.getCommitsData(repo.owner.login, repo.name)
            .collectLaunch {
                adapter.submitData(it)
            }
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> binding.srlCommit.isRefreshing = true
                is LoadState.NotLoading -> binding.srlCommit.isRefreshing = false
                is LoadState.Error -> "数据加载错误: ${(it.refresh as LoadState.Error).error.asString()}".toast()
            }
        }
        binding.srlCommit.setOnRefreshListener {
            adapter.refresh()
        }
    }

    companion object {
        fun newInstance(repo: Repository): CommitFragment {
            return CommitFragment().apply {
                arguments = Bundle().apply {
                    putString("repo", repo.toJson())
                }
            }
        }
    }
}