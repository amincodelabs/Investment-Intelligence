# Technical Specifications

This folder contains the technical specifications for the Real Estate Investment Intelligence Kotlin Multiplatform mobile app.

These documents define the architecture, module boundaries, coding rules, state management approach, data flow, testing strategy, logging expectations, security expectations, and AI agent development rules.

The AI agent must treat this folder as the technical source of truth.

## How the AI Agent Should Use This Folder

Before implementing any technical task, the agent must:

1. Read `specs/technical/README.md`.
2. Read `specs/technical/architecture.md`.
3. Read `specs/technical/modularization.md`.
4. Read `specs/technical/ai-agent-guidelines.md`.
5. Read any task-specific technical documents.
6. Read relevant business specs from `specs/business/`.
7. Implement only the assigned task.
8. Avoid introducing new architecture patterns without updating the specs.
9. Keep changes small, reviewable, and testable.

## Project Type

This is a Kotlin Multiplatform mobile project targeting:

- Android
- iOS

The project should be designed so that shared business logic, domain models, repositories, use cases, state handling, and data abstractions can live in shared Kotlin code where appropriate.

Platform-specific implementation details should be isolated behind abstractions.

## Architecture Summary

The project follows Clean Architecture principles with clear separation between:

- UI / Presentation
- Domain
- Data
- Infrastructure

The project should use MVVM with strong state-driven and event-driven behavior.

The preferred presentation style is MVVM with MVI-like discipline:

- Immutable UI state
- Explicit user events/intents
- Clear one-time UI effects
- Predictable state transitions
- Business logic outside UI

## Current Technical Scope

The first version focuses on:

- Clean KMP project foundation
- Modular structure
- Mock data in the data layer only
- Unit tests for domain and presentation logic
- Strong logging
- Security-conscious implementation
- AI-agent-friendly development workflow

Integration with real backend, production authentication, analytics SDKs, crash reporting, and full end-to-end tests are out of scope unless explicitly added later.
