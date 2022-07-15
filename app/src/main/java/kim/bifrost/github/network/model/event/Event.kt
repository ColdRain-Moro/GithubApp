package kim.bifrost.github.network.model.event


import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("actor")
    val actor: Actor,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("payload")
    val payload: EventPayload,
    @SerializedName("public")
    val `public`: Boolean,
    @SerializedName("repo")
    val repo: Repo,
    @SerializedName("type")
    val type: EventType
) {
    data class Actor(
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("display_login")
        val displayLogin: String,
        @SerializedName("gravatar_id")
        val gravatarId: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("login")
        val login: String,
        @SerializedName("url")
        val url: String
    )

    data class Repo(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )
}

enum class EventType {

    CommitCommentEvent, CreateEvent,

    /**
     * Represents a deleted branch or tag.
     */
    DeleteEvent, ForkEvent,

    /**
     * Triggered when a Wiki page is created or updated.
     */
    GollumEvent,

    /**
     * Triggered when a GitHub App has been installed or uninstalled.
     */
    InstallationEvent,

    /**
     * Triggered when a repository is added or removed from an installation.
     */
    InstallationRepositoriesEvent, IssueCommentEvent, IssuesEvent,

    /**
     * Triggered when a user purchases, cancels, or changes their GitHub Marketplace plan.
     */
    MarketplacePurchaseEvent,

    /**
     * Triggered when a user is added or removed as a collaborator to a repository, or has their permissions changed.
     */
    MemberEvent,

    /**
     * Triggered when an organization blocks or unblocks a user.
     */
    OrgBlockEvent,

    /**
     * Triggered when a project card is created, updated, moved, converted to an issue, or deleted.
     */
    ProjectCardEvent,

    /**
     * Triggered when a project column is created, updated, moved, or deleted.
     */
    ProjectColumnEvent,

    /**
     * Triggered when a project is created, updated, closed, reopened, or deleted.
     */
    ProjectEvent,

    /**
     * made repository public
     */
    PublicEvent, PullRequestEvent,

    /**
     * Triggered when a pull request review is submitted into a non-pending state, the body is edited, or the review is dismissed.
     */
    PullRequestReviewEvent, PullRequestReviewCommentEvent, PushEvent, ReleaseEvent, WatchEvent,  //Events of this type are not visible in timelines. These events are only used to trigger hooks.
    DeploymentEvent, DeploymentStatusEvent, MembershipEvent, MilestoneEvent, OrganizationEvent, PageBuildEvent, RepositoryEvent, StatusEvent, TeamEvent, TeamAddEvent, LabelEvent,  //Events of this type are no longer delivered, but it's possible that they exist in timelines

    // of some users. You cannot createForRepo webhooks that listen to these events.
    DownloadEvent, FollowEvent, ForkApplyEvent, GistEvent
}