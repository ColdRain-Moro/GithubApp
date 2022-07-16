package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityMainBinding
import kim.bifrost.github.view.fragment.EventsFragment
import kim.bifrost.github.view.viewmodel.MainViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.utils.asString
import kotlinx.coroutines.launch

class MainActivity : BaseVmBindActivity<MainViewModel, ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        if (viewModel.user.value != null) {
                            ProfileActivity.start(this@MainActivity, viewModel.user.value!!.login)
                        }
                    }
                }
                true
            }
            getHeaderView(0).apply {
                val avatar = findViewById<ShapeableImageView>(R.id.iv_avatar)
                val name = findViewById<TextView>(R.id.tv_name)
                val desc = findViewById<TextView>(R.id.tv_desc)
                viewModel.user.collectLaunch { user ->
                    Glide.with(avatar)
                        .load(user!!.avatarUrl)
                        .into(avatar)
                    name.text = user.name
                    desc.text = user.bio.ifBlank { "Joined at " + user.createdAt.asString() }
                }
                viewModel.getSelf()
            }
        }
        replaceFragment(R.id.fragment_container) {
            EventsFragment.newInstance(EventsFragment.SourceType.NEWS)
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

    companion object {
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }
}