package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.UserProfilePagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 16:26
 */
class UserProfilePagingSource(private val user: String) : BasePagingSource<Event>() {
    override suspend fun getData(page: Int): List<Event> {
        return UserService.getUserEvents(user, 20, page + 1)
    }
}