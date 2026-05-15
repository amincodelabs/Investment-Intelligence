# Platform Guidelines

## Goal

The app targets Android and iOS.

Shared code should contain business and platform-independent logic.

Platform-specific code should be isolated.

## Android

Android-specific code may include:

- Android app entry point
- Android UI implementation if UI is platform-specific
- Android storage implementation
- Android logging sink
- Android secure storage implementation
- Android navigation integration
- Android-specific permissions if needed

## iOS

iOS-specific code may include:

- iOS app entry point
- SwiftUI/UIKit integration if UI is platform-specific
- iOS storage implementation
- iOS logging sink
- iOS secure storage implementation
- iOS navigation integration

## Shared Code

Shared code should include:

- Domain models
- Business logic
- Repository contracts
- Use cases
- Mock repository/data source if platform-independent
- State models
- Shared ViewModels if the selected UI approach supports it
- Validation logic
- Error models
- Logging abstraction

## Platform Abstraction Rule

If shared code needs platform behavior, define an abstraction.

Examples:

- `Logger`
- `KeyValueStorage`
- `SecureStorage`
- `DateTimeProvider`
- `NetworkStatusProvider`

Platform implementations should live outside pure domain logic.

## AI Agent Rule

The agent must not place platform-specific code inside domain logic.
