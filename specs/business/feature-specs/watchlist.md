# Feature: Watchlist

## Purpose

Allow users to save properties and areas they want to monitor or review later.

## User Value

Users can:

- Save interesting properties.
- Save attractive areas.
- Return to saved items later.
- Track investment opportunities.
- Build a shortlist before making decisions.

## Main User Actions

- Save property.
- Remove property.
- Save area.
- Remove area.
- View saved properties.
- View saved areas.
- Open saved item details.

## Displayed Information

Watchlist may show:

- Saved properties
- Saved areas
- Property price
- Expected rental yield
- Investment score
- Area demand level
- Risk level
- Last updated label if available
- Empty state when nothing is saved

## Business Rules

- Watchlist belongs to the current user/account.
- In the demo version, watchlist may be stored locally.
- User can remove saved items.
- Duplicate saved items should not be created.
- Saved properties and saved areas should be clearly separated or clearly labeled.
- Watchlist should support future alerts, but real notifications are optional for the first demo.

## Acceptance Criteria

- User can save a property.
- User can save an area.
- User can view saved properties and areas.
- User can remove saved items.
- User sees an empty state when watchlist is empty.
- Saved items remain associated with the current user/session if local persistence is implemented.
