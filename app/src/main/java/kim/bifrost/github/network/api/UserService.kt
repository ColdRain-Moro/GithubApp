package kim.bifrost.github.network.api

import kim.bifrost.github.network.RetrofitHelper
import kim.bifrost.github.network.model.User
import kim.bifrost.github.network.model.event.Event
import retrofit2.http.*

/**
 * kim.bifrost.github.network.api.UserService
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 22:50
 */
interface UserService {
    @GET("users/{username}/received_events")
    @Headers("Accept: application/vnd.github.v3+json")
    fun getUserEvents(
        @Path("username") username: String,
        @Query("per_page") per_page: String,
        @Query("page") page: String
    ): List<Event>

    @GET("user")
    suspend fun getMe(): User

    @GET("users/{user}")
    suspend fun getUser(
        @Path("user") user: String
    ): User

    companion object : UserService by RetrofitHelper.userService
}