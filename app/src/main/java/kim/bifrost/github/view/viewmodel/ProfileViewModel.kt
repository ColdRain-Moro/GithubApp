package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.network.api.UserService
import kim.bifrost.github.repository.network.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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
}