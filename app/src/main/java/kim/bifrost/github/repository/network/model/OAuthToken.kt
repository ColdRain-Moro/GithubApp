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
    @SerializedName("refresh_token")
    val refreshToken: String,
    val scope: String,
    @SerializedName("expires_in")
    val expiresIn: String,
    @SerializedName("refresh_token_expires_in")
    val refreshTokenExpiresIn: String,
    @SerializedName("token_type")
    val tokenType: String
): Parcelable
