package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.RepoWatchersPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 10:11
 */
class RepoWatchersPagingSource(
    private val user: String,
    private val repo: String
): BasePagingSource<User>() {
    override suspend fun getData(page: Int): List<User> {
        return RepoService.getRepoWatchers(user, repo, page + 1, 20)
    }
}