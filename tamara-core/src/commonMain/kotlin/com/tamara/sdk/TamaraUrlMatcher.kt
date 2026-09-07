package com.tamara.sdk

sealed interface TamaraCheckoutRedirect {
    data object Success : TamaraCheckoutRedirect
    data object Cancel : TamaraCheckoutRedirect
    data object Failure : TamaraCheckoutRedirect
    data object Unknown : TamaraCheckoutRedirect
}

object TamaraUrlMatcher {
    fun matchCheckoutUrl(
        navigatedUrl: String,
        redirectUrls: TamaraCheckoutRedirectUrls
    ): TamaraCheckoutRedirect {
        return when {
            matches(navigatedUrl, redirectUrls.successUrl) -> TamaraCheckoutRedirect.Success
            matches(navigatedUrl, redirectUrls.cancelUrl) -> TamaraCheckoutRedirect.Cancel
            redirectUrls.failureUrl != null && matches(navigatedUrl, redirectUrls.failureUrl) -> TamaraCheckoutRedirect.Failure
            else -> TamaraCheckoutRedirect.Unknown
        }
    }

    private fun matches(navigatedUrl: String, expectedUrl: String): Boolean {
        val normalizedNavigated = normalize(navigatedUrl)
        val normalizedExpected = normalize(expectedUrl)
        return normalizedNavigated == normalizedExpected || normalizedNavigated.startsWith("$normalizedExpected/")
    }

    private fun normalize(value: String): String {
        val noFragment = value.substringBefore('#')
        val noQuery = noFragment.substringBefore('?')
        val trimmed = noQuery.trim()
        val noTrailingSlash = if (trimmed.endsWith('/')) trimmed.dropLast(1) else trimmed
        return noTrailingSlash.lowercase()
    }
}
