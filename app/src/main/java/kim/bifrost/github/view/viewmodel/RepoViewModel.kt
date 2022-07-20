package kim.bifrost.github.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.network.api.RepoService
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.lib_common.extensions.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
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
    var currentBranch: String? = null

    private val _starred = MutableLiveData<Boolean>()

    val starred: LiveData<Boolean>
        get() = _starred

    fun initStarredState() {
        viewModelScope.launch {
            _starred.value = RepoService.checkRepoStarred(repo.owner.login, repo.name).code() == 204
        }
    }

    fun setStarred(starred: Boolean) {
        viewModelScope.launch {
            val success = if (starred) {
                RepoService.starRepo(repo.owner.login, repo.name).code().also {
                    Log.d(
                        TAG,
                        "star: $it"
                    ) } == 204
            } else {
                RepoService.unstarRepo(repo.owner.login, repo.name).code().also {
                    Log.d(
                        TAG,
                        "unstar: $it"
                    ) } == 204
            }
            if (success) {
                _starred.value = starred
            }
        }
    }

    val readme by lazy {
        flow {
            emit(RepoService.getRepoReadme(repo.owner.login, repo.name))
        }
    }
}