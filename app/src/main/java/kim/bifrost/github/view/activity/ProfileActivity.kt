package kim.bifrost.github.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.github.databinding.ActivityProfileBinding
import kim.bifrost.github.view.fragment.EventsFragment
import kim.bifrost.github.view.fragment.ProfileUserInfoFragment
import kim.bifrost.github.view.fragment.RepositoriesFragment
import kim.bifrost.github.view.viewmodel.ProfileViewModel
import kim.bifrost.lib_common.base.adapter.BaseVPAdapter
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.utils.asEnglishString

/**
 * kim.bifrost.github.view.activity.ProfileActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 19:00
 */
class ProfileActivity : BaseVmBindActivity<ProfileViewModel, ActivityProfileBinding>(isCancelStatusBar = true) {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userName = intent.getStringExtra("user")!!
        viewModel.getUser()
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = viewModel.userName
            }
            val itemList = listOf(
                ProfileUserInfoFragment.newInstance(),
                EventsFragment.newInstance(EventsFragment.SourceType.USER, viewModel.userName),
                RepositoriesFragment.newInstance(RepositoriesFragment.Type.USER_STARRED, viewModel.userName)
            )
            vp2Main.adapter = BaseVPAdapter(
                supportFragmentManager,
                lifecycle,
                listOf("INFO", "ACTIVITY", "STARRED")
            ) { _, i ->
                return@BaseVPAdapter itemList[i]
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
                if (user != null) {
                    toolbar.title = user.login
                    Glide.with(ivBackground)
                        .load(user.avatarUrl)
                        .into(ivBackground)
                    Glide.with(sivAvatar)
                        .load(user.avatarUrl)
                        .into(sivAvatar)
                    tvPlace.text = user.location
                    tvJoinedTime.text = "Joined at " + user.createdAt.asEnglishString()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, user: String) {
            val starter = Intent(context, ProfileActivity::class.java)
                .putExtra("user", user)
            context.startActivity(starter)
        }
    }
}