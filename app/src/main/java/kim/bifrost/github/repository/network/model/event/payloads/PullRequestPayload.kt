package kim.bifrost.github.repository.network.model.event.payloads

import kim.bifrost.github.repository.network.model.event.IEventPayload

/**
 * kim.bifrost.github.repository.network.model.event.payloads.PullRequestEvent
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:44
 */
interface PullRequestPayload : IEventPayload {
    val action: String
    val number: Int
}