package com.tamara.sdk

object Tamara {
    private var configuredSdk: TamaraSdk? = null

    fun configure(
        config: TamaraConfig,
        backendGateway: TamaraBackendGateway,
        eventListener: TamaraEventListener = TamaraEventListener.NO_OP,
        widgetRules: TamaraWidgetEligibilityRules = TamaraWidgetEligibilityRules()
    ): TamaraResult<TamaraSdk> {
        return when (val validation = config.validate()) {
            is TamaraResult.Failure -> validation
            is TamaraResult.Success -> {
                val sdk = TamaraSdk(
                    config = validation.value,
                    backendGateway = backendGateway,
                    eventListener = eventListener,
                    widgetRules = widgetRules
                )
                configuredSdk = sdk
                sdk.publish(TamaraEvent.Configured(validation.value))
                TamaraResult.Success(sdk)
            }
        }
    }

    fun sdkOrNull(): TamaraSdk? = configuredSdk

    fun requireSdk(): TamaraResult<TamaraSdk> =
        configuredSdk?.let { TamaraResult.Success(it) } ?: TamaraResult.Failure(TamaraError.SdkNotConfigured)

    fun resetForTests() {
        configuredSdk = null
    }
}
