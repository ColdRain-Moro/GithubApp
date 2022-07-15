package kim.bifrost.github.view.viewmodel

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayout
import kim.bifrost.github.network.api.UserService
import kim.bifrost.github.user.UserManager
import kim.bifrost.github.view.activity.LoginActivity
import kim.bifrost.github.view.activity.MainActivity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

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

    val toolbarBus: SharedFlow<Toolbar.() -> Unit> = _toolbarBus
    val tabLayoutBus: SharedFlow<TabLayout.() -> Unit> = _tabLayoutBus

    suspend fun dispatchToolbarChange(event: Toolbar.() -> Unit) {
        _toolbarBus.emit(event)
    }

    suspend fun dispatchTabLayoutChange(event: TabLayout.() -> Unit) {
        _tabLayoutBus.emit(event)
    }

    suspend fun getSelf() = UserService.getMe()

    fun logout(activity: MainActivity) {
        UserManager.authTokenData = null
        LoginActivity.start(activity)
        activity.finish()
    }
}