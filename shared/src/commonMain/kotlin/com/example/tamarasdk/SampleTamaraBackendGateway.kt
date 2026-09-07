package com.example.tamarasdk

import com.tamara.sdk.TamaraBackendGateway
import com.tamara.sdk.TamaraCheckoutSession
import com.tamara.sdk.TamaraCheckoutStatus
import com.tamara.sdk.TamaraConfig
import com.tamara.sdk.TamaraResult
import com.tamara.sdk.TamaraSessionStatus

class SampleTamaraBackendGateway : TamaraBackendGateway {
    override suspend fun createCheckoutSession(
        order: com.tamara.sdk.TamaraOrder,
        config: TamaraConfig
    ): TamaraResult<TamaraCheckoutSession> {
        val checkoutUrl =
            "https://api-sandbox.tamara.co/mock-checkout?orderId=${order.orderReferenceId}&country=${config.country.code}"
        return TamaraResult.Success(
            TamaraCheckoutSession(
                sessionId = "session-${order.orderReferenceId}",
                checkoutUrl = checkoutUrl,
                status = TamaraSessionStatus.PENDING
            )
        )
    }

    override suspend fun getCheckoutStatus(
        sessionId: String,
        config: TamaraConfig
    ): TamaraResult<TamaraCheckoutStatus> {
        return TamaraResult.Success(
            TamaraCheckoutStatus(
                sessionId = sessionId,
                status = TamaraSessionStatus.PENDING,
                rawStatus = "pending"
            )
        )
    }
}
