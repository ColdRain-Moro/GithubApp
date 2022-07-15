package kim.bifrost.github.network.model.event.payloads

import kim.bifrost.github.network.model.event.IEventPayload

/**
 * kim.bifrost.github.network.model.event.payloads.PullRequestEvent
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:44
 */
interface PullRequestPayload : IEventPayload {
    val action: PayloadAction
    val number: Int
}