package kim.bifrost.github.repository.network.api

import kim.bifrost.github.repository.network.RetrofitHelper
import kim.bifrost.github.repository.network.model.*
import kim.bifrost.github.repository.network.model.event.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

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
    @GET("/repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoFiles(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path", encoded = true) path: String,
        @Query("ref") branch: String? = null,
    ): List<RepoFile>

    /**
     * 以HTML格式获取文件内容
     *
     * @param url
     * @return
     */
    @GET
    @Headers("Accept: application/vnd.github.html")
    suspend fun getFileAsHtmlStream(
        @Url url: String
    ): Response<ResponseBody>

    /**
     * 以raw格式获取文件内容
     *
     * @param url
     * @return
     */
    @GET
    @Headers("Accept: application/vnd.github.VERSION.raw")
    suspend fun getFileAsRawStream(
        @Url url: String
    ): Response<ResponseBody>

    /**
     * 获取仓库commit
     *
     * @param owner
     * @param repo
     * @param branch SHA or branch to start listing commits from. Default: the repository’s default branch (usually master).
     * @param page
     * @param perPage
     * @return
     */
    @GET("/repos/{owner}/{repo}/commits")
    suspend fun getRepoCommits(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("sha") branch: String? = null,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Commit>

    /**
     * 获取仓库事件
     *
     * @param owner
     * @param repo
     * @param page
     * @param perPage
     * @return
     */
    @GET("/repos/{owner}/{repo}/events")
    suspend fun getRepositoryEvents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Event>

    /**
     * 获取仓库Issues
     *
     * @param owner
     * @param repo
     * @param state open / close / all
     * @param sort created / updated / comments
     * @param page
     * @param perPage
     * @return
     */
    @GET("/repos/{owner}/{repo}/issues")
    suspend fun getRepositoryIssues(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "created",
    ): List<Issue>

    /**
     * 列出仓库所有fork
     *
     * @param owner
     * @param repo
     * @param page
     * @param perPage
     * @param sort newest oldest stargazers watchers
     */
    @GET("/repos/{owner}/{repo}/forks")
    suspend fun getRepositoryForks(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String = "newest",
    ): List<Repository>

    /**
     * 点star
     *
     * @param owner
     * @param repo
     * @return
     */
    @PUT("user/starred/{owner}/{repo}")
    suspend fun starRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<ResponseBody>

    /**
     * 取消star
     *
     * @param owner
     * @param repo
     * @return
     */
    @DELETE("user/starred/{owner}/{repo}")
    suspend fun unstarRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<ResponseBody>

    /**
     * Check if you are starring a repository
     */
    @GET("user/starred/{owner}/{repo}")
    suspend fun checkRepoStarred(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<ResponseBody>

    /**
     * 获取语言对应颜色
     *
     * @param lang
     * @return
     */
    @GET("http://42.192.196.215:8082/lang/color")
    suspend fun getLanguageColor(
        @Query("lang") lang: String
    ): LanguageColorResp

    companion object : RepoService by RetrofitHelper.repoService
}