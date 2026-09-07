# Tamara KMP SDK Integration Guide

## 1) Add modules

This repository exposes two SDK modules:

- `:tamara-core`
- `:tamara-ui`

Use `:tamara-core` for config/contracts/model logic and `:tamara-ui` for widget/checkout UI.

## 2) Configure the SDK

```kotlin
val configResult = Tamara.configure(
    config = TamaraConfig(
        publicKey = "pk_test_xxx",
        environment = TamaraEnvironment.SANDBOX,
        country = TamaraCountry.SA,
        locale = TamaraLocale.EN
    ),
    backendGateway = merchantGateway,
    eventListener = TamaraEventListener { event ->
        // analytics/logging hook
    }
)
```

### Injection option

If you prefer DI over global singleton usage, instantiate and pass `TamaraSdk` directly:

```kotlin
val sdk = TamaraSdk(config, backendGateway, eventListener)
```

All UI APIs accept an optional `sdk` parameter.

## 3) Implement merchant backend gateway

Implement `TamaraBackendGateway` in your app/backend client layer:

- `createCheckoutSession(order, config)`
- `getCheckoutStatus(sessionId, config)`

Return `TamaraResult.Success(...)` or `TamaraResult.Failure(...)`.

> Assumption: Real Tamara endpoint contracts are not hardcoded here due unavailable docs site in this environment.

## 4) Render instalment widget

```kotlin
TamaraInstalmentWidget(order = order)
```

Eligibility is computed from:
- configured country
- configurable min/max amount rules

## 5) Launch checkout UI

```kotlin
TamaraCheckoutBottomSheet(
    visible = showCheckout,
    onDismissRequest = { showCheckout = false },
    session = session,
    redirectUrls = TamaraCheckoutRedirectUrls(
        successUrl = "https://merchant/success",
        cancelUrl = "https://merchant/cancel",
        failureUrl = "https://merchant/failure"
    ),
    onRedirect = { redirect ->
        // Success / Cancel / Failure / Unknown
    }
)
```

### Platform web rendering
- Android: `WebView`
- iOS: `WKWebView`

## 6) Handle callbacks and status polling

- URL callbacks are matched by `TamaraUrlMatcher`.
- Redirect events can trigger backend status checks using `getCheckoutStatus`.

## 7) Error handling

Use `TamaraResult` and `TamaraError` exhaustively in `when` branches.

## 8) Testing recommendations

Included tests show coverage for:
- config validation
- URL matcher behavior
- widget eligibility
- serialization round trips
