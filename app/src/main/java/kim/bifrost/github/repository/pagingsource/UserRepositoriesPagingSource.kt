package kim.bifrost.github.repository.pagingsource

import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.lib_common.base.adapter.BasePagingSource

/**
 * kim.bifrost.github.repository.pagingsource.UserRepositoriesPagingSource
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 21:18
 */
class UserRepositoriesPagingSource(private val user: String) : BasePagingSource<Repository>() {
    override suspend fun getData(page: Int): List<Repository> {
        return RepoService.getUserRepos(user, page + 1, 20)
                // 请求颜色
            .map { repo ->
                repo.language?.let {
                    repo.languageColor = RepoService.getLanguageColor(it).data?.hex
                }
                return@map repo
            }
    }
}