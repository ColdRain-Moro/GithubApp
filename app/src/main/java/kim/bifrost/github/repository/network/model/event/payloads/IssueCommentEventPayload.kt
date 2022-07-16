package kim.bifrost.github.repository.network.model.event.payloads

import com.google.gson.annotations.SerializedName
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.github.repository.network.model.event.IEventPayload
import java.util.*

/**
 * kim.bifrost.github.repository.network.model.event.payloads.IssueCommentEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 0:03
 */
interface IssueCommentEventPayload : IEventPayload {
    val issue: Issue
    val comment: IssueEvent
}

data class Issue(

    val id: String,
    val number: Int,
    val title: String,
    val state: IssueState,
    val locked: Boolean = false,

    @SerializedName("comments")
    val commentNum: Int = 0,

    @SerializedName("created_at")
    val createdAt: Date,

    @SerializedName("updated_at")
    val updatedAt: Date,

    @SerializedName("closed_at")
    val closedAt: Date,
    val body: String,

    @SerializedName("body_html")
    val bodyHtml: String,

    val user: User,

    @SerializedName("author_association")
    val authorAssociation: IssueAuthorAssociation,

    @SerializedName("repository_url")
    val repoUrl: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    val labels: ArrayList<Label>,
    val assignee: User,
    val assignees: ArrayList<User>,
//    private val milestone: Milestone,

    @SerializedName("closed_by")
    val closedBy: User,
) {
    enum class IssueState {
        @SerializedName("open")
        OPEN,

        @SerializedName("closed")
        CLOSED
    }

    enum class IssueAuthorAssociation {
        OWNER, CONTRIBUTOR, NONE
    }

    data class Label(
        val id: String,
        val name: String,
        val select: Boolean,
        val color: String,
        val isDefault: String
    )
}

data class IssueEvent(
    val id: String,
    val user: User,
    @com.google.gson.annotations.SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("updated_at")
    val updatedAt: Date,
    val body: String,
    @SerializedName("body_html")
    val bodyHtml: String,
    @SerializedName("event")
    val type: Type,
    @SerializedName("html_url")
    val htmlUrl: String,
    val assignee: User,
    val assigner: User,
    val actor: User,
    val parentIssue: Issue,
)

enum class Type {
    /**
     * The issue was closed by the actor. When the commit_id is present, it identifies the
     * commit that closed the issue using "closes / fixes #NN" syntax.
     */
    closed,

    /**
     * The issue was reopened by the actor.
     */
    reopened, commented, commentDeleted,

    /**
     * The issue title was changed.
     */
    renamed,

    /**
     * The issue was locked by the actor.
     */
    locked,

    /**
     * The issue was unlocked by the actor.
     */
    unlocked,

    /**
     * The issue was referenced from another issue. The `source` attribute contains the `id`,
     * `actor`, and `url` of the reference's source.
     */
    crossReferenced,

    /**
     * The issue was assigned to the actor.
     */
    assigned,

    /**
     * The actor was unassigned from the issue.
     */
    unassigned,

    /**
     * A label was added to the issue.
     */
    labeled,

    /**
     * A label was removed from the issue.
     */
    unlabeled,

    /**
     * The issue was added to a milestone.
     */
    milestoned,

    /**
     * The issue was removed from a milestone.
     */
    demilestoned
    /**
     * The issue was added to a project board.
     */
    //        @SerializedName("added_to_project") addedToProject,
    /**
     * The issue was moved between columns in a project board.
     */
    //        @SerializedName("moved_columns_in_project") movedColumnsInProject,
    /**
     * The issue was removed from a project board.
     */
    //        @SerializedName("removed_from_project") removedFromProject,
    /**
     * A commit was added to the pull request's `HEAD` branch. Only provided for pull requests.
     */
    //        committed,
    /**
     * The issue was merged by the actor. The `commit_id` attribute is the SHA1 of
     * the HEAD commit that was merged.
     */
    //        merged,
    /**
     * The pull request's branch was deleted.
     */
    //        @SerializedName("head_ref_deleted") headRefDeleted,
    /**
     * The pull request's branch was restored.
     */
    //        @SerializedName("head_ref_restored") headRefRestored,
    /**
     * The actor dismissed a review from the pull request.
     */
    //        @SerializedName("review_dismissed") reviewDismissed,
    /**
     * The actor requested review from the subject on this pull request.
     */
    //        @SerializedName("review_requested") reviewRequested,
    /**
     * The actor removed the review request for the subject on this pull request.
     */
    //        @SerializedName("review_request_removed") reviewRequestRemoved,
    /**
     * A user with write permissions marked an issue as a duplicate of another issue or a
     * pull request as a duplicate of another pull request.
     */
    //        @SerializedName("marked_as_duplicate") markedAsDuplicate,
    /**
     * An issue that a user had previously marked as a duplicate of another issue
     * is no longer considered a duplicate, or a pull request that a user had previously
     * marked as a duplicate of another pull request is no longer considered a duplicate.
     */
    //        @SerializedName("unmarked_as_duplicate") unmarkedAsDuplicate,
    /**
     * The issue was created by converting a note in a project board to an issue.
     */
    //        @SerializedName("converted_note_to_issue") convertedNoteToIssue,
    //unable
    /**
     * The issue was referenced from a commit message. The `commit_id` attribute is
     * the commit SHA1 of where that happened.
     */
    //        referenced,
    /**
     * The actor subscribed to receive notifications for an issue.
     */
    //        subscribed,
    /**
     * The actor unsubscribed to stop receiving notifications for an issue.
     */
    //        unsubscribed,
    /**
     * The actor was @mentioned in an issue body.
     */
    //        mentioned,
}