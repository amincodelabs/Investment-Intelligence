# Documentation Guidelines

## Goal

Documentation should help humans and AI agents understand the system.

Docs should stay practical and close to real implementation decisions.

## Required Documentation Areas

Keep documentation for:

- Business rules
- Architecture
- Module boundaries
- Data flow
- State management
- Testing strategy
- Security expectations
- Design system
- Tasks and acceptance criteria

## When to Update Specs

Update specs when:

- A new architectural decision is made.
- A new module is introduced.
- A business rule changes.
- A new feature is added.
- A workflow changes.
- A security rule is introduced.
- A repeated implementation pattern becomes official.

## Task Documentation

Each task should define:

- Goal
- Context
- Scope
- Out of scope
- Acceptance criteria
- Relevant specs
- Definition of done

## Code Documentation

Add code-level documentation when:

- Business formulas are implemented.
- Security-sensitive behavior exists.
- Platform-specific behavior needs explanation.
- Demo-only behavior must be clearly marked.
- A workaround is used.

## AI Agent Rule

If the agent changes an architectural pattern or creates a new reusable convention, it must update the relevant spec or clearly report that documentation needs to be updated.
