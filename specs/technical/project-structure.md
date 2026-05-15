# Project Structure

This document defines the preferred project structure.

Exact Gradle module names may change depending on the selected Kotlin Multiplatform template, but the boundaries must remain consistent.

## Suggested High-Level Structure

```text
project-root/
├── androidApp/
├── iosApp/
├── shared/
├── specs/
│   ├── business/
│   ├── technical/
│   ├── design/
│   ├── quality/
│   └── tasks/
└── README.md
```

## Suggested Shared Structure

```text
shared/
├── src/
│   ├── commonMain/
│   │   └── kotlin/
│   │       └── app/
│   │           ├── core/
│   │           ├── domain/
│   │           ├── data/
│   │           ├── infrastructure/
│   │           └── features/
│   ├── commonTest/
│   ├── androidMain/
│   ├── androidUnitTest/
│   ├── iosMain/
│   └── iosTest/
```

## Suggested Logical Packages

```text
core/
├── result/
├── error/
├── logging/
├── dispatchers/
├── time/
└── validation/

domain/
├── model/
├── valueobject/
├── repository/
├── usecase/
├── service/
└── rules/

data/
├── repository/
├── datasource/
├── mock/
├── mapper/
└── model/

infrastructure/
├── storage/
├── network/
├── config/
├── security/
└── platform/

features/
├── auth/
├── dashboard/
├── area/
├── property/
├── calculator/
├── comparison/
└── watchlist/
```

## Feature Internal Structure

A feature may follow this internal structure:

```text
features/auth/
├── presentation/
│   ├── AuthViewModel.kt
│   ├── AuthUiState.kt
│   ├── AuthEvent.kt
│   └── AuthEffect.kt
├── domain/
│   └── feature-specific rules if needed
└── navigation/
```

Feature-specific domain code should only exist if it is truly feature-specific.

Shared business concepts should live in the main domain layer.

## Mock Data Location

Mock data must be placed under the data layer.

Suggested location:

```text
data/mock/
```

or feature-specific data location:

```text
features/area/data/mock/
```

Allowed only if the feature owns that mock data and does not expose it to UI directly.

## Naming Rule

Folder and package names should reflect responsibility, not implementation detail.

Prefer:

- `investment`
- `watchlist`
- `calculator`
- `area`
- `property`
- `auth`

Avoid vague names:

- `manager`
- `helper`
- `stuff`
- `common2`
- `utils` for business-specific logic

## Documentation Rule

When a new module or major package is created, update relevant specs if the structure changes.
