package kim.bifrost.github.repository.network.model.event.payloads

import com.google.gson.annotations.SerializedName
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.github.repository.network.model.event.IEventPayload
import java.util.*

/**
 * kim.bifrost.github.repository.network.model.event.payloads.ReleaseEventPayload
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:51
 */
interface ReleaseEventPayload : IEventPayload {
    val release: Release

    data class Release(
        val id: String,
        @SerializedName("tag_name")
        val tagName: String,
        @SerializedName("target_commitish")
        val targetCommitish: String,
        val name: String,
        val body: String,
        @SerializedName("body_html")
        val bodyHtml: String,
        @SerializedName("tarball_url")
        val tarballUrl: String,
        @SerializedName("zipball_url")
        val zipballUrl: String,
        val draft: Boolean,
        @SerializedName("prerelease")
        val preRelease: Boolean,
        @SerializedName("created_at")
        val createdAt: Date,
        @SerializedName("published_at")
        val publishedAt: Date,
        val author: User,
        val assets: List<ReleaseAsset>,
    )

    data class ReleaseAsset(
        val id: String,
        val name: String,
        val uploader: User,
        val label: String,
        @SerializedName("content_type")
        val contentType: String,
        val state: String,
        val size: Long,
        val downloadCount: Int,
        @SerializedName("created_at")
        val createdAt: Date,
        @SerializedName("updated_at")
        val updatedAt: Date,
        @SerializedName("browser_download_url")
        val browserDownloadUrl: String,
    )
}