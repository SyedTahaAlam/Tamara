package com.tamara.sdk

enum class TamaraEnvironment(val baseUrl: String) {
    SANDBOX("https://api-sandbox.tamara.co"),
    PRODUCTION("https://api.tamara.co")
}

enum class TamaraCountry(val code: String) {
    SA("SA"),
    AE("AE"),
    KW("KW"),
    BH("BH"),
    QA("QA")
}

enum class TamaraLocale(val languageTag: String) {
    EN("en"),
    AR("ar")
}

data class TamaraConfig(
    val publicKey: String,
    val environment: TamaraEnvironment = TamaraEnvironment.SANDBOX,
    val country: TamaraCountry = TamaraCountry.SA,
    val locale: TamaraLocale = TamaraLocale.EN,
    val merchantUrlScheme: String? = null
) {
    fun validate(): TamaraResult<TamaraConfig> {
        if (publicKey.isBlank()) {
            return TamaraResult.Failure(TamaraError.ConfigurationError("publicKey must not be blank"))
        }
        return TamaraResult.Success(this)
    }
}
