package kim.bifrost.github.repository.network.model.event.payloads

import kim.bifrost.github.repository.network.model.event.IEventPayload

/**
 * kim.bifrost.github.repository.network.model.event.payloads.CreateEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:47
 */
interface CreateEventPayload : IEventPayload {
    val ref: String
    val ref_type: String
    val master_branch: String
    val description: String
}