package kim.bifrost.github.network.interceptor

import kim.bifrost.github.user.UserManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * kim.bifrost.github.network.interceptor.AuthorizationInterceptor
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 9:38
 */
class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authTokenData = UserManager.authTokenData ?: return chain.proceed(chain.request())
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "token ${authTokenData.accessToken}")
            .build()
        return chain.proceed(newRequest)
    }
}