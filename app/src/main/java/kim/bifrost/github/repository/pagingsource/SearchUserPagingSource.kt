package kim.bifrost.github.repository.pagingsource

import kim.bifrost.github.repository.network.api.SearchService
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.pagingsource.SearchUserPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/22 19:49
 */
class SearchUserPagingSource(
    private val query: () -> String?,
    private val sort: () -> String?,
    private val order: () -> String?
) : BasePagingSource<User>() {
    override suspend fun getData(page: Int): List<User> {
        val q = query() ?: return emptyList()
        return SearchService.searchUser(q, sort(),
            perPage = 20, page = page + 1, order = order()).items
    }
}