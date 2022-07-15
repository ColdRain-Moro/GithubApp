package kim.bifrost.lib_common.extensions

import android.view.animation.Animation

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