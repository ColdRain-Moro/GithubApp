package kim.bifrost.github.repository.network.pagingsource

import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.github.user.UserManager
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.network.pagingsource.NewsPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 14:46
 */
class NewsPagingSource : BasePagingSource<Event>() {
    override suspend fun getData(page: Int): List<Event> {
        val user = UserManager.userTemp?.login
        return UserService.getUserReceivedEvents(user!!, 20, page + 1)
    }
}