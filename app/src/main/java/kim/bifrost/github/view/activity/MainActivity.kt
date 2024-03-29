package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityMainBinding
import kim.bifrost.github.user.UserManager
import kim.bifrost.github.view.fragment.EventsFragment
import kim.bifrost.github.view.fragment.RepositoriesFragment
import kim.bifrost.github.view.viewmodel.MainViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.extensions.ifNullOrEmpty
import kim.bifrost.lib_common.utils.asString
import kotlinx.coroutines.launch

class MainActivity : BaseVmBindActivity<MainViewModel, ActivityMainBinding>(isCancelStatusBar = false) {

    private lateinit var avatar: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }
        binding.navView.apply {
            setCheckedItem(R.id.nav_news)
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_logout -> {
                        viewModel.logout(this@MainActivity)
                    }
                    R.id.nav_profile -> {
                        // 防止数据倒灌
                        if (viewModel.user.value != null) {
                            ProfileActivity.startWithAnimation(this@MainActivity, viewModel.user.value!!.login, avatar, viewModel.user.value!!.avatarUrl)
                        }
                    }
                    R.id.nav_owned -> {
                        if (viewModel.user.value != null) {
                            supportActionBar?.title = "Repositories"
                            supportFragmentManager.commit {
                                replace(
                                    R.id.fragment_container,
                                    RepositoriesFragment.newInstance(RepositoriesFragment.Type.USER, viewModel.user.value!!.login)
                                )
                            }
                            closeDrawer()
                        }
                    }
                    R.id.nav_starred -> {
                        if (viewModel.user.value != null) {
                            supportActionBar?.title = "Starred Repositories"
                            // 由于是同种类型的fragment，所以不能使用郭神的replaceFragment
                            supportFragmentManager.commit {
                                replace(
                                    R.id.fragment_container,
                                    RepositoriesFragment.newInstance(RepositoriesFragment.Type.USER_STARRED, viewModel.user.value!!.login)
                                )
                            }
                            closeDrawer()
                        }
                    }
                    R.id.nav_news -> {
                        if (viewModel.user.value != null) {
                            supportActionBar?.title = "News"
                            replaceFragment(R.id.fragment_container) {
                                EventsFragment.newInstance(
                                    EventsFragment.SourceType.NEWS,
                                    viewModel.user.value!!.login
                                )
                            }
                            closeDrawer()
                        }
                    }
                    R.id.nav_issues -> {
                        if (viewModel.user.value != null) {
                            IssuesActivity.start(this@MainActivity, IssuesActivity.Type.USER)
                        }
                    }
                    R.id.nav_bookmarks -> {
                        if (viewModel.user.value != null) {
                            ItemListActivity.start(this@MainActivity, ItemListActivity.Type.BOOKMARKS, viewModel.user.value!!.login)
                        }
                    }
                    R.id.nav_trace -> {
                        if (viewModel.user.value != null) {
                            ItemListActivity.start(this@MainActivity, ItemListActivity.Type.TRACE, viewModel.user.value!!.login)
                        }
                    }
                    R.id.nav_search -> {
                        SearchActivity.start(this@MainActivity)
                    }
                }
                true
            }
            getHeaderView(0).apply {
                avatar = findViewById(R.id.iv_avatar)
                val name = findViewById<TextView>(R.id.tv_name)
                val desc = findViewById<TextView>(R.id.tv_desc)
                viewModel.user.collectLaunch { user ->
                    // 刚被订阅时，StateFlow会立刻发送最新的值，所以需要做一下判断
                    if (user != null) {
                        Glide.with(avatar)
                            .load(user.avatarUrl)
                            .into(avatar)
                        name.text = user.name
                        desc.text = user.bio.ifNullOrEmpty { "Joined at " + user.createdAt.asString() }
                        replaceFragment(R.id.fragment_container) {
                            EventsFragment.newInstance(EventsFragment.SourceType.NEWS, user.login)
                        }
                    }
                }
                viewModel.getSelf()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(binding.navView)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(binding.navView)
    }

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}