package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivitySearchBinding
import kim.bifrost.github.view.viewmodel.SearchViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity

/**
 * kim.bifrost.github.view.activity.SearchActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/22 14:59
 */
class SearchActivity : BaseVmBindActivity<SearchViewModel, ActivitySearchBinding>(isCancelStatusBar = false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_bast_match -> {
                viewModel.changeRepoSort(null)
                viewModel.changeUserSort(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }
}