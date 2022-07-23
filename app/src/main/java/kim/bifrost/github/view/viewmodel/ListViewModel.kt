package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kim.bifrost.github.repository.database.AppDatabase
import kim.bifrost.github.repository.database.entity.BookmarksQueryResult
import kim.bifrost.github.repository.database.entity.TraceQueryResult
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.pagingsource.*
import kim.bifrost.github.view.activity.ItemListActivity
import kim.bifrost.github.view.adapter.TraceItem
import kim.bifrost.lib_common.extensions.catchAll
import kim.bifrost.lib_common.extensions.tryRun
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * kim.bifrost.github.view.viewmodel.PeopleListViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 13:32
 */
class ListViewModel(
    private val type: ItemListActivity.Type,
    private val user: String,
    private val repo: String?
) : ViewModel() {
    val userPagingSource by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    ItemListActivity.Type.USER_FOLLOWERS -> UserFollowersPagingSource(user)
                    ItemListActivity.Type.USER_FOLLOWING -> UserFollowingPagingSource(user)
                    ItemListActivity.Type.REPO_STARGAZERS -> RepoStargazersPagingSource(user, repo!!)
                    ItemListActivity.Type.REPO_WATCHERS -> RepoWatchersPagingSource(user, repo!!)
                    else -> throw IllegalArgumentException("type is not supported")
                }
            }
        ).flow.cachedIn(viewModelScope)
    }

    val repoPagingSource by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    ItemListActivity.Type.REPO_FORKS -> RepoForksPagingSource(user, repo!!)
                    ItemListActivity.Type.USER_REPOSITORIES -> UserRepositoriesPagingSource(user)
                    else -> throw IllegalArgumentException("type is not supported")
                }
            }
        ).flow.cachedIn(viewModelScope)
    }

    val bookmarksPagingSource by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    ItemListActivity.Type.BOOKMARKS -> AppDatabase.INSTANCE.bookmarksDao().pagingSource()
                    else -> throw IllegalArgumentException("type is not supported")
                }
            }
        ).flow.cachedIn(viewModelScope)
    }

    val tracePagingSource by lazy {
        var date: Int
        var month: Int
        var year: Int
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    ItemListActivity.Type.TRACE -> AppDatabase.INSTANCE.traceDao().pagingSource()
                    else -> throw IllegalArgumentException("type is not supported")
                }
            }
        ).flow.map {
            // 每次刷新初始化一次值
            date = 0
            month = 0
            year = 0
            it.insertSeparators { _, after ->
                if (after != null
                    && (date != after.entity.time.date
                        || month != after.entity.time.month
                        || year != after.entity.time.year)
                ) {
                    date = after.entity.time.date
                    month = after.entity.time.month
                    year = after.entity.time.year
                    return@insertSeparators TraceItem.Divider(after.entity.time)
                }
                return@insertSeparators null
            }.map { res ->
                 if (res is TraceItem.Divider) {
                     res
                 } else {
                    res as TraceQueryResult
                    when (res.entity.type) {
                        "repo" -> {
                            TraceItem.Repo(res.repo!!)
                        }
                        "user" -> {
                            TraceItem.User(res.user!!)
                        }
                        else -> error("type is not supported")
                    }
                }
            }
        }.cachedIn(viewModelScope)
    }

    fun removeTraceItem(item: TraceItem): Flow<Result<Unit>> {
        return flow<Result<Unit>> {
            tryRun {
                when (item) {
                    is TraceItem.User -> {
                        AppDatabase.INSTANCE.traceDao().deleteByUserId(item.user.id)
                        AppDatabase.INSTANCE.localUserDao().delete(item.user)
                        emit(Result.success(Unit))
                    }
                    is TraceItem.Repo -> {
                        AppDatabase.INSTANCE.traceDao().deleteByRepoId(item.repo.id)
                        AppDatabase.INSTANCE.localRepoDao().delete(item.repo)
                        emit(Result.success(Unit))
                    }
                    else -> {}
                }
            } catchAll {
                emit(Result.failure(it))
            }
        }.shareIn(viewModelScope, started = SharingStarted.Lazily, 1)
    }

    fun removeBookmarksItem(item: BookmarksQueryResult): Flow<Result<Unit>> {
        return flow<Result<Unit>> {
            tryRun {
                when (item.entity.type) {
                    "user" -> {
                        AppDatabase.INSTANCE.bookmarksDao().deleteByUserId(item.user!!.id)
                        AppDatabase.INSTANCE.localUserDao().delete(item.user)
                        emit(Result.success(Unit))
                    }
                    "repo" -> {
                        AppDatabase.INSTANCE.bookmarksDao().deleteByRepoId(item.repo!!.id)
                        AppDatabase.INSTANCE.localRepoDao().delete(item.repo)
                        emit(Result.success(Unit))
                    }
                    else -> {}
                }
            } catchAll {
                emit(Result.failure(it))
            }
        }.shareIn(viewModelScope, started = SharingStarted.Lazily, 1)
    }

    fun getRepoFlow(owner: String, repo: String) = flow {
        emit(RepoService.getRepo(owner, repo))
    }
}