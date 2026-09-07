package com.tamara.sdk

interface TamaraBackendGateway {
    suspend fun createCheckoutSession(
        order: TamaraOrder,
        config: TamaraConfig
    ): TamaraResult<TamaraCheckoutSession>

    suspend fun getCheckoutStatus(
        sessionId: String,
        config: TamaraConfig
    ): TamaraResult<TamaraCheckoutStatus>
}
