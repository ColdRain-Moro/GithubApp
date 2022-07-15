package kim.bifrost.github.network.model.event.payloads

import kim.bifrost.github.network.model.User
import kim.bifrost.github.network.model.event.IEventPayload

/**
 * kim.bifrost.github.network.model.event.payloads.PushEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:07
 */
interface PushEventPayload : IEventPayload {
    val ref: String
    val size: Int
    val distinct_size: Int
    val head: String
    val before: String
    val commits: List<Commit>

    data class Commit(
        val sha: String,
        val message: String,
        val author: Author,
        val url: String,
        val distinct: Boolean
    )

    data class Author(
        val name: String,
        val email: String,
    )
}