package kim.bifrost.github.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.github.databinding.ActivityProfileBinding
import kim.bifrost.github.view.fragment.EventsFragment
import kim.bifrost.github.view.fragment.ProfileUserInfoFragment
import kim.bifrost.github.view.fragment.RepositoriesFragment
import kim.bifrost.github.view.viewmodel.ProfileViewModel
import kim.bifrost.lib_common.base.adapter.BaseVPAdapter
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.utils.asEnglishString
import kim.bifrost.lib_common.utils.asString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.activity.ProfileActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 19:00
 */
class ProfileActivity : BaseVmBindActivity<ProfileViewModel, ActivityProfileBinding>(isCancelStatusBar = false) {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUser()
        viewModel.userName = intent.getStringExtra("user")!!
        binding.apply {
            vp2Main.adapter = BaseVPAdapter(
                supportFragmentManager,
                lifecycle,
                listOf("INFO", "ACTIVITY", "STARRED")
            ) { s, i ->
                return@BaseVPAdapter when (s[i]) {
                    "INFO" -> ProfileUserInfoFragment.newInstance()
                    "ACTIVITY" -> EventsFragment.newInstance(EventsFragment.SourceType.USER, user = viewModel.userName)
                    "STARRED" -> RepositoriesFragment.newInstance(viewModel.userName)
                    else -> throw IllegalArgumentException("$s[$i]")
                }
            }
            TabLayoutMediator(tabLayout, vp2Main) { tab, position ->
                tab.text = when (position) {
                    0 -> "INFO"
                    1 -> "ACTIVITY"
                    2 -> "STARRED"
                    else -> throw IllegalArgumentException("$position")
                }
            }.attach()
            viewModel.user.collectLaunch { user ->
                toolbar.title = user!!.login
                Glide.with(ivBackground)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(ivBackground)
                Glide.with(sivAvatar)
                    .load(user.avatarUrl)
                    .into(sivAvatar)
                tvPlace.text = user.location
                tvJoinedTime.text = "Joined at" + user.createdAt.asEnglishString()
            }
        }
    }

    companion object {
        fun start(context: Context, user: String) {
            val starter = Intent(context, ProfileActivity::class.java)
                .putExtra("user", user)
            context.startActivity(starter)
        }
    }
}