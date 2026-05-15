# Logging

## Goal

The project should include strong and consistent logging to support development, debugging, and future production observability.

Logging must be useful but safe.

## Logging Principles

- Log important flow events.
- Log errors with enough context.
- Do not log sensitive data.
- Do not spam meaningless logs.
- Prefer structured logs where possible.
- Use log levels consistently.
- Keep logs searchable and understandable.

## Log Levels

### Debug

Use for development details.

Examples:

- ViewModel event received
- State transition
- Mock data source used
- Mapper executed

### Info

Use for important successful operations.

Examples:

- User logged in
- Dashboard loaded
- Property saved to watchlist
- Calculator completed

### Warning

Use for recoverable unexpected situations.

Examples:

- Missing optional data
- Fallback data used
- Incomplete score inputs
- Retryable storage failure

### Error

Use for failed operations.

Examples:

- Repository failed to load data
- Storage write failed
- Unexpected exception
- Invalid app state

## Required Logging Areas

Logging should be considered in:

- App startup
- Authentication flow
- Repository operations
- Data source access
- Investment calculation failures
- Watchlist actions
- Navigation effects when useful
- Error handling
- Important state transitions

## Security Rules

Never log:

- Passwords
- Auth tokens
- Refresh tokens
- API keys
- Private keys
- Full user personal data
- Sensitive request/response bodies

Be careful with:

- Email addresses
- User IDs
- Location details
- Financial input values

For the demo, financial values may be logged in debug only if useful, but avoid making this a habit.

## Logger Abstraction

Use a logger abstraction instead of platform-specific logging directly across the codebase.

Platform-specific loggers should be implemented behind the abstraction.

## AI Agent Rule

The AI agent should add meaningful logs when implementing flows, especially:

- Loading data
- Handling user events
- Saving/removing watchlist items
- Running calculations
- Handling errors

The agent must not add logs containing sensitive values.
