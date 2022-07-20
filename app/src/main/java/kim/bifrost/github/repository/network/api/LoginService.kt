package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.OAuthToken
import kim.bifrost.lib_common.utils.APP_CLIENT_ID
import kim.bifrost.lib_common.utils.APP_CLIENT_SECRET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * kim.bifrost.github.repository.network.api.GitHubApi
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 13:55
 */
interface LoginService {
    @POST("https://github.com/login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("state") state: String,
    ): OAuthToken

    companion object : LoginService by RetrofitHelper.loginService
}