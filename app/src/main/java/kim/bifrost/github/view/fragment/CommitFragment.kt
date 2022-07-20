package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.FragmentCommitBinding
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.view.adapter.CommitPagingAdapter
import kim.bifrost.github.view.viewmodel.CommitViewModel
import kim.bifrost.github.view.viewmodel.RepoViewModel
import kim.bifrost.lib_common.base.ui.AutoWired
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toJson
import kim.bifrost.lib_common.extensions.toast
import kotlinx.coroutines.Job

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

    private val activityViewModel by activityViewModels<RepoViewModel>()

    private lateinit var currentPagingJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CommitPagingAdapter(requireContext())
        binding.rvCommit.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        currentPagingJob = viewModel.getCommitsData(repo.owner.login, repo.name)
            .collectLaunch { data ->
                adapter.submitData(data)
            }
        activityViewModel.currentBranch.observe(viewLifecycleOwner) {
            // 关流
            currentPagingJob.cancel()
            // 开新流
            currentPagingJob = viewModel.getCommitsData(repo.owner.login, repo.name, it)
                .collectLaunch { data ->
                    adapter.submitData(data)
                }
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