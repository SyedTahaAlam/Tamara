package com.example.tamarasdk

import com.tamara.sdk.TamaraMoney
import com.tamara.sdk.TamaraOrder
import com.tamara.sdk.TamaraOrderItem

fun sampleOrder(): TamaraOrder = TamaraOrder(
    orderReferenceId = "demo-order-1001",
    totalAmount = TamaraMoney(amount = 420.0, currency = "SAR"),
    items = listOf(
        TamaraOrderItem(
            referenceId = "line-1",
            name = "Running Shoes",
            sku = "RUN-SHOE-1",
            quantity = 1,
            totalAmount = TamaraMoney(amount = 320.0, currency = "SAR")
        ),
        TamaraOrderItem(
            referenceId = "line-2",
            name = "Socks",
            sku = "SOCK-1",
            quantity = 2,
            totalAmount = TamaraMoney(amount = 100.0, currency = "SAR")
        )
    ),
    shippingAmount = TamaraMoney(amount = 0.0, currency = "SAR"),
    taxAmount = TamaraMoney(amount = 0.0, currency = "SAR")
)
