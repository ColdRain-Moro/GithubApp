package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.RepoFile
import kim.bifrost.lib_common.extensions.toast
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
    private val _htmlFlow = MutableSharedFlow<Pair<String, String>>()
    private val _urlFlow = MutableSharedFlow<Pair<String, String>>()

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
    val mdFlow: SharedFlow<Pair<String, String>>
        get() = _htmlFlow
    val urlFlow: SharedFlow<Pair<String, String>>
        get() = _urlFlow

    private fun requestData(owner: String, repo: String, path: String, branch: String? = null) {
        viewModelScope.launch {
            _filesFlow.emit(RepoService.getRepoFiles(owner, repo, path, branch).sortFiles())
        }
    }

    private fun List<RepoFile>.sortFiles(): List<RepoFile> {
        val list = mutableListOf<RepoFile>()
        list.addAll(filter { it.type == RepoFile.Type.DIRECTORY }.sortedBy { it.name })
        list.addAll(filter { it.type == RepoFile.Type.FILE }.sortedBy { it.name })
        return list
    }

    fun openFile(file: RepoFile) {
        if (file.type == RepoFile.Type.FILE) {
            viewModelScope.launch {
                if (file.name.endsWith(".md")) {
                    val html = (RepoService.getFileAsRawStream(file.url).body() ?: return@launch let { "文件内容请求失败".toast() } )
                        .charStream().use { it.readText() }
                    _htmlFlow.emit(file.name to html)
                } else {
                    _urlFlow.emit(file.name to file.htmlUrl)
                }
            }
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