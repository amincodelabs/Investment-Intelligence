# Development Workflow

## Goal

The workflow should support controlled AI-agent-assisted development.

The human acts as architect and reviewer.

The AI agent acts as implementation assistant.

## Recommended Workflow

For each task:

1. Select one small task.
2. Provide the task file or task description to the AI agent.
3. Tell the agent which specs to read.
4. Agent implements only the assigned scope.
5. Agent adds or updates unit tests when relevant.
6. Human reviews the diff.
7. Human requests corrections if needed.
8. Merge only after review.

## Task Size

Tasks should be small.

Good task examples:

- Create domain models for property and area.
- Implement rental yield calculation.
- Implement mock area repository.
- Implement dashboard ViewModel state.
- Implement login validation.
- Add unit tests for investment calculator.

Bad task examples:

- Build the whole app.
- Implement all features.
- Create architecture and UI and tests together.
- Refactor everything.

## AI Agent Prompt Pattern

Recommended task prompt:

```text
Read:
- specs/technical/README.md
- specs/technical/architecture.md
- specs/technical/modularization.md
- specs/technical/state-management.md
- specs/business/glossary.md
- specs/business/feature-specs/[feature].md

Task:
[Clear task description]

Rules:
- Implement only this task.
- Do not introduce unrelated changes.
- Add/update unit tests where applicable.
- Follow existing architecture.
- Report assumptions.
```

## Review Checklist

Before accepting agent output:

- Does it match the task?
- Did it avoid unrelated changes?
- Did it follow specs?
- Is business logic outside UI?
- Are mock data sources only in data layer?
- Are names consistent with glossary?
- Are unit tests added where needed?
- Are logs useful and safe?
- Are security rules respected?
- Does the app still build?

## Commit Style

Use clear commits.

Example:

```text
feat(calculator): add rental yield calculation
test(calculator): cover invalid investment inputs
docs(specs): update scoring model assumptions
```

## Avoid

Avoid:

- Giant agent-generated changes
- Unreviewed architecture changes
- Multiple unrelated features in one task
- Silent dependency additions
- Duplicate models
- Business rules hidden in UI
