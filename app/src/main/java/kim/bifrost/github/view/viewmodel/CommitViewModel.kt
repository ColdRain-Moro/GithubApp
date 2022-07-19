package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kim.bifrost.github.repository.network.model.Commit
import kim.bifrost.github.repository.network.pagingsource.CommitPagingSource
import kotlinx.coroutines.flow.Flow

/**
 * kim.bifrost.github.view.viewmodel.CommitViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 19:04
 */
class CommitViewModel : ViewModel() {
   fun getCommitsData(owner: String, repo: String, branch: String? = null): Flow<PagingData<Commit>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                CommitPagingSource(owner, repo, branch)
            }
        ).flow.cachedIn(viewModelScope)
    }
}