# Feature: Authentication and Account

## Purpose

Allow users to enter the app through registration and login flows and maintain a basic user account experience.

For the first demo, authentication may be simulated locally.

The goal is to make the app feel like a real product while keeping backend complexity out of scope.

## User Value

Users can:

- Create a demo account.
- Login to the app.
- Logout from the app.
- View basic profile/account information.
- Keep saved preferences and watchlist associated with their account.

## Main User Actions

- Register a new account.
- Login with existing credentials.
- Logout.
- View profile.
- Update simple investment preferences if included.
- Continue as demo user if supported by a task.

## Displayed Information

Registration may include:

- Full name
- Email
- Password
- Confirm password

Login may include:

- Email
- Password

Profile may include:

- Name
- Email
- Investment preferences
- Saved areas count
- Saved properties count

## Business Rules

- Authentication may be mocked for the demo.
- The app should be designed so real authentication can be added later.
- Registration must validate required fields.
- Email must have a valid basic email format.
- Password validation may be simple but must be consistent.
- Password and confirm password must match during registration.
- Login should show a clear error for invalid credentials.
- Logout should clear the current session.
- Auth state should not be mixed with investment calculation logic.
- The user should not need KYC or document verification in the demo version.

## Suggested Demo Password Rules

For the demo version:

- Password is required.
- Minimum length: 8 characters.
- Confirm password must match password.

Advanced password rules can be added later.

## Out of Scope

- Real backend authentication.
- Real email verification.
- Password reset email.
- Multi-factor authentication.
- KYC.
- Social login.
- Payment authentication.
- Production security hardening.

## Acceptance Criteria

- User can open the login screen.
- User can navigate from login to registration.
- User can register with valid demo information.
- User sees validation errors for invalid registration input.
- User can login after registration or through predefined demo credentials.
- User can logout.
- User session state is reflected in navigation.
- User can access the main app after login.
