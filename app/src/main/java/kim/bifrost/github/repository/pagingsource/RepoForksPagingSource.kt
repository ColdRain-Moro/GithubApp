package kim.bifrost.github.repository.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.pagingsource.RepoForksPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 13:30
 */
class RepoForksPagingSource(
    private val owner: String,
    private val repo: String,
    private val sort: String = "newest",
) : BasePagingSource<Repository>() {
    override suspend fun getData(page: Int): List<Repository> {
        return RepoService.getRepositoryForks(owner, repo, page + 1, 20, sort)
    }
}