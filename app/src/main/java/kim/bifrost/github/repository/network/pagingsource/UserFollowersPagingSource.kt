package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.UserFollowersPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 14:48
 */
class UserFollowersPagingSource(private val user: String) : BasePagingSource<User>() {
    override suspend fun getData(page: Int): List<User> {
        return UserService.getFollowers(user, 20, page + 1)
    }
}