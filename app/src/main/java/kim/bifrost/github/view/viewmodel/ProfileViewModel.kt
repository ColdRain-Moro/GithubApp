package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.database.AppDatabase
import kim.bifrost.github.repository.database.entity.BookmarksEntity
import kim.bifrost.github.repository.database.entity.TraceEntity
import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.lib_common.extensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

/**
 * kim.bifrost.github.view.viewmodel.ProfileViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 19:01
 */
class ProfileViewModel : ViewModel() {

    lateinit var userName: String
    private var _user: MutableStateFlow<User?> = MutableStateFlow(null)

    val user: StateFlow<User?>
        get() = _user

    fun getUser() {
        viewModelScope.launch {
            _user.value = UserService.getUser(userName)
        }
    }

    fun addToBookmark() {
        viewModelScope.launch {
            if (user.value != null) {
                val local = user.value!!.local()
                tryRun {
                    AppDatabase.INSTANCE.localUserDao().insert(local)
                    AppDatabase.INSTANCE.bookmarksDao().insert(
                        BookmarksEntity(
                            userId = user.value!!.id.toInt(),
                            type = "user"
                        )
                    )
                    "Successfully added into your bookmarks".toast()
                } catchAll {
                    it.asString().toast()
                }
            }
        }
    }

    fun addToTrace() {
        viewModelScope.launch {
            if (user.value != null) {
                val local = user.value!!.local()
                tryRun {
                    AppDatabase.INSTANCE.localUserDao().insert(local)
                    AppDatabase.INSTANCE.traceDao().insert(
                        TraceEntity(
                            userId = user.value!!.id.toInt(),
                            type = "user",
                            time = Date()
                        )
                    )
                } catchAll {
                    it.asString().toast()
                }
            }
        }
    }
}