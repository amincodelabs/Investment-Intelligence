# State Management

## Goal

The app should be predictable, testable, state-driven, and event-driven.

Use MVVM as the base pattern with MVI-like discipline.

## Core Concepts

Each screen should define:

- UI State
- User Events / Intents
- One-time Effects
- ViewModel

## UI State

UI state represents everything needed to render the screen.

Rules:

- UI state should be immutable.
- UI state should be explicit.
- UI state should not expose mutable collections.
- UI state should not contain business services or repositories.
- UI state should not trigger side effects by itself.

Example state concepts:

- Loading
- Content
- Empty
- Error
- Form input
- Validation messages
- Selected item
- Saved state

## User Events / Intents

User events represent things the user or UI does.

Examples:

- `EmailChanged`
- `PasswordChanged`
- `LoginSubmitted`
- `AreaClicked`
- `PropertySaved`
- `CalculatorInputChanged`
- `CompareClicked`
- `RetryClicked`

Rules:

- Events should be explicit.
- Events should be named from the user's action or UI event.
- Events should not contain business logic.
- Events should be processed by the ViewModel.

## One-Time Effects

One-time effects represent actions that should happen once.

Examples:

- Navigate to dashboard
- Show snackbar
- Show dialog
- Open property details
- Trigger haptic feedback
- Show toast

Rules:

- Do not store one-time actions as permanent UI state.
- Effects must be separated from state.
- Effects should be easy to test where practical.

## ViewModel Responsibilities

ViewModels may:

- Receive events/intents.
- Coordinate use cases.
- Read repositories only if no use case is needed and the operation is simple.
- Update UI state.
- Emit one-time effects.
- Handle presentation-level validation.
- Log meaningful state transitions and errors.

ViewModels must not:

- Own business calculations.
- Create mock data.
- Directly access platform APIs.
- Directly access storage or network clients.
- Contain large business workflows that belong in use cases or domain services.

## State Transition Rule

State transitions should be predictable.

For complex screens, use a clear reducer-like approach.

```text
Current State + Event + Result = New State
```

## Loading/Error/Empty Handling

Each screen should intentionally handle:

- Initial loading
- Content
- Empty data
- Recoverable error
- Retry action
- Validation error

## Logging in State Management

Log meaningful events such as:

- Screen loaded
- User action received
- Data loading started
- Data loading succeeded
- Data loading failed
- Validation failed
- Navigation effect emitted

Do not log sensitive values such as passwords, tokens, or personal secrets.

## Testing State Management

Unit tests should verify:

- Initial state
- Event handling
- Loading state
- Success state
- Error state
- Validation behavior
- One-time effect emission where practical
