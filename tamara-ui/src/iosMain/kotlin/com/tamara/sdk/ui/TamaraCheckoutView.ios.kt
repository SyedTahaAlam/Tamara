@file:OptIn(ExperimentalForeignApi::class)

package com.tamara.sdk.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.tamara.sdk.TamaraError
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationActionPolicyAllow
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@Composable
actual fun TamaraCheckoutView(
    checkoutUrl: String,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onPageLoadingChanged: (Boolean) -> Unit,
    onError: (TamaraError) -> Unit
) {
    val navigationDelegate = remember(onUrlChanged, onPageLoadingChanged, onError) {
        TamaraNavigationDelegate(onUrlChanged, onPageLoadingChanged, onError)
    }

    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView(
                frame = CGRectZero.readValue(),
                configuration = WKWebViewConfiguration()
            ).apply {
                navigationDelegate = navigationDelegate
                loadCheckoutUrl(
                    checkoutUrl = checkoutUrl,
                    onLoadingStart = { onPageLoadingChanged(true) },
                    onError = onError
                )
            }
        },
        update = { webView ->
            if (webView.URL?.absoluteString != checkoutUrl) {
                webView.loadCheckoutUrl(
                    checkoutUrl = checkoutUrl,
                    onLoadingStart = { onPageLoadingChanged(true) },
                    onError = onError
                )
            }
        }
    )
}

private fun WKWebView.loadCheckoutUrl(
    checkoutUrl: String,
    onLoadingStart: () -> Unit,
    onError: (TamaraError) -> Unit
) {
    val url = NSURL(string = checkoutUrl)
    if (url == null) {
        onError(TamaraError.CheckoutError("Invalid checkout URL"))
        return
    }
    onLoadingStart()
    loadRequest(NSURLRequest.requestWithURL(url))
}

private class TamaraNavigationDelegate(
    private val onUrlChanged: (String) -> Unit,
    private val onPageLoadingChanged: (Boolean) -> Unit,
    private val onError: (TamaraError) -> Unit
) : NSObject(), WKNavigationDelegateProtocol {

    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        decidePolicyForNavigationAction.request.URL?.absoluteString?.let(onUrlChanged)
        decisionHandler(WKNavigationActionPolicyAllow)
    }

    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        onPageLoadingChanged(true)
        webView.URL?.absoluteString?.let(onUrlChanged)
    }

    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        onPageLoadingChanged(false)
        webView.URL?.absoluteString?.let(onUrlChanged)
    }

    override fun webView(
        webView: WKWebView,
        didFailNavigation: WKNavigation?,
        withError: NSError
    ) {
        onPageLoadingChanged(false)
        onError(TamaraError.CheckoutError(withError.localizedDescription))
    }

    override fun webView(
        webView: WKWebView,
        didFailProvisionalNavigation: WKNavigation?,
        withError: NSError
    ) {
        onPageLoadingChanged(false)
        onError(TamaraError.CheckoutError(withError.localizedDescription))
    }
}
