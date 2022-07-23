package kim.bifrost.github.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.*
import kim.bifrost.annotations.AutoWired
import kim.bifrost.github.databinding.ActivityWebBinding
import kim.bifrost.github.utils.renderMarkdown
import kim.bifrost.lib_common.base.ui.BaseBindActivity
import kim.bifrost.lib_common.extensions.TAG
import kim.bifrost.lib_common.extensions.argument
import kim.bifrost.lib_common.extensions.gone
import kim.bifrost.lib_common.extensions.visible

/**
 * kim.bifrost.github.view.activity.WebActivity
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/19 14:38
 */
class WebActivity : BaseBindActivity<ActivityWebBinding>(isCancelStatusBar = false) {

    @AutoWired
    private lateinit var title: String

    @AutoWired
    var url: String? = null

    @AutoWired
    var mdSource: String? = null

    @AutoWired
    lateinit var type: Type

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.title = title
            it.setDisplayHomeAsUpEnabled(true)
        }
        val settings: WebSettings = binding.webView.settings

        // 支持JS
        settings.javaScriptEnabled = true

        // 自适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        // 支持缩放
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true

        // 隐藏缩放控件
        settings.displayZoomControls = false

        // 开启Dom存储功能
        settings.domStorageEnabled = true

        // 开启文件访问
        settings.allowFileAccess = true
        settings.loadsImagesAutomatically = true
        settings.javaScriptCanOpenWindowsAutomatically = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.loader.gone()
            }
        }
        when (type) {
            Type.URL -> {
                binding.webView.loadUrl(url!!)
            }
            Type.MD -> {
                binding.webView.gone()
                binding.tvMd.visible()
                binding.tvMd.renderMarkdown(mdSource!!)
                binding.loader.gone()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    enum class Type {
        URL, MD
    }

    companion object {
        fun start(
            context: Context,
            type: Type,
            title: String,
            url: String? = null,
            mdSource: String? = null
        ) {
            val starter = Intent(context, WebActivity::class.java)
                .argument("title", title)
                .argument("type", type)
                .argument("url", url)
                .argument("mdSource", mdSource)
            context.startActivity(starter)
        }
    }
}