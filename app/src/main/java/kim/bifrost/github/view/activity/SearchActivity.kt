package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivitySearchBinding
import kim.bifrost.github.view.fragment.SearchResultFragment
import kim.bifrost.github.view.viewmodel.RepoSort
import kim.bifrost.github.view.viewmodel.SearchViewModel
import kim.bifrost.github.view.viewmodel.UserSort
import kim.bifrost.lib_common.base.adapter.BaseVPAdapter
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.visible

/**
 * kim.bifrost.github.view.activity.SearchActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/22 14:59
 */
class SearchActivity : BaseVmBindActivity<SearchViewModel, ActivitySearchBinding>(isCancelStatusBar = false) {

    private lateinit var searchView: SearchView
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.vp2.adapter = BaseVPAdapter(supportFragmentManager, lifecycle, listOf("REPO", "USER")) { _, i ->
            when (i) {
                0 -> SearchResultFragment.newInstance(SearchResultFragment.Type.REPO)
                1 -> SearchResultFragment.newInstance(SearchResultFragment.Type.USER)
                else -> error("")
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.vp2) { tab, position ->
            tab.text = when (position) {
                0 -> "REPOSITORIES"
                1 -> "USERS"
                else -> error("")
            }
        }.attach()
        viewModel.currentQuery.observe(this) {
            binding.toolbar.subtitle = "$it/" + if (binding.vp2.currentItem == 0)
                viewModel.repoSort.value?.let { v -> v.sort + " " + v.order } ?: "Best match"
            else viewModel.userSort.value?.let { v -> v.sort + " " + v.order } ?: "Best match"
            binding.tabLayout.visible()
        }
        binding.vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            var firstTime: Boolean = true

            override fun onPageSelected(position: Int) {
                if (::menu.isInitialized) {
                    val subMenu = menu.findItem(R.id.action_sort).subMenu
                    if (position == 0) {
                        subMenu.setGroupVisible(R.id.group_repo, true)
                        subMenu.setGroupVisible(R.id.group_user, false)
                    } else {
                        subMenu.setGroupVisible(R.id.group_user, true)
                        subMenu.setGroupVisible(R.id.group_repo, false)
                    }
                }
                if (firstTime) {
                    firstTime = false
                    return
                }
                binding.toolbar.subtitle = "${viewModel.currentQuery.value}/" + if (position == 0)
                    viewModel.repoSort.value?.let { v -> v.sort + " " + v.order } ?: "Best match"
                else viewModel.userSort.value?.let { v -> v.sort + " " + v.order } ?: "Best match"
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        this.menu = menu
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    return false
                }
                viewModel.submitQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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
            R.id.action_most_stars -> {
                viewModel.changeRepoSort(RepoSort.MOST_STARS)
            }
            R.id.action_fewest_stars -> {
                viewModel.changeRepoSort(RepoSort.LEAST_STARS)
            }
            R.id.action_most_forks -> {
                viewModel.changeRepoSort(RepoSort.MOST_FORKS)
            }
            R.id.action_fewest_forks -> {
                viewModel.changeRepoSort(RepoSort.LEAST_FORKS)
            }
            R.id.action_least_recently_updated -> {
                viewModel.changeRepoSort(RepoSort.LEAST_RECENTLY_UPDATED)
            }
            R.id.action_recently_updated -> {
                viewModel.changeRepoSort(RepoSort.RECENTLY_UPDATED)
            }
            R.id.action_most_recently_joined -> {
                viewModel.changeUserSort(UserSort.LATEST_JOINED)
            }
            R.id.action_least_recently_joined -> {
                viewModel.changeUserSort(UserSort.LEAST_LATEST_JOINED)
            }
            R.id.action_most_followers -> {
                viewModel.changeUserSort(UserSort.MOST_FOLLOWERS)
            }
            R.id.action_fewest_followers -> {
                viewModel.changeUserSort(UserSort.LEAST_FOLLOWERS)
            }
            R.id.action_fewest_repositories -> {
                viewModel.changeUserSort(UserSort.LEAST_REPOS)
            }
            R.id.action_most_repositories -> {
                viewModel.changeUserSort(UserSort.MOST_REPOS)
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