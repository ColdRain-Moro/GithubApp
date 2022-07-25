package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.databinding.FragmentSearchResultBinding
import kim.bifrost.github.view.activity.ProfileActivity
import kim.bifrost.github.view.activity.RepositoryActivity
import kim.bifrost.github.view.adapter.RepositoriesPagingAdapter
import kim.bifrost.github.view.adapter.UserListPagingAdapter
import kim.bifrost.github.view.viewmodel.SearchViewModel
import kim.bifrost.lib_common.base.ui.BaseBindFragment
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.fragment.SearchResultFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/23 14:19
 */
class SearchResultFragment : BaseBindFragment<FragmentSearchResultBinding>() {

    @AutoWired
    lateinit var type: Type

    private val viewModel by activityViewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inject()
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        when (type) {
            Type.USER -> {
                val adapter = UserListPagingAdapter {
                    ProfileActivity.startWithAnimation(requireActivity(), it.login, ivAvatar, it.avatarUrl)
                }
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> binding.srl.isRefreshing = true
                        is LoadState.NotLoading -> binding.srl.isRefreshing = false
                        is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.apply { printStackTrace() }.asString()}".toast()
                    }
                }
                binding.srl.setOnRefreshListener {
                    adapter.refresh()
                }
                viewModel.userSort.collectLaunch {
                    adapter.refresh()
                }
                viewModel.currentQuery.observe(viewLifecycleOwner) {
                    adapter.refresh()
                }
                viewModel.queryUserFlow.collectLaunch {
                    adapter.submitData(it)
                }
                binding.rv.adapter = adapter
            }
            Type.REPO -> {
                val adapter = RepositoriesPagingAdapter(requireActivity()) {
                    RepositoryActivity.startWithAnimation(requireActivity(), it)
                }
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> binding.srl.isRefreshing = true
                        is LoadState.NotLoading -> binding.srl.isRefreshing = false
                        is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.apply { printStackTrace() }.asString()}".toast()
                    }
                }
                binding.srl.setOnRefreshListener {
                    adapter.refresh()
                }
                viewModel.queryRepoFlow.collectLaunch {
                    adapter.submitData(it)
                }
                viewModel.currentQuery.observe(viewLifecycleOwner) {
                    adapter.refresh()
                }
                viewModel.repoSort.collectLaunch {
                    adapter.refresh()
                }
                binding.rv.adapter = adapter
            }
        }
    }

    enum class Type {
        USER,
        REPO
    }

    companion object {
        fun newInstance(type: Type): SearchResultFragment {
            val args = Bundle()
                .apply {
                    putString("type", type.toString())
                }
            val fragment = SearchResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}