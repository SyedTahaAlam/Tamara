package com.tamara.sdk

import kotlin.test.Test
import kotlin.test.assertEquals

class TamaraUrlMatcherTest {
    private val redirects = TamaraCheckoutRedirectUrls(
        successUrl = "https://merchant.example/checkout/success",
        cancelUrl = "https://merchant.example/checkout/cancel",
        failureUrl = "https://merchant.example/checkout/failure"
    )

    @Test
    fun matchesSuccessUrlWhenQueryParamsExist() {
        val result = TamaraUrlMatcher.matchCheckoutUrl(
            navigatedUrl = "https://merchant.example/checkout/success?order_id=123",
            redirectUrls = redirects
        )

        assertEquals(TamaraCheckoutRedirect.Success, result)
    }

    @Test
    fun matchesCancelUrlIgnoringTrailingSlash() {
        val result = TamaraUrlMatcher.matchCheckoutUrl(
            navigatedUrl = "https://merchant.example/checkout/cancel/",
            redirectUrls = redirects
        )

        assertEquals(TamaraCheckoutRedirect.Cancel, result)
    }

    @Test
    fun returnsUnknownWhenNoConfiguredUrlMatches() {
        val result = TamaraUrlMatcher.matchCheckoutUrl(
            navigatedUrl = "https://merchant.example/checkout/in-progress",
            redirectUrls = redirects
        )

        assertEquals(TamaraCheckoutRedirect.Unknown, result)
    }
}
