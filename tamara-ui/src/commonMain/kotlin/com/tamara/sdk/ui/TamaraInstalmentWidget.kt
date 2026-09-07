package com.tamara.sdk.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamara.sdk.Tamara
import com.tamara.sdk.TamaraOrder
import com.tamara.sdk.TamaraSdk
import com.tamara.sdk.TamaraWidgetEligibility
import com.tamara.sdk.TamaraWidgetIneligibilityReason
import kotlin.math.roundToInt

@Composable
fun TamaraInstalmentWidget(
    order: TamaraOrder,
    modifier: Modifier = Modifier,
    sdk: TamaraSdk? = Tamara.sdkOrNull(),
    title: String = "Pay with Tamara"
) {
    val activeSdk = sdk
    if (activeSdk == null) {
        Card(modifier = modifier.fillMaxWidth()) {
            Text(
                text = "Tamara is not configured",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    val widgetState = activeSdk.evaluateWidgetState(order)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            when (val eligibility = widgetState.eligibility) {
                TamaraWidgetEligibility.Eligible -> {
                    Text(
                        text = "4 payments of ${formatAmount(widgetState.instalmentAmount)} ${order.totalAmount.currency}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is TamaraWidgetEligibility.Ineligible -> {
                    Text(
                        text = ineligibilityMessage(eligibility.reason),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun formatAmount(value: Double): String =
    ((value * 100).roundToInt() / 100.0).toString()

private fun ineligibilityMessage(reason: TamaraWidgetIneligibilityReason): String = when (reason) {
    TamaraWidgetIneligibilityReason.UNSUPPORTED_COUNTRY -> "Tamara is not available for this country"
    TamaraWidgetIneligibilityReason.AMOUNT_BELOW_MINIMUM -> "Order amount is below Tamara minimum"
    TamaraWidgetIneligibilityReason.AMOUNT_ABOVE_MAXIMUM -> "Order amount is above Tamara maximum"
    TamaraWidgetIneligibilityReason.INVALID_AMOUNT -> "Order amount is invalid"
}
