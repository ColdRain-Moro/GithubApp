package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.github.databinding.ActivityListBinding
import kim.bifrost.github.view.adapter.BookmarksPagingAdapter
import kim.bifrost.github.view.adapter.RepositoriesPagingAdapter
import kim.bifrost.github.view.adapter.TracePagingAdapter
import kim.bifrost.github.view.adapter.UserListPagingAdapter
import kim.bifrost.github.view.viewmodel.ListViewModel
import kim.bifrost.lib_common.base.ui.AutoWired
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.argument
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.activity.PeopleListActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 13:30
 */
class ItemListActivity : BaseVmBindActivity<ListViewModel, ActivityListBinding>(isCancelStatusBar = false) {

    @AutoWired
    private lateinit var type: Type

    @AutoWired
    private lateinit var user: String

    @AutoWired
    private var repo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                it.title = when (type) {
                    Type.USER_REPOSITORIES -> "Repositories"
                    Type.USER_FOLLOWING -> "Following"
                    Type.USER_FOLLOWERS -> "Followers"
                    Type.REPO_STARGAZERS -> "Stargazers"
                    Type.REPO_WATCHERS -> "Watchers"
                    Type.REPO_FORKS -> "Forks"
                    Type.BOOKMARKS -> "Bookmarks"
                    Type.TRACE -> "TRACE"
                }
                it.subtitle = if (type.toString().startsWith("REPO_")) "$user/$repo" else user
                it.setDisplayHomeAsUpEnabled(true)
            }
            rv.layoutManager = LinearLayoutManager(this@ItemListActivity)
            fun initAdapter() {
                val adapter = rv.adapter as PagingDataAdapter<*, *>
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> srl.isRefreshing = true
                        is LoadState.NotLoading -> srl.isRefreshing = false
                        is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.message}".toast()
                    }
                }
                srl.setOnRefreshListener {
                    adapter.refresh()
                }
            }
            when (type) {
                Type.USER_FOLLOWING, Type.USER_FOLLOWERS, Type.REPO_STARGAZERS, Type.REPO_WATCHERS -> {
                    val adapter = UserListPagingAdapter(this@ItemListActivity) { user ->
                        ProfileActivity.start(this@ItemListActivity, user.login)
                    }
                    rv.adapter = adapter
                    initAdapter()
                    viewModel.userPagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
                Type.USER_REPOSITORIES, Type.REPO_FORKS -> {
                    val adapter = RepositoriesPagingAdapter()
                    rv.adapter = adapter
                    initAdapter()
                    viewModel.repoPagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
                Type.BOOKMARKS -> {
                    val adapter = BookmarksPagingAdapter { e ->
                        if (e.entity.type == "repo") {
                            viewModel.getRepoFlow(e.repo!!.owner, e.repo.name)
                                .collectLaunch { repo ->
                                    RepositoryActivity.start(this@ItemListActivity, repo)
                                }
                        } else {
                            ProfileActivity.start(this@ItemListActivity, e.user!!.name)
                        }
                    }
                    rv.adapter = adapter
                    initAdapter()
                    viewModel.bookmarksPagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
                Type.TRACE -> {
                    val adapter = TracePagingAdapter(
                        onRepoClick = {
                            viewModel.getRepoFlow(it.owner, it.name)
                                .collectLaunch { repo ->
                                    RepositoryActivity.start(this@ItemListActivity, repo)
                                }
                        },
                        onUserClick = {
                            ProfileActivity.start(this@ItemListActivity, it.name)
                        }
                    )
                    rv.adapter = adapter
                    initAdapter()
                    viewModel.tracePagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override val viewModelFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListViewModel(type, user, repo) as T
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    enum class Type {
        USER_FOLLOWERS,
        USER_FOLLOWING,
        USER_REPOSITORIES,
        REPO_STARGAZERS,
        REPO_WATCHERS,
        REPO_FORKS,
        BOOKMARKS,
        TRACE
    }

    companion object {
        fun start(context: Context, type: Type, user: String, repo: String? = null) {
            val starter = Intent(context, ItemListActivity::class.java)
                .argument("type", type)
                .argument("user", user)
                .argument("repo", repo)
            context.startActivity(starter)
        }
    }
}