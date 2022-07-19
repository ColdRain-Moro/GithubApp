package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.RepoFile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.viewmodel.FilesViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 16:57
 */
class FilesViewModel : ViewModel() {

    private lateinit var user: String
    private lateinit var repo: String
    private val _filesFlow = MutableSharedFlow<List<RepoFile>>()
    var branch: String? = null
        set(value) {
            field = value
            path = ""
        }
    var path: String = ""
        set(value) {
            field = value
            requestData(user, repo, path, branch)
        }

    val filesFlow: SharedFlow<List<RepoFile>>
        get() = _filesFlow

    private fun requestData(owner: String, repo: String, path: String, branch: String? = null) {
        viewModelScope.launch {
            _filesFlow.emit(RepoService.getRepoFiles(owner, repo, path, branch))
        }
    }

    fun refresh() {
        requestData(user, repo, path, branch)
    }

    fun init(user: String, repo: String, branch: String?) {
        this.repo = repo
        this.user = user
        // branch改变会触发一次刷新
        this.branch = branch
    }
}