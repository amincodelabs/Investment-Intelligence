# Data Flow

## Preferred Data Flow

```text
UI
→ User Event / Intent
→ ViewModel
→ Use Case when useful
→ Repository Contract
→ Repository Implementation
→ Data Source
→ Repository Implementation
→ Use Case
→ ViewModel
→ UI State / Effect
→ UI
```

## UI Access Rule

The UI must not access repositories directly.

The UI communicates with the ViewModel.

## ViewModel Access Rule

The ViewModel may call:

- Use cases
- Simple repository contracts only when no business logic exists
- Validators
- Presentation mappers
- Logger

The ViewModel must not call:

- Raw data sources
- Mock data directly
- Network clients directly
- Storage implementations directly

## Repository Rule

Repository contracts belong in the domain layer.

Repository implementations belong in the data layer.

Repositories should hide data source details from domain and UI.

## Data Source Rule

Data sources are implementation details.

Possible data sources:

- Mock data source
- Local storage data source
- Remote API data source
- Cache data source

For the first demo, mock data source is acceptable.

## Mapper Rule

Use mappers when crossing boundaries.

Examples:

- DTO -> Data model
- Data model -> Domain model
- Storage entity -> Domain model

Avoid leaking data-specific models into UI or domain.

## Error Flow

Errors should be mapped into app-level error types.

Avoid exposing low-level technical exceptions directly to UI.

Example:

```text
NetworkException -> AppError.Network
SerializationException -> AppError.DataParsing
UnauthorizedException -> AppError.Unauthorized
```

## Logging Flow

Important flow points should be logged:

- Repository request started
- Data source selected
- Data loaded
- Data mapping failed
- Use case failed
- ViewModel state changed after important events

Do not log sensitive data.

## Future Backend Replacement

Because mock data is only in the data layer, replacing mock data with a backend should mainly affect:

- Data sources
- Repository implementations
- DTOs
- Mappers
- Dependency setup

It should not require changing:

- UI screens
- ViewModels except loading/error behavior if needed
- Domain models unless the business domain changes
- Use case contracts unless requirements change
