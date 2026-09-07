package com.tamara.sdk

fun interface TamaraEventListener {
    fun onEvent(event: TamaraEvent)

    companion object {
        val NO_OP: TamaraEventListener = TamaraEventListener { }
    }
}

sealed interface TamaraEvent {
    data class Configured(val config: TamaraConfig) : TamaraEvent
    data class WidgetEligibilityUpdated(val state: TamaraInstalmentWidgetState) : TamaraEvent
    data class CheckoutSessionCreated(val session: TamaraCheckoutSession) : TamaraEvent
    data class CheckoutStatusUpdated(val status: TamaraCheckoutStatus) : TamaraEvent
    data class CheckoutUrlNavigated(val url: String) : TamaraEvent
    data class CheckoutRedirectMatched(val redirect: TamaraCheckoutRedirect) : TamaraEvent
    data class ErrorEmitted(val error: TamaraError) : TamaraEvent
}
