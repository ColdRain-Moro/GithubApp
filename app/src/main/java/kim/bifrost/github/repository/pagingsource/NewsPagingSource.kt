package kim.bifrost.github.repository.pagingsource

import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.event.Event
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.pagingsource.NewsPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 14:46
 */
class NewsPagingSource(private val user: String) : BasePagingSource<Event>() {
    override suspend fun getData(page: Int): List<Event> {
        return UserService.getUserReceivedEvents(user, 20, page + 1)
    }
}