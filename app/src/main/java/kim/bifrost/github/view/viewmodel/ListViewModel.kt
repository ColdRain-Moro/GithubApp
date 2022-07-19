package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kim.bifrost.github.repository.network.pagingsource.*
import kim.bifrost.github.view.activity.ItemListActivity
import kim.bifrost.github.view.fragment.EventsFragment

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
                UserRepositoriesPagingSource(user)
            }
        ).flow.cachedIn(viewModelScope)
    }
}