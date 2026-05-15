# Business Rules

## General Rules

- The app is focused on real estate investment analysis.
- The app is not a property marketplace.
- The app must not claim guaranteed profit.
- The app must not present investment results as financial advice.
- All calculated results must be treated as estimates.
- Mock data is allowed for the demo version.
- Mock data must be realistic and internally consistent.
- Currency should default to AED.
- Dubai/UAE should be the default market for the demo version.
- The app should clearly distinguish between property data, calculated metrics, and AI-generated insights.
- The app should avoid exaggerated claims such as "best guaranteed investment" or "risk-free opportunity".

## Authentication and Account Rules

- The demo app may include registration and login flows.
- Authentication may be simulated locally in the first version.
- The app should behave as if authentication can later be replaced with a real backend service.
- User profile, session, and preferences should be modeled clearly even if stored locally.
- The user should be able to register, login, logout, and view basic profile information.
- The app should not require real identity verification in the demo version.
- The app should not collect sensitive identity documents in the demo version.
- Password validation rules may be simplified for the demo, but must be documented.
- Any simulated auth behavior must be clearly separated from investment business logic.

## User Preference Rules

The app may use user preferences to improve recommendations and defaults.

Possible preferences:

- Budget range
- Preferred areas
- Investment goal
- Risk tolerance
- Preferred property type
- Minimum expected yield

Preferences should influence the user experience but should not silently hide important information.

## Property Rules

- Every property must belong to an area.
- Every property must have a price.
- Every property must have a size in square feet.
- Every property should have an expected annual rent for investment calculations.
- Every property should have a property type.
- Every property should have a completion status.
- Property metrics must be calculated consistently.
- A property can be saved to the watchlist.
- A property can be compared with other properties.
- A property should not show investment metrics if required input data is missing, unless the missing data is clearly handled.

## Area Rules

- Every area must have a name.
- Every area should include average market metrics.
- Every area may include demand level, risk level, appreciation potential, and investment score.
- An area can be saved to the watchlist.
- Area metrics should be treated as market estimates.
- Area intelligence should help users understand investment attractiveness, not just location details.

## Calculation Rules

- Rental yield must be calculated using annual rent and property price.
- Net rental yield must deduct annual costs before calculating yield.
- Monthly cash flow must consider monthly income and monthly expenses.
- Service charges should be treated as a cost.
- Mortgage-related calculations are optional for the first demo unless included in a specific task.
- Calculations must use consistent units and currency.
- Percentages should be formatted clearly.
- Negative cash flow should be visible and not hidden.

## Scoring Rules

- Investment score must be between 0 and 100.
- Higher score means the opportunity appears more attractive.
- The score must be based on documented assumptions.
- The score should not be presented as a guarantee.
- If score inputs are missing, the score should either not be shown or should be marked as incomplete/estimated.
- The scoring model must be consistent across the app.

## Watchlist Rules

- A user can save properties to the watchlist.
- A user can save areas to the watchlist.
- A user can remove saved items from the watchlist.
- Watchlist items should be available after app restart in the demo if local persistence is implemented.
- Watchlist should support future alerts, but real notifications are optional for the first demo.

## Comparison Rules

- A user should be able to compare multiple properties.
- The comparison should focus on investment metrics.
- The comparison should make differences easy to understand.
- The app should avoid declaring one property as absolutely better unless based on clear documented criteria.
- Comparison should include both return and risk indicators.

## AI Insight Rules

- AI insights should be short, explainable, and grounded in visible data.
- AI insights must not make guaranteed investment claims.
- AI insights should use cautious wording such as "may", "appears", "based on available assumptions", or "estimated".
- AI insights should not invent facts that are not available in the data.
- AI insights should be helpful but not replace the underlying numbers.

## Legal and Trust Rules

- The app must not provide legal, tax, or regulated financial advice.
- The app must not guarantee rental income, appreciation, or profitability.
- The app should communicate that calculations are estimates.
- The app should encourage users to validate important decisions with professional advisors in future production versions.
