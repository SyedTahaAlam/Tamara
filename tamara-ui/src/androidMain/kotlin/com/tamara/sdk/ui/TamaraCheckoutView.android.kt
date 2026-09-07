package com.tamara.sdk.ui

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tamara.sdk.TamaraError

@Composable
actual fun TamaraCheckoutView(
    checkoutUrl: String,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onPageLoadingChanged: (Boolean) -> Unit,
    onError: (TamaraError) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        request?.url?.toString()?.let(onUrlChanged)
                        return false
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onPageLoadingChanged(true)
                        url?.let(onUrlChanged)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onPageLoadingChanged(false)
                        url?.let(onUrlChanged)
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        onPageLoadingChanged(false)
                        onError(
                            TamaraError.CheckoutError(
                                error?.description?.toString() ?: "Failed to load checkout page"
                            )
                        )
                    }
                }
                loadUrl(checkoutUrl)
            }
        },
        update = { webView ->
            if (webView.url != checkoutUrl) {
                webView.loadUrl(checkoutUrl)
            }
        }
    )
}
