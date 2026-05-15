# Feature: AI Insights

## Purpose

Provide short, helpful, explainable insights based on visible investment data.

AI insights should help users understand the meaning of the numbers.

## User Value

Users can:

- Understand why an area or property may be attractive.
- Notice risks they might otherwise miss.
- Get a quick summary before reading all metrics.
- Compare opportunities faster.

## Main User Actions

- Read insight on dashboard.
- Read insight on area details.
- Read insight on property details.
- Read insight on comparison summary.
- Optionally expand insight for more detail if supported later.

## Insight Examples

Good examples:

- "This property shows strong gross yield, but service charges reduce the net return."
- "JVC appears attractive for rental yield, while Downtown may be stronger for long-term appreciation."
- "This opportunity has positive cash flow based on current assumptions."
- "The property may be relatively expensive compared with the area average price per sqft."

Bad examples:

- "This property will make you rich."
- "This is guaranteed profit."
- "You should buy this immediately."
- "The market will definitely rise."

## Business Rules

- AI insights must be grounded in available data.
- AI insights must not invent unsupported facts.
- AI insights must not guarantee returns.
- AI insights must use cautious language.
- AI insights must be short and understandable.
- AI insights should explain trade-offs, not just praise opportunities.

## Acceptance Criteria

- User can see a short insight where relevant.
- Insight is based on visible metrics.
- Insight avoids guaranteed claims.
- Insight helps the user understand return, risk, or comparison.
