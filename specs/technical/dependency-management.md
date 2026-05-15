# Dependency Management

## Goal

Dependencies should be added carefully.

Every dependency should have a clear purpose.

## Rules

- Do not add a dependency without a reason.
- Prefer standard Kotlin/KMP capabilities when sufficient.
- Prefer multiplatform-compatible libraries for shared code.
- Keep platform-specific libraries isolated.
- Avoid dependencies that force business logic into platform-specific code.
- Avoid large frameworks for small problems.
- Avoid duplicate libraries for the same responsibility.

## Dependency Categories

Possible dependency categories:

- Coroutines
- Serialization
- Dependency injection
- HTTP client
- Local storage
- Logging
- Testing
- Date/time
- UI framework

## AI Agent Rule

The AI agent must not introduce new dependencies unless the task explicitly allows it or the need is clear and explained.

If a dependency is added, the agent should report:

- Dependency name
- Purpose
- Module where it was added
- Why existing tools were not enough
