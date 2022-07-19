package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.UserFollowingPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 14:50
 */
class UserFollowingPagingSource(private val user: String) : BasePagingSource<User>() {
    override suspend fun getData(page: Int): List<User> {
        return UserService.getFollowing(user, 20, page + 1)
    }
}