package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.pagingsource.NewsPagingSource
import kim.bifrost.github.repository.pagingsource.RepositoryEventsPagingSource
import kim.bifrost.github.repository.pagingsource.UserProfilePagingSource
import kim.bifrost.github.view.fragment.EventsFragment
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * kim.bifrost.github.view.viewmodel.NewsViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 22:34
 */
class EventsViewModel(
    private val type: EventsFragment.SourceType,
    private val user: String?,
    private val repo: String?
) : ViewModel() {
    val eventsData by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                when (type) {
                    EventsFragment.SourceType.NEWS -> NewsPagingSource(user!!)
                    EventsFragment.SourceType.USER -> UserProfilePagingSource(user!!)
                    EventsFragment.SourceType.REPO -> RepositoryEventsPagingSource(user!!, repo!!)
                }
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getRepoFlow(fullName: String) = flow {
        val (name, repo) = fullName.split("/")
        emit(RepoService.getRepo(name, repo))
    }.map { repo ->
        repo.language?.let {
            repo.languageColor = RepoService.getLanguageColor(repo.language).data?.hex
        }
        return@map repo
    }
}