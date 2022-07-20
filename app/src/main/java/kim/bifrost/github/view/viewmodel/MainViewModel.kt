package kim.bifrost.github.view.viewmodel

import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.tabs.TabLayout
import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.User
import kim.bifrost.github.user.UserManager
import kim.bifrost.github.view.activity.LoginActivity
import kim.bifrost.github.view.activity.MainActivity
import kim.bifrost.lib_common.extensions.TAG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.viewmodel.MainViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 21:20
 */
class MainViewModel : ViewModel() {
    private val _toolbarBus = MutableSharedFlow<Toolbar.() -> Unit>()
    private val _tabLayoutBus = MutableSharedFlow<TabLayout.() -> Unit>()
    private val _user = MutableStateFlow<User?>(null)

    val toolbarBus: SharedFlow<Toolbar.() -> Unit>
        get() = _toolbarBus
    val tabLayoutBus: SharedFlow<TabLayout.() -> Unit>
        get() = _tabLayoutBus
    val user: StateFlow<User?>
        get() = _user

    fun dispatchToolbarChange(event: Toolbar.() -> Unit) {
        viewModelScope.launch {
            _toolbarBus.emit(event)
        }
    }

    fun dispatchTabLayoutChange(event: TabLayout.() -> Unit) {
        viewModelScope.launch {
            _tabLayoutBus.emit(event)
        }
    }

    fun getSelf() {
        viewModelScope.launch {
            _user.value = UserService.getMe().also { UserManager.userTemp = it }
        }
    }

    fun logout(activity: MainActivity) {
        UserManager.authTokenData = null
        UserManager.userTemp = null
        LoginActivity.start(activity)
        activity.finish()
    }
}