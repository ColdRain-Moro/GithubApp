package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Repository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

/**
 * kim.bifrost.github.view.viewmodel.RepoViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 20:07
 */
class RepoViewModel : ViewModel() {
    lateinit var repo: Repository

    val readme by lazy {
        flow {
            emit(RepoService.getRepoReadme(repo.owner.login, repo.name))
        }
    }
}