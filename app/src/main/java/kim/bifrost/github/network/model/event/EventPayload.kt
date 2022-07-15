package kim.bifrost.github.network.model.event

import kim.bifrost.github.network.model.User
import kim.bifrost.github.network.model.event.payloads.*

/**
 * kim.bifrost.github.network.model.event.EventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:05
 */
data class EventPayload(
    override val ref: String,
    override val ref_type: String,
    override val master_branch: String,
    override val description: String,
    override val issue: Issue,
    override val comment: IssueEvent,
    override val member: User,
    override val organization: User,
    override val blocked_user: User,
    override val action: PayloadAction,
    override val number: Int,
    override val size: Int,
    override val distinct_size: Int,
    override val head: String,
    override val before: String,
    override val commits: List<PushEventPayload.Commit>,
    override val release: ReleaseEventPayload.Release
) : CreateEventPayload,
    IssueCommentEventPayload,
    MemberEventPayload,
    PullRequestPayload,
    PushEventPayload,
    ReleaseEventPayload,
    WatchEventPayload