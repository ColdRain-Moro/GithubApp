package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.transition.Visibility
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.material.transition.platform.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityListBinding
import kim.bifrost.github.databinding.ItemUserBinding
import kim.bifrost.github.view.adapter.*
import kim.bifrost.github.view.viewmodel.ListViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.argument
import kim.bifrost.lib_common.extensions.asString
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
    lateinit var type: Type

    @AutoWired
    lateinit var user: String

    @AutoWired
    var repo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        window.exitTransition = buildExitTransition()
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
                    Type.TRACE -> "Trace"
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
                        is LoadState.Error -> "数据加载错误: ${(state.refresh as LoadState.Error).error.also { it.printStackTrace() }.asString()}".toast()
                    }
                }
                srl.setOnRefreshListener {
                    adapter.refresh()
                }
            }
            when (type) {
                Type.USER_FOLLOWING, Type.USER_FOLLOWERS, Type.REPO_STARGAZERS, Type.REPO_WATCHERS -> {
                    val adapter = UserListPagingAdapter { user ->
                        ProfileActivity.startWithAnimation(this@ItemListActivity, user.login, ivAvatar, user.avatarUrl)
                    }
                    rv.adapter = adapter
                    initAdapter()
                    viewModel.userPagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
                Type.USER_REPOSITORIES, Type.REPO_FORKS -> {
                    val adapter = RepositoriesPagingAdapter(this@ItemListActivity) {
                        RepositoryActivity.startWithAnimation(this@ItemListActivity, it)
                    }
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
                                    RepositoryActivity.startWithAnimation(this@ItemListActivity, repo)
                                }
                        } else {
                            ProfileActivity.startWithAnimation(this@ItemListActivity, e.user!!.name, (this as ItemUserBinding).ivAvatar, e.user.avatarUrl)
                        }
                    }
                    rv.adapter = adapter
                    initAdapter()
                    initBookmarksTouchHelper(adapter)
                    viewModel.bookmarksPagingSource.collectLaunch {
                        adapter.submitData(it)
                    }
                }
                Type.TRACE -> {
                    val adapter = TracePagingAdapter(
                        onRepoClick = {
                            viewModel.getRepoFlow(it.owner, it.name)
                                .collectLaunch { repo ->
                                    RepositoryActivity.startWithAnimation(this@ItemListActivity, repo)
                                }
                        },
                        onUserClick = {
                            ProfileActivity.startWithAnimation(this@ItemListActivity, it.name, ivAvatar, it.avatarUrl)
                        }
                    )
                    rv.adapter = adapter
                    initAdapter()
                    initTraceTouchHelper(adapter)
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

    private fun initTraceTouchHelper(adapter: TracePagingAdapter) {
        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                if (adapter.getItemOut(viewHolder.bindingAdapterPosition) is TraceItem.Divider) {
                    return makeMovementFlags(0, 0)
                }
                // 控制快速滑动的方向（一般是左右）
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                // 不允许拖拽
                return makeMovementFlags(0, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //滑动处理
                val position = viewHolder.bindingAdapterPosition
                val data = adapter.getItemOut(position)!!
                viewModel.removeTraceItem(data).collectLaunch {
                    it.onSuccess {
                        adapter.notifyItemRemoved(position)
                        adapter.refresh()
                        Snackbar.make(binding.root, "Trace deleted", Snackbar.LENGTH_SHORT).show()
                    }.onFailure { e ->
                        e.asString().toast()
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rv)
    }

    private fun initBookmarksTouchHelper(adapter: BookmarksPagingAdapter) {
        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                // 控制快速滑动的方向（一般是左右）
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                // 不允许拖拽
                return makeMovementFlags(0, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //滑动处理
                val position = viewHolder.bindingAdapterPosition
                val data = adapter.getItemOut(position)!!
                viewModel.removeBookmarksItem(data).collectLaunch {
                    it.onSuccess {
                        adapter.notifyItemRemoved(position)
                        Snackbar.make(binding.root, "Bookmark deleted", Snackbar.LENGTH_SHORT).show()
                    }.onFailure { e ->
                        e.asString().toast()
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rv)
    }

    private fun buildExitTransition(): Visibility {
        val slide = Slide()
        slide.duration = 500
        slide.slideEdge = Gravity.START
        slide.interpolator = OvershootInterpolator(0.5f)
        return slide
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