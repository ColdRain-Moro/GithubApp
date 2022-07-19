package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kim.bifrost.github.repository.network.api.LoginService
import kim.bifrost.github.repository.network.model.OAuthToken
import kim.bifrost.lib_common.utils.APP_CLIENT_ID
import kim.bifrost.lib_common.utils.APP_CLIENT_SECRET
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.viewmodel.LoginViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 13:26
 */
class LoginViewModel : ViewModel() {

    private val _oauthToken: MutableStateFlow<OAuthToken?> = MutableStateFlow(null)
    val oauthToken: StateFlow<OAuthToken?>
        get() = _oauthToken

    fun getAccessToken(code: String, state: String) {
        viewModelScope.launch {
            _oauthToken.emit(LoginService.getAccessToken(APP_CLIENT_ID, APP_CLIENT_SECRET, code, state))
        }
    }

    fun getAccessTokenFromRefreshToken(refreshToken: String) {
        viewModelScope.launch {
            _oauthToken.emit(LoginService.getAccessTokenFromRefreshToken(refreshToken))
        }
    }
}