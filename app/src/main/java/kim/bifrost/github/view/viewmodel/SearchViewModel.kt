package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kim.bifrost.github.repository.pagingsource.SearchRepoPagingSource
import kim.bifrost.github.repository.pagingsource.SearchUserPagingSource
import kotlinx.coroutines.flow.MutableStateFlow

/**
  * kim.bifrost.github.view.viewmodel.SearhViewModel
  * GitHubApp
  *
  * @author 寒雨
  * @since 2022/7/22 15:00
  */
class SearchViewModel : ViewModel() {
    private val _currentQuery = MutableLiveData<String>()
    // null -> bestMatch
    private val _userSort = MutableStateFlow<UserSort?>(null)
    private val _repoSort = MutableStateFlow<RepoSort?>(null)

    val currentQuery: LiveData<String>
        get() = _currentQuery

    fun submitQuery(q: String) {
        _currentQuery.value = q
    }

    fun changeUserSort(sort: UserSort?) {
        _userSort.value = sort
    }

    fun changeRepoSort(sort: RepoSort?) {
        _repoSort.value = sort
    }

    val queryRepoFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                SearchRepoPagingSource({ _currentQuery.value!! }, { _repoSort.value?.sort }, { _repoSort.value?.order })
            }
        ).flow.cachedIn(viewModelScope)
    }

    val queryUserFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                SearchUserPagingSource({ _currentQuery.value!! }, { _userSort.value?.sort }, { _repoSort.value?.order })
            }
        ).flow.cachedIn(viewModelScope)
    }
}

enum class UserSort(val sort: String, val order: String) {
    MOST_FOLLOWERS("followers", "desc"),
    LATEST_JOINED("joined", "desc"),
    MOST_REPOS("repositories", "desc"),
    LEAST_FOLLOWERS("followers", "asc"),
    LEAST_LATEST_JOINED("joined", "asc"),
    LEAST_REPOS("repositories", "asc"),
}

enum class RepoSort(val sort: String, val order: String) {
    MOST_STARS("stars", "desc"),
    MOST_FORKS("forks", "desc"),
    RECENTLY_UPDATED("updated", "desc"),
    LEAST_RECENTLY_UPDATED("updated", "asc"),
    LEAST_STARS("stars", "asc"),
    LEAST_FORKS("forks", "asc")
}