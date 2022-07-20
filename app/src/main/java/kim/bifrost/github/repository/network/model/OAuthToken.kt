package kim.bifrost.github.repository.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * kim.bifrost.github.repository.network.model.OAuthToken
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 14:12
 */
@Parcelize
data class OAuthToken(
    @SerializedName("access_token")
    val accessToken: String,
    val scope: String,
): Parcelable
