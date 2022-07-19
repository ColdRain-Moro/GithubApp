package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Commit
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.CommitPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 19:43
 */
class CommitPagingSource(
    private val owner: String,
    private val repo: String,
    private val branch: String? = null,
) : BasePagingSource<Commit>() {
    override suspend fun getData(page: Int): List<Commit> {
        return RepoService.getRepoCommits(owner, repo, branch, page + 1, 20)
    }
}