# Changelog

## Unreleased

### Added
- New `:tamara-core` module with configuration API, injectable `TamaraSdk`, singleton `Tamara.configure`, result/error/event sealed types, serializable checkout/order models, URL matching, and widget eligibility logic.
- New `:tamara-ui` module with Compose UI for instalment widget and checkout flow.
- `expect/actual TamaraCheckoutView`:
  - Android: `WebView`
  - iOS: `WKWebView`
- Bottom-sheet wrapper API for checkout UI.
- Unit tests covering config validation, URL matching, eligibility rules, and model serialization.
- Integration docs and parity checklist.

### Changed
- Existing Compose Multiplatform sample app (`:shared`) now demonstrates Tamara widget + checkout flow via a mock backend gateway.

### Notes
- Because Tamara docs endpoints are unavailable in this runtime environment, backend requests are represented with `TamaraBackendGateway` abstraction and mock sample implementation; concrete server payload contracts are documented as assumptions.
