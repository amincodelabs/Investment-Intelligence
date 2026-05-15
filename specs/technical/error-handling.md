# Error Handling

## Goal

Errors must be predictable, user-friendly, testable, and safe.

The app should avoid leaking raw technical failures into UI.

## Error Categories

Suggested error categories:

- Validation error
- Network error
- Storage error
- Data parsing error
- Unauthorized error
- Not found error
- Unknown error

## Domain Errors

Domain errors represent business rule failures.

Examples:

- Invalid property price
- Missing required investment input
- Invalid rental yield input
- Watchlist item already exists
- Invalid login credentials

## Data Errors

Data errors represent data retrieval or persistence failures.

Examples:

- Mock data not found
- Storage read failure
- Storage write failure
- Data mapping failure

## UI Error Display

UI should show clear messages.

Rules:

- Avoid exposing stack traces.
- Avoid showing raw exception names to users.
- Give retry action when possible.
- Validation errors should be shown near the related field when practical.
- Empty state should not be treated as an error unless something failed.

## Result Type

Use a consistent result type for operations that can fail.

Example concept:

```text
Result<SuccessValue, AppError>
```

The exact implementation may vary.

## Logging Errors

Log errors with enough context to debug.

Do log:

- Error category
- Operation name
- Feature/screen
- Non-sensitive IDs if useful
- Stack trace in debug builds if safe

Do not log:

- Passwords
- Tokens
- Secret keys
- Full personal data
- Sensitive auth values

## Testing

Unit tests should cover expected error cases.

Examples:

- Invalid calculator input
- Missing property data
- Login validation failure
- Repository returns error
- ViewModel maps error to UI state
