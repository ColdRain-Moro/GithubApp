package kim.bifrost.lib_common.extensions

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt

/**
 * kim.bifrost.lib_common.extensions.SpannedText
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/18 20:35
 */
fun SpannableStringBuilder.appendClickableSpan(
    text: String,
    @ColorInt foregroundColor: Int? = null,
    @ColorInt backgroundColor: Int? = null,
    onClick: () -> Unit,
) {
    append(text)
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }
    }, length - text.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    foregroundColor?.let {
        setSpan(ForegroundColorSpan(it), length - text.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    backgroundColor?.let {
        setSpan(BackgroundColorSpan(it), length - text.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}