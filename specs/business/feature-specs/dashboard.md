# Feature: Dashboard

## Purpose

Provide a high-level investment overview when the user opens the app.

The dashboard should help the user quickly understand current investment opportunities and market direction.

## User Value

Users can quickly see:

- Best-performing areas.
- High-yield opportunities.
- Saved watchlist summary.
- Market overview.
- Recommended next actions.

## Main User Actions

- View market summary.
- Open an area from the dashboard.
- Open a property opportunity.
- Open watchlist.
- Open investment calculator.
- View AI-generated market insight if available.

## Displayed Information

The dashboard may show:

- Market overview cards.
- Top areas by investment score.
- Top properties by rental yield.
- Watchlist summary.
- Recent opportunities.
- Average rental yield.
- Average price movement.
- Short AI insight.

## Business Rules

- Dashboard should prioritize investment intelligence, not listing volume.
- Numbers must be clearly labeled.
- Mock metrics must be internally consistent.
- Dashboard should avoid overwhelming the user.
- Dashboard insights must not guarantee returns.
- If user is not logged in, dashboard access depends on the auth/navigation decision defined by tasks.

## Acceptance Criteria

- User can view a dashboard after entering the app.
- Dashboard shows at least one market summary section.
- Dashboard shows top investment areas or opportunities.
- Dashboard allows navigation to area details or property details.
- Dashboard uses AED by default.
- Dashboard does not present estimates as guaranteed returns.
