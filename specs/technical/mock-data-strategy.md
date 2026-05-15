# Mock Data Strategy

## Goal

The first demo may use mock data, but mock data must be isolated so future backend integration causes minimal breaking changes.

## Location Rule

Mock data must live only in the data layer.

Allowed locations:

```text
data/mock/
```

or feature-owned data package:

```text
features/[feature]/data/mock/
```

Not allowed:

- UI components
- ViewModels
- Domain services
- Use cases
- Design system

## Repository Rule

UI and domain should access data through repository contracts.

Repository implementation can use mock data source internally.

## Realistic Data Rule

Mock data must be realistic and internally consistent.

Example:

If:

- Property price = 1,000,000 AED
- Annual rent = 80,000 AED

Then:

- Gross rental yield should be 8%

Do not use random metrics that contradict formulas.

## Replaceability Rule

Future backend integration should mainly require replacing:

- Mock data source
- Repository implementation
- DTOs
- Mappers
- Dependency setup

It should not require rewriting UI state or domain calculations.

## AI Agent Rule

When creating mock data, the agent must keep it in the data layer and must not duplicate business formulas in mock objects unless values are explicitly precomputed for demo display.
