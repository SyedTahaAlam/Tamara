package com.example.tamarasdk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tamara.sdk.Tamara
import com.tamara.sdk.TamaraCheckoutRedirect
import com.tamara.sdk.TamaraCheckoutRedirectUrls
import com.tamara.sdk.TamaraCheckoutSession
import com.tamara.sdk.TamaraConfig
import com.tamara.sdk.TamaraCountry
import com.tamara.sdk.TamaraEnvironment
import com.tamara.sdk.TamaraEvent
import com.tamara.sdk.TamaraEventListener
import com.tamara.sdk.TamaraLocale
import com.tamara.sdk.TamaraResult
import com.tamara.sdk.onFailure
import com.tamara.sdk.ui.TamaraCheckoutBottomSheet
import com.tamara.sdk.ui.TamaraInstalmentWidget
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        val order = remember { sampleOrder() }
        val gateway = remember { SampleTamaraBackendGateway() }
        val redirectUrls = remember {
            TamaraCheckoutRedirectUrls(
                successUrl = "https://merchant.example/tamara/success",
                cancelUrl = "https://merchant.example/tamara/cancel",
                failureUrl = "https://merchant.example/tamara/failure"
            )
        }

        var latestEvent by remember { mutableStateOf("Not configured") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var checkoutSession by remember { mutableStateOf<TamaraCheckoutSession?>(null) }
        var showCheckout by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            Tamara.configure(
                config = TamaraConfig(
                    publicKey = "pk_test_demo_key",
                    environment = TamaraEnvironment.SANDBOX,
                    country = TamaraCountry.SA,
                    locale = TamaraLocale.EN
                ),
                backendGateway = gateway,
                eventListener = TamaraEventListener { event ->
                    latestEvent = event.toHumanReadableText()
                }
            ).onFailure {
                errorMessage = it.message
            }
        }

        val sdk = Tamara.sdkOrNull()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Tamara KMP SDK Demo", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Latest event: $latestEvent", style = MaterialTheme.typography.bodyMedium)

            TamaraInstalmentWidget(
                modifier = Modifier.fillMaxWidth(),
                order = order,
                sdk = sdk
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        val activeSdk = sdk ?: return@launch
                        when (val sessionResult = activeSdk.createCheckoutSession(order)) {
                            is TamaraResult.Success -> {
                                checkoutSession = sessionResult.value
                                showCheckout = true
                            }

                            is TamaraResult.Failure -> {
                                errorMessage = sessionResult.error.message
                            }
                        }
                    }
                },
                enabled = sdk != null
            ) {
                Text("Launch Checkout")
            }

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }

        checkoutSession?.let { session ->
            TamaraCheckoutBottomSheet(
                visible = showCheckout,
                onDismissRequest = { showCheckout = false },
                session = session,
                redirectUrls = redirectUrls,
                sdk = sdk,
                onRedirect = { redirect ->
                    latestEvent = "Checkout redirect: $redirect"
                    if (redirect != TamaraCheckoutRedirect.Unknown) {
                        showCheckout = false
                    }
                },
                onError = { error -> errorMessage = error.message }
            )
        }
    }
}

private fun TamaraEvent.toHumanReadableText(): String = when (this) {
    is TamaraEvent.Configured -> "Configured for ${config.environment} (${config.country}, ${config.locale})"
    is TamaraEvent.WidgetEligibilityUpdated -> "Widget eligibility: ${state.eligibility}"
    is TamaraEvent.CheckoutSessionCreated -> "Checkout session created: ${session.sessionId}"
    is TamaraEvent.CheckoutStatusUpdated -> "Checkout status: ${status.status}"
    is TamaraEvent.CheckoutUrlNavigated -> "Checkout opened URL: $url"
    is TamaraEvent.CheckoutRedirectMatched -> "Checkout redirect detected: $redirect"
    is TamaraEvent.ErrorEmitted -> "Error: ${error.message}"
}
