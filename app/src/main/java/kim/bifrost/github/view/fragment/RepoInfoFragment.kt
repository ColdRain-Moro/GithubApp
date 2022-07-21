package kim.bifrost.github.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import kim.bifrost.github.databinding.FragmentRepoInfoBinding
import kim.bifrost.github.utils.renderMarkdown
import kim.bifrost.github.view.activity.IssuesActivity
import kim.bifrost.github.view.activity.ItemListActivity
import kim.bifrost.github.view.viewmodel.RepoViewModel
import kim.bifrost.lib_common.base.ui.BaseBindFragment
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.invisible
import kim.bifrost.lib_common.extensions.toast
import kim.bifrost.lib_common.utils.asEnglishString
import kim.bifrost.lib_common.utils.getNewsTimeStr
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import okio.ByteString.Companion.decodeBase64

/**
 * kim.bifrost.github.view.fragment.RepoInfoFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/17 23:44
 */
class RepoInfoFragment : BaseBindFragment<FragmentRepoInfoBinding>() {

    private val viewModel by viewModels<RepoViewModel>(ownerProducer = { requireActivity() })
    private lateinit var readmeJob: Job

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            tvRepoName.text = viewModel.repo.fullName
            tvTime.text = "Created at ${viewModel.repo.createdAt.asEnglishString()}, last pushed at ${getNewsTimeStr(viewModel.repo.pushedAt)}"
            tvIssues.text = viewModel.repo.openIssuesCount.toString()
            tvForks.text = viewModel.repo.forksCount.toString()
            tvStargazers.text = viewModel.repo.stargazersCount.toString()
            tvWatchers.text = viewModel.repo.watchersCount.toString()
            llStargazers.setOnClickListener {
                ItemListActivity.start(requireContext(), ItemListActivity.Type.REPO_STARGAZERS, viewModel.repo.owner.login, viewModel.repo.name)
            }
            llWatchers.setOnClickListener {
                ItemListActivity.start(requireContext(), ItemListActivity.Type.REPO_WATCHERS, viewModel.repo.owner.login, viewModel.repo.name)
            }
            llIssues.setOnClickListener {
                IssuesActivity.start(requireContext(), IssuesActivity.Type.REPO, viewModel.repo)
            }
            llForks.setOnClickListener {
                ItemListActivity.start(requireContext(), ItemListActivity.Type.REPO_FORKS, viewModel.repo.owner.login, viewModel.repo.name)
            }
            readmeJob = viewModel.getReadme().catch {
                cardReadme.gone()
            }.collectLaunch {
                tvReadme.renderMarkdown((it.content!!.decodeBase64() ?: return@collectLaunch let{ "base64解码失败".toast() }).utf8())
                readmeLoader.invisible()
            }
            viewModel.currentBranch.observe(viewLifecycleOwner) {
                // 关流
                readmeJob.cancel()
                // 重开
                readmeJob = viewModel.getReadme().catch {
                    cardReadme.gone()
                }.collectLaunch {
                    tvReadme.renderMarkdown((it.content!!.decodeBase64() ?: return@collectLaunch let{ "base64解码失败".toast() }).utf8())
                    readmeLoader.invisible()
                }
            }
        }
    }

    companion object {
        fun newInstance(): RepoInfoFragment {
            val args = Bundle()
            val fragment = RepoInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}