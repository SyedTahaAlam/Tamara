package com.tamara.sdk

import kotlin.test.Test
import kotlin.test.assertEquals

class TamaraWidgetEligibilityTest {
    @Test
    fun eligibleWhenOrderAmountAndCountryAreSupported() {
        val state = TamaraWidgetEligibilityEvaluator.evaluate(
            order = sampleOrder(amount = 400.0),
            config = TamaraConfig(publicKey = "pk_test", country = TamaraCountry.SA)
        )

        assertEquals(TamaraWidgetEligibility.Eligible, state.eligibility)
        assertEquals(100.0, state.instalmentAmount)
    }

    @Test
    fun ineligibleWhenAmountAboveMaximum() {
        val state = TamaraWidgetEligibilityEvaluator.evaluate(
            order = sampleOrder(amount = 20_000.0),
            config = TamaraConfig(publicKey = "pk_test", country = TamaraCountry.SA)
        )

        assertEquals(
            TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.AMOUNT_ABOVE_MAXIMUM),
            state.eligibility
        )
    }

    @Test
    fun ineligibleWhenCountryUnsupported() {
        val state = TamaraWidgetEligibilityEvaluator.evaluate(
            order = sampleOrder(amount = 400.0),
            config = TamaraConfig(publicKey = "pk_test", country = TamaraCountry.KW),
            rules = TamaraWidgetEligibilityRules(supportedCountries = setOf(TamaraCountry.SA, TamaraCountry.AE))
        )

        assertEquals(
            TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.UNSUPPORTED_COUNTRY),
            state.eligibility
        )
    }

    private fun sampleOrder(amount: Double) = TamaraOrder(
        orderReferenceId = "order-1",
        totalAmount = TamaraMoney(amount = amount, currency = "SAR"),
        items = listOf(
            TamaraOrderItem(
                referenceId = "item-1",
                name = "Sample Item",
                sku = "SKU-1",
                quantity = 1,
                totalAmount = TamaraMoney(amount = amount, currency = "SAR")
            )
        )
    )
}
