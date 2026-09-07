# Tamara KMP SDK Parity Checklist

Legend: ✅ implemented, ⚠️ partial/assumption-based

## Core API
- ✅ Config models (`TamaraConfig`, environment/country/locale)
- ✅ SDK instance injection (`TamaraSdk` constructor)
- ✅ Global configuration entry point (`Tamara.configure`)
- ✅ Sealed result and error models (`TamaraResult`, `TamaraError`)
- ✅ Serializable order/session/status models
- ✅ Backend contract abstraction (`TamaraBackendGateway`)
- ✅ Event listener/event stream model (`TamaraEventListener`, `TamaraEvent`)
- ✅ Redirect URL matching (`TamaraUrlMatcher`)

## Widget
- ✅ Eligibility rules + evaluator + state model
- ✅ `TamaraInstalmentWidget` Compose API

## Checkout
- ✅ Common checkout composable API
- ✅ `expect/actual TamaraCheckoutView`
  - ✅ Android `WebView`
  - ✅ iOS `WKWebView`
- ✅ Bottom sheet checkout wrapper API

## Sample App
- ✅ Existing sample retained and wired to SDK demo flow
- ✅ Mock backend gateway for demonstrable checkout/session behavior

## Tests
- ✅ Config validation tests
- ✅ URL matching tests
- ✅ Widget eligibility tests
- ✅ Serialization round-trip tests

## Assumptions / Non-finalized pieces
- ⚠️ Real Tamara endpoint payloads and server contracts are represented through `TamaraBackendGateway`; exact network API shape intentionally not invented without docs access.
- ⚠️ iOS web checkout implementation is pragmatic and focused on practical WKWebView integration + navigation callbacks for URL matching.
