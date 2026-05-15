# Security Guidelines

## Goal

The project must avoid basic security mistakes even in the demo version.

The app is not production-ready yet, but the architecture should not encourage insecure habits.

## General Security Rules

- Do not hardcode secrets.
- Do not log passwords, tokens, API keys, or secret values.
- Do not store passwords in plain text.
- Do not place sensitive data in mock files unless fake and clearly demo-only.
- Do not expose technical stack traces to users.
- Validate user input.
- Keep authentication/session logic separated from investment business logic.
- Treat future backend integration as security-sensitive.

## Authentication Security

For the demo version, authentication may be mocked.

Even then:

- Do not store real user passwords.
- Do not log password input.
- Do not create fake code that looks production-safe if it is not.
- Make demo limitations clear in comments or docs when needed.

Future production auth should use backend-managed secure authentication.

## Storage Security

For future storage:

- Tokens must use secure storage.
- Sensitive session data must not be stored in plain local storage.
- User preferences can use regular local storage if not sensitive.
- Cached property data is generally non-sensitive, but user-specific saved data should still be handled carefully.

## Network Security

For future networking:

- Use HTTPS.
- Avoid disabling certificate validation.
- Avoid logging full request/response bodies if they may contain sensitive data.
- Map network errors safely.
- Do not expose raw backend errors directly to users.

## Input Validation

Validate:

- Email format
- Required fields
- Numeric calculator values
- Non-negative costs
- Property price greater than zero
- Valid percentage ranges
- Valid selection values

## Logging Security

Never log:

- Passwords
- Tokens
- API keys
- Secrets
- Private user notes
- Sensitive identifiers

## AI Agent Rule

The AI agent must not introduce insecure shortcuts.

Examples of forbidden behavior:

- Hardcoding API keys
- Logging credentials
- Storing passwords as plain text for realistic-looking auth
- Adding disabled SSL verification
- Returning raw exceptions to UI
- Adding fake production claims for demo auth
