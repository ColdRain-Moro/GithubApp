package kim.bifrost.github.utils

import android.text.Spanned
import android.widget.TextView
import io.noties.markwon.Markwon
import kim.bifrost.lib_common.BaseApp

/**
 * kim.bifrost.github.utils.Markdown
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 14:29
 */
val markwon: Markwon = Markwon.create(BaseApp.appContext)

fun TextView.renderMarkdown(markdown: String) {
    markwon.setMarkdown(this, markdown)
}

fun String.parseMarkdown(): Spanned {
    return markwon.toMarkdown(this)
}