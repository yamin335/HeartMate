package com.tovalue.me.ui.browser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import androidx.core.view.isVisible
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.BingoUiActivityVisitLinksBinding

class VisitLinksActivity : AppCompatActivity() {
	private lateinit var _binding: BingoUiActivityVisitLinksBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		_binding = BingoUiActivityVisitLinksBinding.inflate(layoutInflater)
		setContentView(_binding.root)
		configUIs()
		val url = intent.getStringExtra("url")
		if (url.isNullOrEmpty()) {
			showToast("URL is missing.")
			return
		}
		loadUrlToWeb(url)
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private fun configUIs() {
		// WebView
		_binding.webView.apply {
			settings.apply {
				javaScriptEnabled = true
				loadWithOverviewMode = true
				useWideViewPort = true
				builtInZoomControls = true
				pluginState = WebSettings.PluginState.ON
			}
			webViewClient = AppWebViewClients()
		}
		_binding.toolbar.setNavigationOnClickListener{
			finish()
		}
	}
	
	private fun loadUrlToWeb(url: String?) {
		_binding.webView.isVisible = true
		_binding.progressBar.isVisible = true
		
		url?.let {
			_binding.webView.loadUrl(it)
		}
	}
	
	private inner class AppWebViewClients : WebViewClient() {
		@Deprecated("Deprecated in Java")
		override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
			view.loadUrl(url)
			return true
		}
		
		override fun onPageFinished(view: WebView, url: String) {
			super.onPageFinished(view, url)
			_binding.progressBar.isVisible = false
		}
		
		override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError?) {
			if (error == null) {
				return
			}
			showToast(error.toString())
		}
	}
	
	override fun onBackPressed() {
		super.onBackPressed()
		finish()
	}
}