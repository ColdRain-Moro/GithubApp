package kim.bifrost.github.view.fragment

import android.os.Bundle
import android.view.View
import kim.bifrost.github.databinding.FragmentNewsBinding
import kim.bifrost.github.view.viewmodel.NewsViewModel
import kim.bifrost.lib_common.ui.mvvm.BaseVmBindFragment

/**
 * kim.bifrost.github.view.fragment.NewsFragment
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 22:34
 */
class NewsFragment : BaseVmBindFragment<NewsViewModel, FragmentNewsBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}