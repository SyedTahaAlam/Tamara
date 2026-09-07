# Tamara KMP SDK (Pragmatic Reference Implementation)

This repository now contains a Kotlin Multiplatform Tamara SDK prototype with two new modules:

- `:tamara-core` – core API, models, config, gateway contracts, result/error/event types, URL matching, widget eligibility.
- `:tamara-ui` – Compose Multiplatform UI (`TamaraInstalmentWidget`, checkout screen, bottom-sheet wrapper, and platform `TamaraCheckoutView` expect/actual).

The existing sample app (`:shared` + `:androidApp` + `iosApp`) is retained and wired to demonstrate SDK usage.

## Implemented Public API (current scope)

### Core
- `TamaraEnvironment`, `TamaraCountry`, `TamaraLocale`
- `TamaraConfig`
- `TamaraSdk` (injectable instance)
- `Tamara.configure(...)` singleton entry point
- `TamaraResult` and `TamaraError` sealed types
- Serializable models:
  - `TamaraOrder`, `TamaraOrderItem`, `TamaraMoney`
  - `TamaraCheckoutSession`, `TamaraCheckoutStatus`, `TamaraSessionStatus`
  - `TamaraCheckoutRedirectUrls`
- `TamaraBackendGateway` interface for merchant backend integration
- `TamaraEventListener` + `TamaraEvent` sealed events
- `TamaraUrlMatcher`
- Widget eligibility evaluator/state:
  - `TamaraWidgetEligibilityEvaluator`
  - `TamaraInstalmentWidgetState`

### UI
- `TamaraInstalmentWidget(...)`
- `TamaraCheckoutScreen(...)`
- `TamaraCheckoutBottomSheet(...)`
- `expect/actual TamaraCheckoutView(...)`
  - Android actual uses `WebView`
  - iOS actual uses `WKWebView`

## Sample App Demo

The sample configures Tamara with a mock `SampleTamaraBackendGateway`, renders widget eligibility, and launches checkout in a bottom sheet.

> **Assumption note:** The Tamara docs site is unavailable in this environment, so backend/network calls are intentionally modeled via `TamaraBackendGateway` and a mock implementation. No real Tamara API call payloads were invented.

## Quick Start (SDK usage)

```kotlin
val result = Tamara.configure(
    config = TamaraConfig(
        publicKey = "pk_test_xxx",
        environment = TamaraEnvironment.SANDBOX,
        country = TamaraCountry.SA,
        locale = TamaraLocale.EN
    ),
    backendGateway = myBackendGateway,
    eventListener = TamaraEventListener { event -> println(event) }
)
```

Then render:

```kotlin
TamaraInstalmentWidget(order = order)
```

And launch checkout with a created `TamaraCheckoutSession`:

```kotlin
TamaraCheckoutBottomSheet(
    visible = true,
    onDismissRequest = {},
    session = session,
    redirectUrls = TamaraCheckoutRedirectUrls(
        successUrl = "https://merchant/success",
        cancelUrl = "https://merchant/cancel",
        failureUrl = "https://merchant/failure"
    )
)
```

## Docs

- Integration guide: `docs/INTEGRATION_GUIDE.md`
- SDK parity checklist: `docs/PARITY_CHECKLIST.md`
- Changelog: `CHANGELOG.md`
