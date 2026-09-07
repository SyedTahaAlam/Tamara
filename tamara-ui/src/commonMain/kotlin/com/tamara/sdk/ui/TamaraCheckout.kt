package com.tamara.sdk.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamara.sdk.Tamara
import com.tamara.sdk.TamaraCheckoutRedirect
import com.tamara.sdk.TamaraCheckoutRedirectUrls
import com.tamara.sdk.TamaraCheckoutSession
import com.tamara.sdk.TamaraError
import com.tamara.sdk.TamaraSdk

@Composable
fun TamaraCheckoutScreen(
    session: TamaraCheckoutSession,
    redirectUrls: TamaraCheckoutRedirectUrls,
    modifier: Modifier = Modifier,
    sdk: TamaraSdk? = Tamara.sdkOrNull(),
    onRedirect: (TamaraCheckoutRedirect) -> Unit = {},
    onError: (TamaraError) -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        TamaraCheckoutView(
            checkoutUrl = session.checkoutUrl,
            modifier = Modifier.fillMaxSize(),
            onUrlChanged = { url ->
                val redirect = (sdk?.resolveRedirect(url, redirectUrls)
                    ?: com.tamara.sdk.TamaraUrlMatcher.matchCheckoutUrl(url, redirectUrls))
                if (redirect != TamaraCheckoutRedirect.Unknown) {
                    onRedirect(redirect)
                }
            },
            onPageLoadingChanged = { isLoading = it },
            onError = onError
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TamaraCheckoutBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    session: TamaraCheckoutSession,
    redirectUrls: TamaraCheckoutRedirectUrls,
    modifier: Modifier = Modifier,
    sdk: TamaraSdk? = Tamara.sdkOrNull(),
    onRedirect: (TamaraCheckoutRedirect) -> Unit = {},
    onError: (TamaraError) -> Unit = {}
) {
    if (!visible) return

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = "Secure Tamara Checkout",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TamaraCheckoutScreen(
            session = session,
            redirectUrls = redirectUrls,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            sdk = sdk,
            onRedirect = onRedirect,
            onError = onError
        )
    }
}

@Composable
expect fun TamaraCheckoutView(
    checkoutUrl: String,
    modifier: Modifier = Modifier,
    onUrlChanged: (String) -> Unit,
    onPageLoadingChanged: (Boolean) -> Unit,
    onError: (TamaraError) -> Unit
)
