package com.tamara.sdk

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class TamaraSerializationTest {
    private val json = Json {
        prettyPrint = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Test
    fun orderRoundTripSerialization() {
        val order = TamaraOrder(
            orderReferenceId = "order-123",
            totalAmount = TamaraMoney(amount = 350.0, currency = "SAR"),
            items = listOf(
                TamaraOrderItem(
                    referenceId = "item-1",
                    name = "Shoes",
                    sku = "SKU-SHOES",
                    quantity = 1,
                    totalAmount = TamaraMoney(amount = 300.0, currency = "SAR")
                )
            ),
            shippingAmount = TamaraMoney(amount = 50.0, currency = "SAR")
        )

        val encoded = json.encodeToString(order)
        val decoded = json.decodeFromString<TamaraOrder>(encoded)

        assertEquals(order, decoded)
    }

    @Test
    fun checkoutSessionRoundTripSerialization() {
        val session = TamaraCheckoutSession(
            sessionId = "session-abc",
            checkoutUrl = "https://checkout.example/session-abc",
            status = TamaraSessionStatus.PENDING
        )

        val encoded = json.encodeToString(session)
        val decoded = json.decodeFromString<TamaraCheckoutSession>(encoded)

        assertEquals(session, decoded)
    }

    @Test
    fun checkoutStatusRoundTripSerialization() {
        val status = TamaraCheckoutStatus(
            sessionId = "session-abc",
            status = TamaraSessionStatus.APPROVED,
            rawStatus = "approved",
            updatedAtIso8601 = "2026-09-07T12:00:00Z"
        )

        val encoded = json.encodeToString(status)
        val decoded = json.decodeFromString<TamaraCheckoutStatus>(encoded)

        assertEquals(status, decoded)
    }
}
