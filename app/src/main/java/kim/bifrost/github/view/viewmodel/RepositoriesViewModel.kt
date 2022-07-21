package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kim.bifrost.github.repository.pagingsource.UserRepositoriesPagingSource
import kim.bifrost.github.repository.pagingsource.UserStarRepoPagingSource
import kim.bifrost.github.view.fragment.RepositoriesFragment

/**
 * kim.bifrost.github.view.viewmodel.RepositoriesViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 16:45
 */
class RepositoriesViewModel(private val type: RepositoriesFragment.Type, private val user: String) : ViewModel() {
    val userRepoData by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    RepositoriesFragment.Type.USER -> UserRepositoriesPagingSource(user)
                    RepositoriesFragment.Type.USER_STARRED -> UserStarRepoPagingSource(user)
                }
            }
        ).flow.cachedIn(viewModelScope)
    }
}