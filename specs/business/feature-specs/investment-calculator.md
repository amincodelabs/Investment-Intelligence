# Feature: Investment Calculator

## Purpose

Allow users to test investment assumptions and calculate estimated return, yield, and cash flow.

## User Value

Users can:

- Enter property investment values.
- Understand expected rental yield.
- Estimate annual and monthly cash flow.
- See the effect of costs on returns.
- Make more informed comparisons.

## Main User Actions

- Enter property price.
- Enter expected annual rent.
- Enter annual service charges.
- Enter other annual costs if supported.
- Enter occupancy rate if supported.
- Calculate investment metrics.
- Reset inputs.
- Save calculation if supported later.

## Inputs

Required or suggested inputs:

- Property price
- Expected annual rent
- Annual service charges
- Other annual costs
- Occupancy rate
- Appreciation rate

Optional future inputs:

- Down payment
- Mortgage amount
- Interest rate
- Loan duration
- Property management fee
- Vacancy estimate

## Outputs

The calculator may show:

- Gross rental yield
- Net rental yield
- Monthly rent
- Monthly costs
- Monthly cash flow
- Annual cash flow
- Price per sqft if size is included
- Simple ROI if appreciation is included

## Business Rules

- Required inputs must be validated.
- Property price must be greater than zero.
- Annual rent must not be negative.
- Costs must not be negative.
- Calculations must not divide by zero.
- Negative cash flow should be shown clearly.
- Results are estimates.
- The calculator must not present results as financial advice.

## Acceptance Criteria

- User can enter investment values.
- User can calculate gross rental yield.
- User can calculate net rental yield.
- User can see monthly or annual cash flow.
- User sees validation errors for invalid input.
- User can understand that outputs are estimates.
