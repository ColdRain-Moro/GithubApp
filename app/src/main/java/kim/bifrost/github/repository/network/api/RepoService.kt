package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.LanguageColorResp
import kim.bifrost.github.repository.network.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * kim.bifrost.github.repository.network.api.RepoService
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 17:30
 */
interface RepoService {

    /**
     * 获取指定用户仓库列表
     * 鉴权不通过的话只会返回public的仓库
     *
     * @param user 用户
     * @param page
     * @param perPage
     * @param sort 排序方式 created, updated, pushed, full_name
     * @return
     */
    @GET("/users/{user}/repos")
    suspend fun getUserRepos(
        @Path("user") user: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "pushed",
    ): List<Repository>



    @GET("http://42.192.196.215:8082/lang/color")
    suspend fun getLanguageColor(
        @Query("lang") lang: String
    ): LanguageColorResp

    companion object : RepoService by RetrofitHelper.repoService
}