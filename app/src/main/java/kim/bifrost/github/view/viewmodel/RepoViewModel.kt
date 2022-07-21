package kim.bifrost.github.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.database.AppDatabase
import kim.bifrost.github.repository.database.entity.BookmarksEntity
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Branch
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.lib_common.extensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.viewmodel.RepoViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 20:07
 */
class RepoViewModel : ViewModel() {
    lateinit var repo: Repository
    private val _currentBranch = MutableLiveData<String>()
    private val _starred = MutableLiveData<Boolean>()

    val currentBranch: LiveData<String>
        get() = _currentBranch
    val starred: LiveData<Boolean>
        get() = _starred

    fun getReadme() = flow {
        emit(RepoService.getRepoReadme(repo.owner.login, repo.name, _currentBranch.value))
    }

    fun addToBookmark() {
        viewModelScope.launch(Dispatchers.IO) {
            tryRun {
                val local = repo.local()
                AppDatabase.INSTANCE.localRepoDao().insert(local)
                AppDatabase.INSTANCE.bookmarksDao().insert(
                    BookmarksEntity(
                        repoId = local.id,
                        type = "repo",
                    )
                )
                "Successfully added into your bookmarks".toastOnUIThread()
            } catchAll {
                it.asString().toastOnUIThread()
            }
        }
    }

    fun requestBranches(): Flow<List<String>> {
        return flow {
            emit(RepoService.getBranches(repo.owner.login, repo.name))
        }.map { it.map { s -> s.name } }
    }

    fun setCurrentBranch(branch: String) {
        _currentBranch.value = branch
    }

    fun initStarredState() {
        viewModelScope.launch {
            _starred.value = RepoService.checkRepoStarred(repo.owner.login, repo.name).code() == 204
        }
    }

    fun setStarred(starred: Boolean) {
        viewModelScope.launch {
            val success = if (starred) {
                RepoService.starRepo(repo.owner.login, repo.name).code() == 204
            } else {
                RepoService.unstarRepo(repo.owner.login, repo.name).code() == 204
            }
            if (success) {
                _starred.value = starred
            }
        }
    }
}