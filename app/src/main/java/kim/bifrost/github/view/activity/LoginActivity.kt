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
        lifecycleScope.launch {
            tryRun {
                val data = viewModel.getAccessToken(code, state)
                UserManager.authTokenData = data
                MainActivity.start(this@LoginActivity)
                // dismiss掉再finish 不然会导致window leaked
                dialog.dismiss()
                finish()
            } catchAll {
                dialog.dismiss()
                toast("网络请求错误: ${it.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashFragment = replaceFragment(R.id.splash_screen) {
            SplashFragment()
        }
        binding.splashScreen.postDelayed(1600) {
            binding.splashScreen.startAnimation(
                AlphaAnimation(1F, 0F).apply {
                    duration = 400
                    setOnEnd {
                        supportFragmentManager.commit {
                            remove(splashFragment)
                        }
                        binding.splashScreen.gone()
                        // 已经登录就直接进入
                        if (UserManager.authTokenData != null) {
                            Log.d(TAG, "onCreate: ${UserManager.authTokenData!!.accessToken}")
                            MainActivity.start(this@LoginActivity)
                            finish()
                        }
                    }
                }
            )
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