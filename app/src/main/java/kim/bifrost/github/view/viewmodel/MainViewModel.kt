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
    // fix: ViewModel中不能持有高阶函数或者函数式接口的匿名类的引用，因为其中可能会持有Activity或者Fragment的引用，这样就会导致内存泄漏
    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?>
        get() = _user

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