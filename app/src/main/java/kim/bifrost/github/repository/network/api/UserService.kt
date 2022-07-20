package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.Issue
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.github.repository.network.model.event.Event
import retrofit2.http.*

/**
 * kim.bifrost.github.repository.network.api.UserService
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 22:50
 */
interface UserService {
    /**
     * 获取指定用户收到的事件
     * 需要鉴权
     *
     * @param username 用户名
     * @param perPage 每页数量
     * @param page 页码
     * @return
     */
    @GET("users/{username}/received_events")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getUserReceivedEvents(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<Event>

    /**
     * 获取自己
     * 需要鉴权
     *
     * @return
     */
    @GET("user")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getMe(): User

    /**
     * 获取指定用户
     * 如果鉴权通过，返回完整信息，否则返回部分
     *
     * @param user
     * @return
     */
    @GET("users/{user}")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getUser(
        @Path("user") user: String
    ): User

    /**
     * 获取用户相关事件
     * 如果鉴权通过，返回完整信息，否则返回public部分
     *
     * @param user 用户名
     * @param perPage 每页数量
     * @param page 页码
     */
    @GET("users/{user}/events")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getUserEvents(
        @Path("user") user: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<Event>

    /**
     * 列出指定用户所有follower
     *
     * @param username
     * @param perPage
     * @param page
     * @return
     */
    @GET("/users/{username}/followers")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getFollowers(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<User>

    /**
     * 列出指定用户正在follow谁
     *
     * @param username
     * @param perPage
     * @param page
     * @return
     */
    @GET("/users/{username}/following")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getFollowing(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<User>

    /**
     * 列出授权用户所有issue
     * 需要鉴权
     *
     * @param page
     * @param perPage
     * @param state open / close / all
     * @param sort created / updated / comments
     * @return
     */
    @GET("/issues")
    suspend fun getUserIssues(
        @Query("state") state: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "created",
    ): List<Issue>

    companion object : UserService by RetrofitHelper.userService
}