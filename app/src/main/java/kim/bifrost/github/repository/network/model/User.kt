package kim.bifrost.github.repository.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * kim.bifrost.github.repository.network.model.User
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 23:22
 */
data class User(
    val login: String,
    val id: String,
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val type: UserType,
    val company: String,
    val blog: String,
    val location: String,
    val email: String,
    val bio: String?,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("public_gists")
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at")
    val createdAt: Date,
    @SerializedName("updated_at")
    val updatedAt: Date
) {
    enum class UserType {
        @SerializedName("User")
        User,
        @SerializedName("Organization")
        Organization
    }
}