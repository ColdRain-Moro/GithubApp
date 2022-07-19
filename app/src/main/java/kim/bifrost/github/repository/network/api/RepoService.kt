package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.LanguageColorResp
import kim.bifrost.github.repository.network.model.RepoFile
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.repository.network.model.User
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

    /**
     * 获取指定用户star仓库列表
     *
     * @param user
     * @param page
     * @param perPage
     * @param sort
     * @return
     */
    @GET("/users/{user}/starred")
    suspend fun getUserStarredRepos(
        @Path("user") user: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "pushed",
    ): List<Repository>

    /**
     * 获取给指定仓库标星的列表
     *
     * @param owner
     * @param repo
     * @param page
     * @param perPage
     * @return
     */
    @GET("/repos/{owner}/{repo}/stargazers")
    suspend fun getRepoStargazers(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<User>

    /**
     * 获取指定仓库watcher
     *
     * @param owner
     * @param repo
     * @param page
     * @param perPage
     * @return
     */
    @GET("/repos/{owner}/{repo}/subscribers")
    suspend fun getRepoWatchers(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<User>

    /**
     * 获取仓库README
     * 没有README的话会抛404，所以要做好异常处理
     *
     * @param owner
     * @param repo
     * @param ref 默认为该仓库的默认branch
     * @return
     */
    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getRepoReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") ref: String? = null,
    ): RepoFile

    /**
     * 获取仓库文件列表
     *
     * @param owner
     * @param repo
     * @param path
     * @param branch
     * @return
     */
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoFiles(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path", encoded = true) path: String,
        @Query("ref") branch: String? = null,
    ): List<RepoFile>

    @GET("http://42.192.196.215:8082/lang/color")
    suspend fun getLanguageColor(
        @Query("lang") lang: String
    ): LanguageColorResp

    companion object : RepoService by RetrofitHelper.repoService
}