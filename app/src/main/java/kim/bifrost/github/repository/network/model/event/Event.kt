@file:Suppress("ALL")

package kim.bifrost.github.repository.network.model.event


import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import com.google.gson.annotations.SerializedName
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ItemEventBinding
import kim.bifrost.github.repository.network.model.event.payloads.PayloadAction
import kim.bifrost.github.repository.network.model.event.payloads.PushEventPayload
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.string
import kim.bifrost.lib_common.extensions.visible
import java.util.*

data class Event(
    @SerializedName("actor")
    val actor: Actor,
    @SerializedName("created_at")
    val createdAt: Date,
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

enum class PullRequestReviewCommentEventActionType {
    created, edited, deleted
}

enum class PullRequestReviewEventActionType {
    submitted, edited, dismissed, created
}

enum class RefType {
    repository, branch, tag
}

enum class OrgBlockEventActionType {
    blocked, unblocked
}

enum class MemberEventActionType {
    added, deleted, edited
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


fun ItemEventBinding.handleEvent(event: Event, context: Context) {
    var actionStr: String? = null
    var descSpan: SpannableStringBuilder? = null
    when (event.type) {
        EventType.CommitCommentEvent -> {
            actionStr = String.format(R.string.created_comment_on_commit.string, event.repo.name)
            descSpan = SpannableStringBuilder(event.payload.comment.body)
        }
        EventType.CreateEvent -> actionStr = when (event.payload.ref_type) {
            RefType.repository.toString() -> String.format(R.string.created_repo.string, event.repo.name)
            RefType.branch.toString() -> String.format(R.string.created_branch_at.string, event.payload.ref, event.repo.name)
            RefType.tag.toString() -> String.format(R.string.created_tag_at.string, event.payload.ref, event.repo.name)
            else -> ""
        }
        EventType.DeleteEvent -> actionStr = when (event.payload.ref_type) {
            RefType.branch.toString() -> String.format(R.string.delete_branch_at.string, event.payload.ref, event.repo.name)
            RefType.tag.toString() -> String.format(R.string.delete_tag_at.string, event.payload.ref, event.repo.name)
            else -> ""
        }
        EventType.ForkEvent -> {
            val oriRepo: String = event.repo.name
            val newRepo: String =
                event.actor.login + "/" + event.repo.name.split("/")[1]
            actionStr = String.format(R.string.forked_to.string, oriRepo, newRepo)
        }
        EventType.GollumEvent -> actionStr = event.payload.action.lowercase() + " a wiki page "
        EventType.InstallationEvent -> actionStr = event.payload.action.lowercase() + " an GitHub App "
        EventType.InstallationRepositoriesEvent -> actionStr = event.payload.action.lowercase() + " repository from an installation "
        EventType.IssueCommentEvent -> {
            actionStr = String.format(
                R.string.created_comment_on_issue.string,
                event.payload.issue.number, event.repo.name
            )
            descSpan = SpannableStringBuilder(event.payload.comment.body)
        }
        EventType.IssuesEvent -> {
            val issueEventStr: String = getIssueEventStr(event.payload.action)
            actionStr = java.lang.String.format(
                issueEventStr,
                event.payload.issue.number, event.repo.name
            )
            descSpan = SpannableStringBuilder(event.payload.issue.title)
        }
        EventType.MarketplacePurchaseEvent -> actionStr = event.payload.action + " marketplace plan "
        EventType.MemberEvent -> {
            val memberEventStr: String = getMemberEventStr(event.payload.action)
            actionStr = java.lang.String.format(
                memberEventStr,
                event.payload.member.login, event.repo.name
            )
        }
        EventType.OrgBlockEvent -> {
            val orgBlockEventStr: String = if (OrgBlockEventActionType.blocked.toString() == event.payload.action) {
                R.string.org_blocked_user.string
            } else {
                R.string.org_unblocked_user.string
            }
            actionStr = String.format(
                orgBlockEventStr,
                event.payload.organization.login,
                event.payload.organization.login
            )
        }
        EventType.ProjectCardEvent -> actionStr = event.payload.action + " a project "
        EventType.ProjectColumnEvent -> actionStr = event.payload.action + " a project "
        EventType.ProjectEvent -> actionStr = event.payload.action + " a project "
        EventType.PublicEvent -> actionStr = String.format(R.string.made_repo_public.string, event.repo.name)
        EventType.PullRequestEvent -> actionStr = event.payload.action + " pull request " + event.repo.name
        EventType.PullRequestReviewEvent -> {
            val pullRequestReviewStr: String = getPullRequestReviewEventStr(event.payload.action)
            actionStr = String.format(pullRequestReviewStr, event.repo.name)
        }
        EventType.PullRequestReviewCommentEvent -> {
            val pullRequestCommentStr: String = getPullRequestReviewCommentEventStr(event.payload.action)
            actionStr = String.format(pullRequestCommentStr, event.repo.name)
            descSpan = (event.payload.comment.bodyHtml ?: "").let { SpannableStringBuilder(it) }
        }
        EventType.PushEvent -> {
            val branch: String = event.payload.getBranch()
            actionStr = String.format(R.string.push_to.string, branch, event.repo.name)
            descSpan = SpannableStringBuilder("")
            val count: Int = event.payload.commits.size
            val maxLines = 4
            val max = if (count > maxLines) maxLines - 1 else count
            var i = 0
            while (i < max) {
                val commit: PushEventPayload.Commit = event.payload.commits[i]
                if (i != 0) {
                    descSpan.append("\n")
                }
                val lastLength: Int = descSpan.length
                val sha: String = commit.sha.substring(0, 7)
                descSpan.append(sha)
                descSpan.setSpan(
                    TextAppearanceSpan(context, R.style.text_link),
                    lastLength, lastLength + sha.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                descSpan.append(" ")
                descSpan.append(getFirstLine(commit.message))
//                descSpan.setSpan(
//                    EllipsizeLineSpan(if (i == count - 1) 0 else 0),
//                    lastLength, descSpan.length, 0
//                )
                i++
            }
            if (count > maxLines) {
                descSpan.append("\n").append("...")
            }
        }
        EventType.ReleaseEvent -> actionStr = java.lang.String.format(
            R.string.published_release_at.string,
            event.payload.release.tagName, event.repo.name
        )
        EventType.WatchEvent -> actionStr =
            String.format(R.string.starred_repo.string, event.repo.name)
    }
    tvAction.text = actionStr
    if (descSpan != null) {
        tvDesc.visible()
        tvDesc.text = descSpan
    } else {
        tvDesc.gone()
    }
}

private fun getFirstLine(str: String): String {
    return if (!str.contains("\n")) str else str.substring(0, str.indexOf("\n"))
}

private fun getPullRequestReviewEventStr(action: String): String {
    return when (PullRequestReviewEventActionType.valueOf(action)) {
        PullRequestReviewEventActionType.submitted -> R.string.submitted_pull_request_review_at.string
        PullRequestReviewEventActionType.edited -> R.string.edited_pull_request_review_at.string
        PullRequestReviewEventActionType.dismissed -> R.string.dismissed_pull_request_review_at.string
        else -> R.string.submitted_pull_request_review_at.string
    }
}

private fun getPullRequestReviewCommentEventStr(action: String): String {
    return when (PullRequestReviewCommentEventActionType.valueOf(action)) {
        PullRequestReviewCommentEventActionType.created -> R.string.created_pull_request_comment_at.string
        PullRequestReviewCommentEventActionType.edited -> R.string.edited_pull_request_comment_at.string
        PullRequestReviewCommentEventActionType.deleted -> R.string.deleted_pull_request_comment_at.string
        else -> R.string.created_pull_request_comment_at.string
    }
}

fun getIssueEventStr(action: String): String {
    return when (action) {
        PayloadAction.ASSIGNED.toString().lowercase() -> R.string.assigned_issue_at.string
        PayloadAction.UNASSIGNED.toString().lowercase() -> R.string.unassigned_issue_at.string
        PayloadAction.LABELED.toString().lowercase() -> R.string.labeled_issue_at.string
        PayloadAction.UNLABELED.toString().lowercase() -> R.string.unlabeled_issue_at.string
        PayloadAction.OPENED.toString().lowercase() -> R.string.opened_issue_at.string
        PayloadAction.EDITED.toString().lowercase() -> R.string.edited_issue_at.string
        PayloadAction.CLOSED.toString().lowercase() -> R.string.closed_issue_at.string
        PayloadAction.REOPENED.toString().lowercase() -> R.string.reopened_issue_at.string
        else -> R.string.opened_issue_at.string
    }
}

private fun getMemberEventStr(action: String): String {
    return when (MemberEventActionType.valueOf(action)) {
        MemberEventActionType.added -> R.string.added_member_to.string
        MemberEventActionType.deleted -> R.string.deleted_member_at.string
        MemberEventActionType.edited -> R.string.edited_member_at.string
        else -> R.string.added_member_to.string
    }
}