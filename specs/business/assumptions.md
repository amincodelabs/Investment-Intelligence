# Business Assumptions

This document defines current assumptions for the demo version.

## Market Assumptions

- The first demo focuses on Dubai/UAE real estate.
- Data may be mocked.
- Mock data should represent realistic property and area values.
- AED is the default currency.
- Area names should be realistic.
- Property prices and rents should be internally consistent.

## Product Assumptions

- The app is mobile-first.
- Android and iOS are both target platforms.
- The first version is a demo and does not require production backend integration.
- The product should be designed so backend integration can be added later.
- The product should focus on investor decision support, not brokerage workflows.

## User Assumptions

- Users understand basic property investment concepts.
- Some users may need simple explanations for metrics.
- Users want quick insight, not complex spreadsheets.
- Users may save properties and areas for later review.

## Authentication Assumptions

- Registration and login are included as product features.
- Authentication can be mocked locally in the first demo.
- Real authentication backend, password reset email, MFA, KYC, and identity verification are out of scope for the first demo.
- The app should still model session and user profile concepts cleanly.

## Calculation Assumptions

- Rental yield and net rental yield are important metrics.
- Service charges are treated as annual costs.
- Mortgage calculations are optional unless a task explicitly includes them.
- AI insights should be generated from available data and should not invent external facts.

## UX Assumptions

- The app should feel premium, calm, and analytical.
- Numbers should be easy to scan.
- Charts and cards may be used to simplify complex information.
- The user should not feel overwhelmed by too many metrics at once.
