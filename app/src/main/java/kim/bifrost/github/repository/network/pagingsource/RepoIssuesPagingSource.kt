package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Issue
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.RepoIssuesPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 11:18
 */
class RepoIssuesPagingSource(
    private val owner: String,
    private val repo: String,
    private val state: String,
    private val sort: String = "created",
) : BasePagingSource<Issue>() {
    override suspend fun getData(page: Int): List<Issue> {
        return RepoService.getRepositoryIssues(owner, repo, state, page + 1, 20, sort)
    }
}