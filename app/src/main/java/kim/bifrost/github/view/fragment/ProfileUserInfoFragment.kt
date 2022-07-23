package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kim.bifrost.github.databinding.FragmentUserInfoBinding
import kim.bifrost.github.view.activity.ItemListActivity
import kim.bifrost.github.view.viewmodel.ProfileViewModel
import kim.bifrost.lib_common.base.ui.BaseBindFragment
import kotlinx.coroutines.launch

/**
 * kim.bifrost.github.view.fragment.InfoFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 21:07
 */
class ProfileUserInfoFragment : BaseBindFragment<FragmentUserInfoBinding>() {

    private val viewModel by activityViewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inject()
        viewModel.user.collectLaunch { user ->
            if (user != null) {
                binding.apply {
                    tvUserName.text = user.name
                    if (user.bio == null || user.bio.isEmpty()) {
                        tvDesc.visibility = View.GONE
                    } else {
                        tvDesc.text = user.bio
                    }
                    tvGroup.text = user.company
                    tvLink.text = user.blog
                    tvFollowers.text = user.followers.toString()
                    tvFollowing.text = user.following.toString()
                    tvRepositories.text = user.publicRepos.toString()
                    tvGists.text = user.publicGists.toString()
                    llFollowers.setOnClickListener {
                        ItemListActivity.start(requireContext(), ItemListActivity.Type.USER_FOLLOWERS, user.login)
                    }
                    llFollowing.setOnClickListener {
                        ItemListActivity.start(requireContext(), ItemListActivity.Type.USER_FOLLOWERS, user.login)
                    }
                    llRepo.setOnClickListener {
                        ItemListActivity.start(requireContext(), ItemListActivity.Type.USER_REPOSITORIES, user.login)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): ProfileUserInfoFragment {
            val args = Bundle()
            val fragment = ProfileUserInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}