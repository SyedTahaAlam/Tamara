package com.tamara.sdk

sealed interface TamaraError {
    val message: String

    data class ConfigurationError(override val message: String) : TamaraError
    data class ValidationError(
        val field: String,
        override val message: String
    ) : TamaraError

    data class BackendError(
        val code: String,
        override val message: String
    ) : TamaraError

    data class NetworkError(
        override val message: String,
        val causeDescription: String? = null
    ) : TamaraError

    data class CheckoutError(override val message: String) : TamaraError
    data class SerializationError(override val message: String) : TamaraError
    data object SdkNotConfigured : TamaraError {
        override val message: String = "Tamara SDK is not configured. Call Tamara.configure first."
    }
}
