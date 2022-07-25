package kim.bifrost.github.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityProfileBinding
import kim.bifrost.github.user.UserManager
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

    @AutoWired
    var avatarUrl: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        if (avatarUrl != null) {
            Glide.with(this)
                .load(avatarUrl)
                .into(binding.sivAvatar)
            Glide.with(this)
                .load(avatarUrl)
                .into(binding.ivBackground)
        }
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
                    if (user.name != UserManager.userTemp?.name) {
                        viewModel.addToTrace()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
            }
            R.id.action_bookmark -> {
                viewModel.addToBookmark()
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

        fun startWithAnimation(activity: Activity, user: String, view: View, avatarUrl: String) {
            val starter = Intent(activity, ProfileActivity::class.java)
                .putExtra("user", user)
                .putExtra("avatarUrl", avatarUrl)
            activity.startActivity(starter, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "profile").toBundle())
        }
    }
}