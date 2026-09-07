package com.tamara.sdk

class TamaraSdk(
    val config: TamaraConfig,
    private val backendGateway: TamaraBackendGateway,
    private val eventListener: TamaraEventListener = TamaraEventListener.NO_OP,
    private val widgetRules: TamaraWidgetEligibilityRules = TamaraWidgetEligibilityRules()
) {
    fun evaluateWidgetState(order: TamaraOrder): TamaraInstalmentWidgetState {
        val state = TamaraWidgetEligibilityEvaluator.evaluate(order, config, widgetRules)
        publish(TamaraEvent.WidgetEligibilityUpdated(state))
        return state
    }

    suspend fun createCheckoutSession(order: TamaraOrder): TamaraResult<TamaraCheckoutSession> {
        val result = backendGateway.createCheckoutSession(order, config)
        return result.onSuccess { publish(TamaraEvent.CheckoutSessionCreated(it)) }
            .onFailure { publish(TamaraEvent.ErrorEmitted(it)) }
    }

    suspend fun getCheckoutStatus(sessionId: String): TamaraResult<TamaraCheckoutStatus> {
        val result = backendGateway.getCheckoutStatus(sessionId, config)
        return result.onSuccess { publish(TamaraEvent.CheckoutStatusUpdated(it)) }
            .onFailure { publish(TamaraEvent.ErrorEmitted(it)) }
    }

    fun resolveRedirect(url: String, redirectUrls: TamaraCheckoutRedirectUrls): TamaraCheckoutRedirect {
        publish(TamaraEvent.CheckoutUrlNavigated(url))
        val redirect = TamaraUrlMatcher.matchCheckoutUrl(url, redirectUrls)
        publish(TamaraEvent.CheckoutRedirectMatched(redirect))
        return redirect
    }

    fun publish(event: TamaraEvent) {
        eventListener.onEvent(event)
    }
}
