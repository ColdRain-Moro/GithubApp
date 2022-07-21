package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.FragmentRvBinding
import kim.bifrost.github.view.activity.RepositoryActivity
import kim.bifrost.github.view.adapter.EventsPagingAdapter
import kim.bifrost.github.view.viewmodel.EventsViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.asString
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.fragment.NewsFragment
 * GitHubApp
 * for event api，可复用
 *
 * @author 寒雨
 * @since 2022/7/14 22:34
 */
class EventsFragment : BaseVmBindFragment<EventsViewModel, FragmentRvBinding>() {

    private val type: SourceType by lazy {
        SourceType.valueOf(requireArguments().getString("type") ?: error("type is null"))
    }

    private val user by lazy {
        requireArguments().getString("user")
    }

    private val repo by lazy {
        requireArguments().getString("repo")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val eventsAdapter = EventsPagingAdapter { event ->
            viewModel.getRepoFlow(event.repo.name).collectLaunch {
                RepositoryActivity.start(requireContext(), it)
            }
        }
        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventsAdapter
        }
        eventsAdapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading -> binding.srlEvents.isRefreshing = true
                is LoadState.NotLoading -> binding.srlEvents.isRefreshing = false
                is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.asString()}".toast()
            }
        }
        binding.srlEvents.setOnRefreshListener {
            eventsAdapter.refresh()
        }
        viewModel.eventsData.collectLaunch {
            eventsAdapter.submitData(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventsViewModel(type, user, repo) as T
            }
        }

    enum class SourceType {
        NEWS, USER, REPO
    }

    companion object {
        fun newInstance(type: SourceType, user: String? = null, repo: String? = null): EventsFragment {
            val args = Bundle()
                .apply {
                    putString("type", type.toString())
                    putString("user", user)
                    putString("repo", repo)
                }
            val fragment = EventsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}