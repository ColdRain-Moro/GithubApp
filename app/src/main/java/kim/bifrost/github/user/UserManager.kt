package kim.bifrost.github.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import kim.bifrost.github.repository.network.model.OAuthToken
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.extensions.defaultSp
import kim.bifrost.lib_common.extensions.fromJson
import kim.bifrost.lib_common.extensions.toJson
import kim.bifrost.lib_common.utils.APP_CLIENT_ID
import kim.bifrost.lib_common.utils.OAUTH2_URL
import kim.bifrost.lib_common.utils.OAUTH_TOKEN_DATA
import kim.bifrost.lib_common.utils.USER_TEMP
import java.util.*

/**
 * kim.bifrost.github.user.UserManager
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 14:40
 */
object UserManager {
    var authTokenData: OAuthToken? = null
        get() {
            if (field == null) {
                field = defaultSp.getString(OAUTH_TOKEN_DATA, null)?.fromJson()
            }
            Log.d(TAG, ": $field")
            return field
        }
        set(value) {
            field = value
            defaultSp.edit { putString(OAUTH_TOKEN_DATA, value?.toJson()) }
        }

    var userTemp: User? = null
        get() {
            if (field == null) {
                field = defaultSp.getString(USER_TEMP, null)?.fromJson()
            }
            return field
        }
        set(value) {
            field = value
            defaultSp.edit(commit = true) { putString(USER_TEMP, value?.toJson()) }
        }

    fun openOAuth2Page(context: Context) {
        val url = OAUTH2_URL +
                "?client_id=" + APP_CLIENT_ID +
                "&state=" + UUID.randomUUID().toString() +
                "&scope=" + "user,repo,gist,notifications"
        Log.d(TAG, "openOAuth2Page: $url")
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .run {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        context.startActivity(intent)
    }
}