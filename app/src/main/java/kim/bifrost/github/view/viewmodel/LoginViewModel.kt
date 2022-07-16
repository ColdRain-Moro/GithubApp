package kim.bifrost.github.view.viewmodel

import androidx.lifecycle.ViewModel
import kim.bifrost.github.repository.network.api.LoginService
import kim.bifrost.lib_common.utils.APP_CLIENT_ID
import kim.bifrost.lib_common.utils.APP_CLIENT_SECRET

/**
 * kim.bifrost.github.view.viewmodel.LoginViewModel
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 13:26
 */
class LoginViewModel : ViewModel() {
    suspend fun getAccessToken(code: String, state: String)
        = LoginService.getAccessToken(APP_CLIENT_ID, APP_CLIENT_SECRET, code, state)
}