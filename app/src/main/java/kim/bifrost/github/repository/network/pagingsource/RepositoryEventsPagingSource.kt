package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.RepositoryEventsPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 21:19
 */
class RepositoryEventsPagingSource(private val user: String, private val repo: String) : BasePagingSource<Event>() {
    override suspend fun getData(page: Int): List<Event> {
        return RepoService.getRepositoryEvents(user, repo, page + 1, 20)
    }
}