package kim.bifrost.github.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.databinding.ActivityIssuesBinding
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.user.UserManager
import kim.bifrost.github.utils.getValue
import kim.bifrost.github.view.fragment.IssuesFragment
import kim.bifrost.github.view.viewmodel.IssuesViewModel
import kim.bifrost.lib_common.base.adapter.BaseVPAdapter
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.argument

/**
 * kim.bifrost.github.view.activity.IssuesActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 0:11
 */
class IssuesActivity : BaseVmBindActivity<IssuesViewModel, ActivityIssuesBinding>(isCancelStatusBar = false) {

    @AutoWired
    lateinit var type: Type
    @AutoWired
    var repository: Repository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Issues"
            it.subtitle = if (type == Type.USER) UserManager.userTemp!!.login else repository!!.fullName
        }
        binding.vp2Issues.adapter = BaseVPAdapter(
            supportFragmentManager,
            lifecycle,
            listOf("OPEN", "CLOSED")
        ) { _, i ->
            return@BaseVPAdapter when (i) {
                0 -> if (type == Type.REPO)
                    IssuesFragment.newInstance(IssuesFragment.Type.REPO_ISSUES, IssuesFragment.State.OPEN, repository!!.owner.login, repository!!.name)
                else
                    IssuesFragment.newInstance(IssuesFragment.Type.MY_ISSUES, IssuesFragment.State.OPEN)
                1 -> if (type == Type.REPO)
                    IssuesFragment.newInstance(IssuesFragment.Type.REPO_ISSUES, IssuesFragment.State.CLOSED, repository!!.owner.login, repository!!.name)
                else
                    IssuesFragment.newInstance(IssuesFragment.Type.MY_ISSUES, IssuesFragment.State.CLOSED)
                else -> throw IllegalArgumentException("$i")
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.vp2Issues) { tab, position ->
            tab.text = when (position) {
                0 -> "OPEN"
                1 -> "CLOSED"
                else -> throw IllegalArgumentException("$position")
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    enum class Type {
        REPO,
        USER
    }

    companion object {
        fun start(context: Context, type: Type, repository: Repository? = null) {
            val starter = Intent(context, IssuesActivity::class.java)
                .argument("type", type)
                .argument("repository", repository)
            context.startActivity(starter)
        }
    }
}