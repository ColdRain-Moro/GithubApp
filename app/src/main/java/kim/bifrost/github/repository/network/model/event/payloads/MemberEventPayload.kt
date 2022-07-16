package kim.bifrost.github.repository.network.model.event.payloads

import kim.bifrost.github.repository.network.model.User
import kim.bifrost.github.repository.network.model.event.IEventPayload

/**
 * kim.bifrost.github.repository.network.model.event.payloads.MemberEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 0:26
 */
interface MemberEventPayload : IEventPayload {
    val member: User
    val organization: User
    val blocked_user: User
}