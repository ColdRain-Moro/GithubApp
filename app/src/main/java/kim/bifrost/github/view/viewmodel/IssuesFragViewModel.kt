package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kim.bifrost.github.repository.pagingsource.*
import kim.bifrost.github.view.fragment.IssuesFragment

/**
 * kim.bifrost.github.view.viewmodel.ItemsFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 10:50
 */
class IssuesFragViewModel(
    val type: IssuesFragment.Type,
    val state: IssuesFragment.State,
    val user: String? = null,
    val repo: String? = null,
    private val sort: String = "created",
) : ViewModel() {
    val pagingData by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    IssuesFragment.Type.REPO_ISSUES -> RepoIssuesPagingSource(user!!, repo!!, state.toString().lowercase(), sort)
                    IssuesFragment.Type.MY_ISSUES -> UserIssuesPagingSource(state = state.toString().lowercase(), sort = sort)
                }
            }
        ).flow.cachedIn(viewModelScope)
    }
}