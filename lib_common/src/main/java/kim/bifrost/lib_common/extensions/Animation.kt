package kim.bifrost.lib_common.extensions

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.core.app.ActivityOptionsCompat

/**
 * kim.bifrost.lib_common.extensions.Animation
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/14 15:11
 */
fun Animation.setOnEnd(func: () -> Unit) {
    setAnimationListener(
        object : Animation(), Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {
                func.invoke()
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        }
    )
}

fun Activity.makeSceneTransitionAnimation(
    vararg args: Pair<View, String>
): Bundle? {
    return ActivityOptionsCompat.makeSceneTransitionAnimation(this, *args.map { androidx.core.util.Pair.create(it.first, it.second) }.toTypedArray()).toBundle()
}