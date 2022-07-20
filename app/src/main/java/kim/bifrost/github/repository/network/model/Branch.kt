package kim.bifrost.github.repository.network.model

import com.google.gson.annotations.SerializedName

/**
 * kim.bifrost.github.repository.network.model.Branch
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 19:54
 */
data class Branch(
    val name: String,
    @SerializedName("zipball_url")
    val zipballUrl: String,
    @SerializedName("tarball_url")
    val tarballUrl: String
)
