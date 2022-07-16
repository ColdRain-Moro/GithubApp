package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.FragmentRvBinding
import kim.bifrost.github.view.adapter.RepositoriesPagingAdapter
import kim.bifrost.github.view.viewmodel.RepositoriesViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.fragment.RepositoriesFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 16:44
 */
class RepositoriesFragment : BaseVmBindFragment<RepositoriesViewModel, FragmentRvBinding>() {

    private val user: String by lazy {
        requireArguments().getString("user")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.userRepoData.collectLaunch {
            val adapter = RepositoriesPagingAdapter(requireContext())
            binding.apply {
                rvEvents.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = adapter
                }
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> binding.srlEvents.isRefreshing = true
                        is LoadState.NotLoading -> binding.srlEvents.isRefreshing = false
                        is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.message}".toast()
                    }
                }
                binding.srlEvents.setOnRefreshListener {
                    adapter.refresh()
                }
                adapter.submitData(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RepositoriesViewModel(user) as T
            }
        }

    companion object {
        fun newInstance(user: String): RepositoriesFragment {
            val args = Bundle()
                .apply {
                    putString("user", user)
                }
            val fragment = RepositoriesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}