package kim.bifrost.github.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.transition.Visibility
import android.view.*
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.R
import kim.bifrost.github.databinding.ActivityRepoBinding
import kim.bifrost.github.repository.network.model.Repository
import kim.bifrost.github.view.adapter.BranchAdapter
import kim.bifrost.github.view.fragment.CommitFragment
import kim.bifrost.github.view.fragment.EventsFragment
import kim.bifrost.github.view.fragment.FilesFragment
import kim.bifrost.github.view.fragment.RepoInfoFragment
import kim.bifrost.github.view.viewmodel.RepoViewModel
import kim.bifrost.lib_common.base.adapter.BaseVPAdapter
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity
import kim.bifrost.lib_common.extensions.argument
import kim.bifrost.lib_common.extensions.drawable
import kim.bifrost.lib_common.extensions.makeSceneTransitionAnimation
import kim.bifrost.lib_common.extensions.toast

/**
 * kim.bifrost.github.view.activity.RepositoryActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 20:04
 */
class RepositoryActivity : BaseVmBindActivity<RepoViewModel, ActivityRepoBinding>() {

    @AutoWired
    lateinit var repo: Repository

    @AutoWired
    var transitionName: String? = null

    private lateinit var menu: Menu

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        window.enterTransition = buildEnterTransition()
        postponeEnterTransition()
        val decorView = window.decorView
        window.decorView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                decorView.viewTreeObserver.removeOnPreDrawListener(this)
                supportStartPostponedEnterTransition()
                return true
            }
        })
        viewModel.repo = repo
        viewModel.addToTrace()
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                title = repo.name
            }
            tvDesc.text = repo.description
            tvLang.text = "Language ${repo.language}"
            Glide.with(ivBackground)
                .load(repo.owner.avatarUrl)
                .into(ivBackground)
            vp2Main.adapter = BaseVPAdapter(
                supportFragmentManager,
                lifecycle,
                listOf("INFO", "FILES", "COMMITS", "ACTIVITY")
            ) { _, i ->
                return@BaseVPAdapter when (i) {
                    0 -> RepoInfoFragment.newInstance()
                    1 -> FilesFragment.newInstance(repo.owner.login, repo.name)
                    2 -> CommitFragment.newInstance(repo)
                    3 -> EventsFragment.newInstance(EventsFragment.SourceType.REPO, repo.owner.login, repo.name)
                    else -> Fragment()
                }
            }
            TabLayoutMediator(tabLayout, vp2Main) { tab, position ->
                tab.text = when (position) {
                    0 -> "INFO"
                    1 -> "FILES"
                    2 -> "COMMITS"
                    3 -> "ACTIVITY"
                    else -> throw IllegalArgumentException("Invalid position $position")
                }
            }.attach()
        }
        var firstTime = true
        viewModel.initStarredState()
        viewModel.starred.observe(this) {
            menu.findItem(R.id.action_star).icon = if (it) {
                R.drawable.ic_star_title
            } else {
                R.drawable.ic_menu_star
            }.drawable
            if (!firstTime) {
                if (it) {
                    "Starred"
                } else {
                    "Unstarred"
                }.toast()
            }
            firstTime = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_repository, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
            }
            R.id.action_star -> {
                if (viewModel.starred.value != null) {
                    viewModel.setStarred(!viewModel.starred.value!!)
                }
            }
            R.id.action_branch -> {
                showBranchSelectDialog()
            }
            R.id.action_bookmark -> {
                viewModel.addToBookmark()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBranchSelectDialog() {
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle("Select Branch")
            .setView(recyclerView)
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
        val adapter = BranchAdapter(viewModel.currentBranch.value) {
            viewModel.setCurrentBranch(it)
            dialog.dismiss()
        }
        recyclerView.adapter = adapter
        viewModel.requestBranches().collectLaunch {
            adapter.submitList(it)
        }
    }

    private fun buildEnterTransition(): Visibility {
        val slide = Slide()
        slide.duration = 500
        slide.slideEdge = Gravity.BOTTOM
        slide.interpolator = OvershootInterpolator(0.5F)
        return slide
    }

    companion object {
        fun start(context: Context, repo: Repository) {
            val starter = Intent(context, RepositoryActivity::class.java)
                .argument("repo", repo)
            context.startActivity(starter)
        }

        fun startWithAnimation(activity: Activity, repo: Repository) {
            val starter = Intent(activity, RepositoryActivity::class.java)
                .argument("repo", repo)
//                .argument("transitionName", sharedElement.transitionName)
            activity.startActivity(starter, activity.makeSceneTransitionAnimation(
//                sharedElement to sharedElement.transitionName,
//                activity.window.decorView.findViewById<View>(android.R.id.statusBarBackground) to Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME,
//                activity.findViewById<View>(android.R.id.navigationBarBackground) to Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME
            ))
        }
    }
}