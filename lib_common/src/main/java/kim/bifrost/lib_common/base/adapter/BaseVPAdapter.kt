package kim.bifrost.lib_common.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * kim.bifrost.lib_common.base.adapter.BaseVPAdapter
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/15 10:26
 */
class BaseVPAdapter<D : Any>(
    fragment: FragmentManager,
    lifecycle: Lifecycle,
    private val types: List<D>,
    private val createFragmentFunc: (List<D>, Int) -> Fragment
) : FragmentStateAdapter(fragment, lifecycle) {

    override fun getItemCount(): Int = types.size

    override fun createFragment(position: Int): Fragment {
        return createFragmentFunc(types, position)
    }
}