# Investment Logic

This document defines the business calculation logic used by the app.

The first demo version should keep calculations simple, understandable, and testable.

## Currency

Default currency: AED

All money values should include currency in the UI.

## Rental Yield

Rental yield shows how much annual rent is generated compared to property price.

Formula:

`Rental Yield = Annual Rent / Property Price * 100`

Example:

- Property Price: 1,000,000 AED
- Annual Rent: 80,000 AED

Rental Yield:

`80,000 / 1,000,000 * 100 = 8%`

## Gross Rental Yield

Gross rental yield does not include costs.

Formula:

`Gross Rental Yield = Annual Rent / Property Price * 100`

## Net Rental Yield

Net rental yield includes estimated yearly costs.

Formula:

`Net Rental Yield = (Annual Rent - Annual Costs) / Property Price * 100`

Annual costs may include:

- Service charges
- Maintenance estimate
- Property management fee
- Vacancy estimate
- Other recurring ownership costs

Example:

- Property Price: 1,000,000 AED
- Annual Rent: 80,000 AED
- Annual Costs: 15,000 AED

Net Rental Yield:

`(80,000 - 15,000) / 1,000,000 * 100 = 6.5%`

## Monthly Rent

Formula:

`Monthly Rent = Annual Rent / 12`

## Monthly Costs

Formula:

`Monthly Costs = Annual Costs / 12`

## Monthly Cash Flow

Formula:

`Monthly Cash Flow = Monthly Rent - Monthly Costs - Monthly Mortgage Payment`

For the first demo, mortgage payment may be optional.

If mortgage is not included:

`Monthly Cash Flow = Monthly Rent - Monthly Costs`

## Annual Cash Flow

Formula:

`Annual Cash Flow = Annual Rent - Annual Costs - Annual Mortgage Payments`

If mortgage is not included:

`Annual Cash Flow = Annual Rent - Annual Costs`

## Price Per Sqft

Formula:

`Price Per Sqft = Property Price / Size Sqft`

Example:

- Property Price: 1,200,000 AED
- Size: 800 sqft

Price Per Sqft:

`1,200,000 / 800 = 1,500 AED/sqft`

## Occupancy Adjustment

If occupancy is used, expected rent should be adjusted.

Formula:

`Adjusted Annual Rent = Annual Rent * Occupancy Rate`

Example:

- Annual Rent: 100,000 AED
- Occupancy Rate: 90%

Adjusted Annual Rent:

`100,000 * 0.90 = 90,000 AED`

## Vacancy Estimate

Vacancy can be modeled as the opposite of occupancy.

Formula:

`Vacancy Rate = 100% - Occupancy Rate`

For simple calculations, vacancy cost may be represented as lost rent.

## Appreciation Estimate

Appreciation estimates possible property value growth.

Formula:

`Estimated Future Value = Property Price * (1 + Appreciation Rate)`

Example:

- Property Price: 1,000,000 AED
- Appreciation Rate: 5%

Estimated Future Value after one year:

`1,000,000 * 1.05 = 1,050,000 AED`

## Simple ROI

For the first demo, simple ROI may be calculated as:

`Simple ROI = (Annual Cash Flow + Estimated Annual Appreciation) / Initial Investment * 100`

Where:

- Annual Cash Flow = Annual Rent - Annual Costs
- Estimated Annual Appreciation = Property Price * Appreciation Rate
- Initial Investment may be full property price or user-entered invested capital

For the first demo, if no down payment or mortgage exists, use property price as the initial investment.

## Investment Calculation Rules

- Do not divide by zero.
- If required values are missing, return an incomplete calculation state.
- Do not hide negative results.
- Negative cash flow should be shown clearly.
- Percentages should be rounded consistently.
- Money values should be formatted consistently.
- Business logic must be testable outside the UI layer.
