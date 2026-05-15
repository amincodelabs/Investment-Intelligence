# Testing Strategy

## Current Testing Scope

For the first version, write unit tests only.

UI tests, integration tests, snapshot tests, and end-to-end tests are out of scope unless explicitly added later.

## Testing Priorities

Prioritize tests for:

1. Domain business logic
2. Investment calculations
3. Scoring model
4. Use cases
5. Repository behavior with mock data
6. ViewModel state transitions
7. Validation rules
8. Error mapping

## Must-Test Business Logic

Unit tests should cover:

- Rental yield calculation
- Net rental yield calculation
- Price per sqft calculation
- Cash flow calculation
- Investment score calculation
- Risk/demand mapping
- Comparison rules
- Watchlist duplicate prevention
- Authentication validation rules

## ViewModel Tests

ViewModel tests should cover:

- Initial state
- Loading state
- Successful data loading
- Error state
- User event handling
- Validation errors
- One-time effects
- Retry behavior where applicable

## Repository Tests

Repository tests should cover:

- Returning mock data
- Mapping data to domain models
- Handling missing data
- Handling data source errors
- Watchlist save/remove if repository owns it

## Test Naming

Test names should describe behavior.

Prefer:

```text
calculateRentalYield returns expected percentage when price and rent are valid
```

Avoid vague names:

```text
test1
testCalculation
worksCorrectly
```

## Test Structure

Use Arrange, Act, Assert.

Example:

```text
Arrange: create input data
Act: call function/use case
Assert: verify result
```

## AI Agent Rule

When the agent implements business logic, it should add or update unit tests in the same task unless the task explicitly says tests are not required.

If tests are skipped, the agent must explain why.
