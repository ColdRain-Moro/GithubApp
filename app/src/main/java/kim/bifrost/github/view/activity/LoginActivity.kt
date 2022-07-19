package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import androidx.core.view.postDelayed
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityLoginBinding
import kim.bifrost.github.user.UserManager
import kim.bifrost.github.view.fragment.SplashFragment
import kim.bifrost.github.view.viewmodel.LoginViewModel
import kim.bifrost.lib_common.extensions.*
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

class LoginActivity : BaseVmBindActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("请稍等")
            .setMessage("正在跳转...")
            .show()

        fun sendError() {
            dialog.dismiss()
            MaterialAlertDialogBuilder(this)
                .setTitle("错误")
                .setMessage("解析url: ${intent.data} 失败")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }

        val uri = intent.data ?: return sendError()

        val code = uri.getQueryParameter("code") ?: return sendError()
        val state = uri.getQueryParameter("state") ?: return sendError()
        viewModel.getAccessToken(code, state)
        viewModel.oauthToken.collectLaunch { data ->
            if (data != null) {
                data.expires = System.currentTimeMillis() + data.expiresIn.toLong() * 1000
                data.refreshTokenExpires = System.currentTimeMillis() + data.refreshTokenExpiresIn.toLong() * 1000
                UserManager.authTokenData = data
                MainActivity.start(this@LoginActivity)
                // dismiss掉再finish 不然会导致window leaked
                dialog.dismiss()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashFragment = replaceFragment(R.id.splash_screen) {
            SplashFragment()
        }
        binding.splashScreen.postDelayed(1600) {
            // 存在缓存
            if (UserManager.authTokenData != null) {
                // access_token未过期
                if (UserManager.authTokenData!!.expires > System.currentTimeMillis()) {
                    MainActivity.start(this@LoginActivity)
                    finish()
                } else if (UserManager.authTokenData!!.refreshTokenExpires > System.currentTimeMillis()) {
                    // access_token过期，但refresh_token未过期
                    viewModel.getAccessTokenFromRefreshToken(UserManager.authTokenData!!.refreshToken)
                    viewModel.oauthToken.collectLaunch { data ->
                        if (data != null) {
                            UserManager.authTokenData = data
                            MainActivity.start(this@LoginActivity)
                            finish()
                        }
                    }
                } else {
                    UserManager.authTokenData = null
                    binding.splashScreen.startAnimation(
                        AlphaAnimation(1F, 0F).apply {
                            duration = 400
                            setOnEnd {
                                supportFragmentManager.commit {
                                    remove(splashFragment)
                                }
                                binding.splashScreen.gone()
                            }
                        }
                    )
                }
            } else {
                binding.splashScreen.startAnimation(
                    AlphaAnimation(1F, 0F).apply {
                        duration = 400
                        setOnEnd {
                            supportFragmentManager.commit {
                                remove(splashFragment)
                            }
                            binding.splashScreen.gone()
                        }
                    }
                )
            }
        }
        binding.btnLogin.setOnClickListener {
            UserManager.openOAuth2Page(this)
        }
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }
}