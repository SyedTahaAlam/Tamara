package com.tamara.sdk

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TamaraConfigTest {
    @Test
    fun blankPublicKeyReturnsConfigurationFailure() {
        val result = TamaraConfig(publicKey = " ").validate()

        val failure = assertIs<TamaraResult.Failure>(result)
        val error = assertIs<TamaraError.ConfigurationError>(failure.error)
        assertEquals("publicKey must not be blank", error.message)
    }

    @Test
    fun validConfigReturnsSuccess() {
        val config = TamaraConfig(
            publicKey = "pk_test_demo",
            environment = TamaraEnvironment.SANDBOX,
            country = TamaraCountry.AE,
            locale = TamaraLocale.AR
        )

        val result = config.validate()
        val success = assertIs<TamaraResult.Success<TamaraConfig>>(result)
        assertEquals(config, success.value)
    }
}
