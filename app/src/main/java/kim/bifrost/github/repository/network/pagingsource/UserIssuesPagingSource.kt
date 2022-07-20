package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.Issue
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.UserIssuesPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 11:31
 */
class UserIssuesPagingSource(
    private val state: String,
    private val sort: String = "created",
) : BasePagingSource<Issue>() {
    override suspend fun getData(page: Int): List<Issue> {
        return UserService.getUserIssues(state, page + 1, 20, sort)
    }
}