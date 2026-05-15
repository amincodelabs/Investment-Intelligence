# Use Case Guidelines

## Purpose

Use cases represent meaningful application actions.

This project should use use cases where they improve clarity, testability, and business separation.

The project must avoid unnecessary use case ceremony.

## When to Create a Use Case

Create a use case when the operation:

- Contains business logic.
- Coordinates multiple repositories.
- Applies business rules.
- Performs validation beyond simple form validation.
- Is reused by more than one ViewModel.
- Has important error handling.
- Needs unit tests as an application operation.
- Represents a meaningful user/business action.

Examples:

- CalculateInvestmentMetricsUseCase
- ComparePropertiesUseCase
- GetAreaInvestmentDetailsUseCase
- SavePropertyToWatchlistUseCase
- RegisterUserUseCase
- LoginUserUseCase
- LogoutUserUseCase
- GetDashboardOverviewUseCase

## When Not to Create a Use Case

Do not create a use case when:

- It only calls one repository method with no additional logic.
- It adds no clarity.
- It creates a meaningless wrapper.
- It makes the code harder to follow.
- It is only used once and the operation is trivial.

Example of questionable use case:

```text
GetPropertyByIdUseCase -> repository.getPropertyById(id)
```

This may be unnecessary unless it later adds business rules, logging, validation, or orchestration.

## Use Case Naming

Use verb-based names.

Prefer:

- `CalculateInvestmentMetricsUseCase`
- `ComparePropertiesUseCase`
- `SaveAreaToWatchlistUseCase`
- `LoadDashboardOverviewUseCase`

Avoid:

- `PropertyUseCase`
- `HandleDataUseCase`
- `ManagerUseCase`
- `ProcessUseCase`

## Input and Output

For non-trivial use cases, prefer explicit request/result models.

Example:

```text
CalculateInvestmentMetricsRequest
InvestmentMetricsResult
```

Use simple parameters only when the use case is small and clear.

## Error Handling

Use cases should return predictable result types.

Avoid throwing raw exceptions to ViewModels for expected failures.

## Testing

Use cases that contain business rules must have unit tests.

Tests should cover:

- Success path
- Invalid input
- Edge cases
- Error mapping
- Boundary values
