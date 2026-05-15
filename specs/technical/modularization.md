# Modularization

## Goal

The project must be modular enough to keep technical responsibilities separated and reusable.

Modularization in this project means separating major technical capabilities and feature boundaries, not necessarily creating one Gradle module per Clean Architecture layer.

## Important Decision

Domain, Data, and UI do not need to be separate modules by default.

They are logical layers.

The project should avoid unnecessary module explosion.

Create modules where separation provides real value.

## Recommended Module Types

The project may include modules such as:

- app-android
- app-ios
- shared
- core
- networking
- storage
- logging
- design-system
- navigation
- testing
- feature-dashboard
- feature-auth
- feature-area-intelligence
- feature-property-details
- feature-investment-calculator
- feature-comparison
- feature-watchlist

Exact module names may be adjusted based on the actual KMP project template.

## Shared Module

The shared module may contain:

- Domain models
- Value objects
- Repository contracts
- Use cases
- Business calculation logic
- State models
- Shared ViewModels if appropriate
- Repository implementations if they are platform-independent
- Mock data sources
- Mappers
- Common utilities

## Core Module

The core module may contain shared foundation code such as:

- Result types
- Error models
- Base state/effect contracts
- Dispatchers abstraction
- Date/time abstractions
- Common extensions
- Common validation helpers

Core must not depend on feature modules.

## Infrastructure Modules

Infrastructure modules should represent reusable technical capabilities.

Examples:

### networking

Responsible for:

- HTTP client setup
- Request/response handling
- Network error mapping
- Network logging rules
- API client abstractions

### storage

Responsible for:

- Local persistence abstractions
- Key-value storage
- Database setup if added later
- Cache utilities
- Secure storage abstractions if needed

### logging

Responsible for:

- Logger abstraction
- Platform-specific logger implementation
- Log level configuration
- Structured logging helpers

### design-system

Responsible for:

- Reusable UI components
- Theme tokens
- Typography
- Spacing
- Colors
- Common UI states
- Shared visual rules

The design-system module must not depend on business feature modules.

## Feature Modules

Feature modules may be created for meaningful feature boundaries.

Suggested features:

- Authentication
- Dashboard
- Area Intelligence
- Property Details
- Investment Calculator
- Property Comparison
- Watchlist
- Market Trends
- AI Insights

Feature modules should be independent where possible.

Avoid direct feature-to-feature coupling unless explicitly needed.

If features need to share concepts, move shared contracts/models to shared/core/domain.

## Dependency Rules

Allowed examples:

- feature-auth -> shared/core
- feature-dashboard -> shared/domain
- feature-dashboard -> design-system
- shared/data -> shared/domain
- shared/data -> storage
- shared/data -> logging
- networking -> logging
- storage -> logging

Avoid:

- domain -> data
- domain -> UI
- core -> feature-dashboard
- design-system -> feature-auth
- feature-watchlist -> feature-property-details unless explicitly justified
- circular dependencies

## Module Creation Rule

Create a module when at least one of these is true:

- It represents a reusable technical capability.
- It has a clear independent responsibility.
- It reduces coupling.
- It allows separate testing.
- It has different platform-specific implementations.
- It improves build organization without creating excessive complexity.

Do not create a module when:

- It only contains one tiny class.
- It creates ceremony without real separation.
- It makes navigation and dependencies harder for no gain.
- It duplicates Clean Architecture layers unnecessarily.

## AI Agent Rule

The AI agent must not create new modules without checking this document and the assigned task.

If a new module is needed, the agent must explain:

- Why the module is needed
- What responsibility it owns
- What it depends on
- What depends on it
- Why existing modules are not enough
