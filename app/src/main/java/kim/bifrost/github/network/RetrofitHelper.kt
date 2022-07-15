package kim.bifrost.github.network

import kim.bifrost.github.network.api.LoginService
import kim.bifrost.github.network.api.UserService
import kim.bifrost.github.network.interceptor.AuthorizationInterceptor
import kim.bifrost.lib_common.utils.GITHUB_API_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

/**
 * kim.bifrost.github.network.RetrofitHelper
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 13:55
 */
object RetrofitHelper {
    private val retrofit by lazy { initRetrofit() }

    val loginService by lazy { retrofit.create<LoginService>() }
    val userService by lazy { retrofit.create<UserService>() }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }
}