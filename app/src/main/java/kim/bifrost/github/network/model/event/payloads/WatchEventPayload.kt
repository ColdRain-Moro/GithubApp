package kim.bifrost.github.network.model.event.payloads

import com.google.gson.annotations.SerializedName
import kim.bifrost.github.network.model.event.IEventPayload

/**
 * kim.bifrost.github.network.model.event.payloads.WatchEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:36
 */
interface WatchEventPayload : IEventPayload {
    val action: PayloadAction
}

enum class PayloadAction {
    // PullRequestEvent
    @SerializedName("opened")
    OPENED,
    @SerializedName("edited")
    EDITED,
    @SerializedName("closed")
    CLOSED,
    @SerializedName("reopened")
    REOPENED,
    @SerializedName("assigned")
    ASSIGNED,
    @SerializedName("unassigned")
    UNASSIGNED,
    @SerializedName("review_requested")
    REVIEW_REQUESTED,
    @SerializedName("review_request_removed")
    REVIEW_REQUEST_REMOVED,
    @SerializedName("labeled")
    LABELED,
    @SerializedName("unlabeled")
    UNLABELED,
    @SerializedName("synchronize")
    SYNCHRONIZED,
    // WatchEvent
    @SerializedName("started")
    STARTED
}