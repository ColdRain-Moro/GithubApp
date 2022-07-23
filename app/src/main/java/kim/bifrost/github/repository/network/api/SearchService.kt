package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.repository.network.model.SearchPager
import kim.bifrost.github.repository.network.model.User
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * kim.bifrost.github.repository.network.api.SearchService
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/22 17:02
 */
interface SearchService {
    /**
     * 搜索仓库
     *
     * @param q
     * @param sort null -> best match | stars | forks | updated | help-wanted-issues
     * @param order desc asc
     * @param perPage
     * @param page
     * @return
     */
    @GET("/search/repositories")
    suspend fun searchRepo(
        @Query("q") q: String,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ) : SearchPager<Repository>

    /**
     * 搜索用户
     *
     * @param q
     * @param sort null -> best match | followers | repositories | joined
     * @param order desc asc
     * @param perPage
     * @param page
     * @return
     */
    @GET("/search/users")
    suspend fun searchUser(
        @Query("q") q: String,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ) : SearchPager<User>

    companion object : SearchService by RetrofitHelper.searchService
}