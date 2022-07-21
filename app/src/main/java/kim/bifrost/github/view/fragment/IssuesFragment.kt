package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.FragmentItemsBinding
import kim.bifrost.github.view.adapter.IssuesPagingAdapter
import kim.bifrost.github.view.viewmodel.IssuesFragViewModel
import kim.bifrost.lib_common.base.ui.AutoWired
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.fragment.ItemsFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 10:49
 */
class IssuesFragment : BaseVmBindFragment<IssuesFragViewModel, FragmentItemsBinding>() {

    @AutoWired
    private var user: String? = null
    @AutoWired
    private var repo: String? = null
    @AutoWired
    private lateinit var type: Type
    @AutoWired
    private lateinit var state: State

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = IssuesPagingAdapter(if (type == Type.MY_ISSUES) null else user + repo)
        viewModel.pagingData.collectLaunch {
            adapter.submitData(it)
        }
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        adapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading -> binding.srlItems.isRefreshing = true
                is LoadState.NotLoading -> binding.srlItems.isRefreshing = false
                is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.asString()}".toast()
            }
        }
        binding.srlItems.setOnRefreshListener {
            adapter.refresh()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return IssuesFragViewModel(type, state, user, repo) as T
            }
        }

    enum class Type {
        REPO_ISSUES, MY_ISSUES
    }

    enum class State {
        OPEN, CLOSED, ALL
    }

    companion object {
        fun newInstance(type: Type, state: State, user: String? = null, repo: String? = null): IssuesFragment{
            val args = Bundle().apply {
                putString("type", type.toString())
                putString("state", state.toString())
                putString("user", user)
                putString("repo", repo)
            }
            val fragment = IssuesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}