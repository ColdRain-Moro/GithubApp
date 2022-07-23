package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.databinding.FragmentFilesBinding
import kim.bifrost.github.repository.network.model.RepoFile
import kim.bifrost.github.view.activity.WebActivity
import kim.bifrost.github.view.adapter.FilesAdapter
import kim.bifrost.github.view.adapter.PathAdapter
import kim.bifrost.github.view.viewmodel.FilesViewModel
import kim.bifrost.github.view.viewmodel.RepoViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindFragment
import kim.bifrost.lib_common.extensions.spiltWalkAsList

/**
 * kim.bifrost.github.view.fragment.FilesFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 16:53
 */
class FilesFragment : BaseVmBindFragment<FilesViewModel, FragmentFilesBinding>() {

    @AutoWired
    lateinit var repo: String
    @AutoWired
    lateinit var user: String
    @AutoWired
    var branch: String? = null

    private val activityViewModel by activityViewModels<RepoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inject()
        viewModel.init(user, repo, branch)
        binding.apply {
            val pathAdapter = PathAdapter {
                viewModel.path = it
            }
            val adapter = FilesAdapter {
                when (type) {
                    RepoFile.Type.FILE -> {
                        viewModel.openFile(this)
                    }
                    RepoFile.Type.DIRECTORY -> {
                        viewModel.path = path
                    }
                }
            }
            rvPath.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvPath.adapter = pathAdapter
            rvFiles.layoutManager = LinearLayoutManager(requireContext())
            rvFiles.adapter = adapter
            binding.srlFiles.setOnRefreshListener {
                viewModel.refresh()
            }
            viewModel.filesFlow.collectLaunch {
                adapter.submitList(it)
                pathAdapter.submitList(viewModel.path.spiltWalkAsList("/").run {
                    if (last() != "") {
                        mutableListOf("").apply { addAll(this@run) }
                    } else {
                        this
                    }
                })
                binding.srlFiles.isRefreshing = false
            }
            viewModel.urlFlow.collectLaunch { (name, value) ->
                WebActivity.start(requireContext(), WebActivity.Type.URL, name, url = value)
            }
            viewModel.mdFlow.collectLaunch { (name, value) ->
                WebActivity.start(requireContext(), WebActivity.Type.MD, name, mdSource = value)
            }
        }
        activityViewModel.currentBranch.observe(viewLifecycleOwner) {
            viewModel.branch = it
        }
    }

    companion object {
        fun newInstance(user: String, repo: String, branch: String? = null) =
            FilesFragment().apply {
                arguments = Bundle().apply {
                    putString("user", user)
                    putString("repo", repo)
                    putString("branch", branch)
                }
            }
    }
}