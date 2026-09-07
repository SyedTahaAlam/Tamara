package com.tamara.sdk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TamaraMoney(
    val amount: Double,
    val currency: String
)

@Serializable
data class TamaraOrderItem(
    val referenceId: String,
    val name: String,
    val sku: String,
    val quantity: Int,
    val totalAmount: TamaraMoney
)

@Serializable
data class TamaraOrder(
    val orderReferenceId: String,
    val totalAmount: TamaraMoney,
    val items: List<TamaraOrderItem>,
    val shippingAmount: TamaraMoney? = null,
    val taxAmount: TamaraMoney? = null,
    val discountAmount: TamaraMoney? = null,
    val consumerEmail: String? = null,
    val description: String? = null
)

@Serializable
enum class TamaraSessionStatus {
    @SerialName("new") NEW,
    @SerialName("approved") APPROVED,
    @SerialName("declined") DECLINED,
    @SerialName("cancelled") CANCELLED,
    @SerialName("expired") EXPIRED,
    @SerialName("pending") PENDING,
    @SerialName("unknown") UNKNOWN
}

@Serializable
data class TamaraCheckoutSession(
    val sessionId: String,
    val checkoutUrl: String,
    val status: TamaraSessionStatus = TamaraSessionStatus.NEW
)

@Serializable
data class TamaraCheckoutStatus(
    val sessionId: String,
    val status: TamaraSessionStatus,
    val rawStatus: String? = null,
    val updatedAtIso8601: String? = null
)

@Serializable
data class TamaraCheckoutRedirectUrls(
    val successUrl: String,
    val cancelUrl: String,
    val failureUrl: String? = null
)
