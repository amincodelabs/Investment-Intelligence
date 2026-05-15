# Coding Standards

## Goal

Code should be readable, maintainable, testable, and consistent.

The project should follow strong engineering principles without unnecessary ceremony.

## Principles

Apply:

- SOLID
- Separation of concerns
- Dependency inversion
- Single responsibility
- Explicit naming
- Small classes/functions
- Testability
- Immutability where practical
- Clear boundaries

## Naming

Names must be clear and specific.

Prefer domain language from `specs/business/glossary.md`.

Prefer:

- `InvestmentScore`
- `RentalYield`
- `PropertyRepository`
- `CalculateInvestmentMetricsUseCase`
- `WatchlistViewModel`

Avoid vague names:

- `Manager`
- `Helper`
- `Util`
- `Processor` unless specific
- `DataHandler`
- `CommonModel`

## Function Design

Functions should:

- Do one thing.
- Have clear inputs and outputs.
- Avoid hidden side effects.
- Be easy to test.
- Avoid long parameter lists when a request object is clearer.

## Class Design

Classes should:

- Have one clear responsibility.
- Depend on abstractions when crossing boundaries.
- Avoid God objects.
- Avoid circular dependencies.
- Avoid mixing UI, data, and business logic.

## Immutability

Prefer immutable models for:

- Domain models
- UI state
- Value objects
- Calculation results

## Dependency Inversion

High-level logic should not depend on low-level implementation details.

Examples:

- ViewModel depends on repository contract or use case.
- Domain depends on repository contract.
- Data implements repository contract.
- Infrastructure implements technical abstraction.

## Comments

Comments should explain why, not repeat what the code says.

Add comments when:

- A business assumption is important.
- A formula needs explanation.
- A demo limitation exists.
- A non-obvious trade-off is made.

Avoid noisy comments.

## AI Agent Rule

The AI agent must not create duplicate concepts.

Before creating a new model, service, use case, or module, the agent should check whether an existing concept already represents the same thing.
