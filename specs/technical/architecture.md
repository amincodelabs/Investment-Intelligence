# Architecture

## Goal

The app must be built with a clean, maintainable, testable, and scalable architecture suitable for a Kotlin Multiplatform mobile project.

The architecture must support:

- Android and iOS outputs
- Shared business logic
- Clear separation of concerns
- Replaceable data sources
- Mock data today, backend integration later
- Strong testability
- AI-agent-friendly development

## Architectural Style

The project follows Clean Architecture principles.

Main layers:

1. UI / Presentation
2. Domain
3. Data
4. Infrastructure

These layers are logical boundaries. They do not necessarily need to be separate Gradle modules.

## Layer Responsibilities

### UI / Presentation Layer

Responsible for:

- Screens
- UI components
- ViewModels / presenters
- UI state
- User events/intents
- One-time UI effects
- Navigation triggers
- Formatting for display when appropriate

Must not contain:

- Investment calculations
- Repository implementation details
- Mock data
- Direct storage access
- Direct network access
- Business rules

### Domain Layer

Responsible for:

- Core business models
- Value objects
- Business rules
- Investment calculations
- Scoring logic
- Use cases when useful
- Repository contracts
- Domain services

The domain layer must be independent from:

- UI framework
- Database
- Network client
- Platform APIs
- Mock data implementation
- Dependency injection framework

### Data Layer

Responsible for:

- Repository implementations
- Data source coordination
- Mock data sources
- DTO mapping
- Cache/storage mapping if needed
- Data model conversion

Mock data must live only in the data layer.

The UI and domain layers must not know whether the data comes from mock data, local storage, or a future backend.

### Infrastructure Layer

Responsible for reusable technical capabilities such as:

- Networking
- Local storage
- Logging
- Serialization
- Date/time provider
- Platform abstractions
- Configuration
- Security utilities
- Dependency setup

Infrastructure should be isolated and reusable.

Feature logic must not be buried inside infrastructure modules.

## Dependency Rule

Dependencies must point inward.

Preferred direction:

```text
UI -> Domain
UI -> Infrastructure abstractions only when needed
Data -> Domain
Data -> Infrastructure
Infrastructure -> no business-specific feature dependency
```

Domain must not depend on Data, UI, or Infrastructure implementation details.

## Use Case Rule

Use cases should be created when they add value.

Create a use case when:

- Business logic is involved.
- Multiple repositories or services are coordinated.
- The operation needs validation or decision-making.
- The operation is reused by multiple ViewModels.
- The operation is complex enough to deserve a named business action.

Do not create a use case when:

- The ViewModel only forwards a simple read operation.
- There is no business logic.
- The use case would be a meaningless one-line wrapper.
- It creates noise without improving clarity.

This project values practical Clean Architecture, not ceremony.

## Business Logic Rule

Business logic must not be placed in UI code.

Examples of business logic:

- Rental yield calculation
- Net yield calculation
- Investment score calculation
- Risk interpretation
- Property comparison logic
- Watchlist business rules
- Authentication/session rules

These belong in domain services, use cases, or domain models depending on complexity.

## Presentation Pattern

Use MVVM as the base pattern.

Apply MVI-like discipline:

- ViewModel exposes immutable UI state.
- UI sends explicit user events/intents.
- ViewModel processes events and updates state.
- One-time effects are separated from persistent state.
- State transitions should be predictable and testable.

## State-Driven and Event-Driven Behavior

The app should be strongly state-driven and event-driven.

User actions should be represented explicitly.

Examples:

- `LoginSubmitted`
- `RegisterClicked`
- `AreaSelected`
- `PropertySaved`
- `CalculatorInputChanged`
- `ComparisonItemRemoved`

The UI should render state, not own business decisions.

## Platform-Specific Code

Platform-specific APIs must be hidden behind abstractions when used from shared code.

Examples:

- Storage
- Secure storage
- Logging sinks
- Network availability
- Date/time
- Device information

## Mock Data Strategy

Mock data must be implemented in the data layer.

Do not place mock data in:

- UI components
- ViewModels
- Domain services
- Use cases

Repository contracts should make future backend replacement possible with minimal breaking changes.

## Quality Expectations

The architecture must support:

- Unit testing
- Small tasks
- Small pull requests
- Consistent naming
- Replaceable implementations
- Clear module boundaries
- Minimal coupling
- No circular dependencies
