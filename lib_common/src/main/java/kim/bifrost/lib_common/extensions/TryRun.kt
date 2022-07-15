package kim.bifrost.lib_common.extensions

import kotlinx.coroutines.CancellationException


/**
 * kim.bifrost.lib_common.extensions.TryRun
 * GitHubApp
 * 利用kotlin1.5 inline class的特性简化协程中的异常处理
 * 详见 https://github.com/bennyhuo/TryRun
 *
 * @author 寒雨
 * @since 2022/7/14 14:22
 */
@JvmInline
value class TryRunResult(val throwable: Throwable?)

inline fun tryRun(block: () -> Unit): TryRunResult {
    return try {
        block()
        TryRunResult(null)
    } catch (e: Throwable) {
        TryRunResult(e)
    }
}

inline infix fun <reified T : Throwable> TryRunResult.catch(block: (t: T) -> Unit) {
    if (throwable is CancellationException) throw throwable
    if (throwable is T) {
        block(throwable)
    } else if (throwable != null) {
        throw throwable
    }
}

inline infix fun TryRunResult.catchAll(block: (t: Throwable) -> Unit) {
    if (throwable is CancellationException) throw throwable
    if (throwable != null) block(throwable)
}

inline infix fun <reified T : Throwable> TryRunResult.catching(block: (t: T) -> Unit): TryRunResult {
    return if (throwable is T) {
        block(throwable)
        TryRunResult(null)
    } else {
        this
    }
}

inline infix fun TryRunResult.finally(block: () -> Unit) {
    block()
    if (throwable != null) throw throwable
}