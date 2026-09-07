package com.tamara.sdk

data class TamaraWidgetEligibilityRules(
    val minAmount: Double = 99.0,
    val maxAmount: Double = 9_999.0,
    val supportedCountries: Set<TamaraCountry> = setOf(TamaraCountry.SA, TamaraCountry.AE, TamaraCountry.KW)
)

enum class TamaraWidgetIneligibilityReason {
    UNSUPPORTED_COUNTRY,
    AMOUNT_BELOW_MINIMUM,
    AMOUNT_ABOVE_MAXIMUM,
    INVALID_AMOUNT
}

sealed interface TamaraWidgetEligibility {
    data object Eligible : TamaraWidgetEligibility
    data class Ineligible(val reason: TamaraWidgetIneligibilityReason) : TamaraWidgetEligibility
}

data class TamaraInstalmentWidgetState(
    val eligibility: TamaraWidgetEligibility,
    val order: TamaraOrder,
    val instalmentsCount: Int = 4
) {
    val instalmentAmount: Double = if (instalmentsCount > 0) {
        order.totalAmount.amount / instalmentsCount
    } else {
        order.totalAmount.amount
    }
}

object TamaraWidgetEligibilityEvaluator {
    fun evaluate(
        order: TamaraOrder,
        config: TamaraConfig,
        rules: TamaraWidgetEligibilityRules = TamaraWidgetEligibilityRules()
    ): TamaraInstalmentWidgetState {
        val amount = order.totalAmount.amount
        val eligibility = when {
            config.country !in rules.supportedCountries -> {
                TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.UNSUPPORTED_COUNTRY)
            }

            amount.isNaN() || amount <= 0.0 -> {
                TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.INVALID_AMOUNT)
            }

            amount < rules.minAmount -> {
                TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.AMOUNT_BELOW_MINIMUM)
            }

            amount > rules.maxAmount -> {
                TamaraWidgetEligibility.Ineligible(TamaraWidgetIneligibilityReason.AMOUNT_ABOVE_MAXIMUM)
            }

            else -> TamaraWidgetEligibility.Eligible
        }

        return TamaraInstalmentWidgetState(
            eligibility = eligibility,
            order = order
        )
    }
}
