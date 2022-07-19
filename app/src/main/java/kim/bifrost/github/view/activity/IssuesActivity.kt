package kim.bifrost.github.view.activity

import android.os.Bundle
import kim.bifrost.github.databinding.ActivityIssuesBinding
import kim.bifrost.github.view.viewmodel.IssuesViewModel
import kim.bifrost.lib_common.base.ui.mvvm.BaseVmBindActivity

/**
 * kim.bifrost.github.view.activity.IssuesActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/20 0:11
 */
class IssuesActivity : BaseVmBindActivity<IssuesViewModel, ActivityIssuesBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}