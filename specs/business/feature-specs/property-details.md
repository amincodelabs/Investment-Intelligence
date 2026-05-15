# Feature: Property Details

## Purpose

Show property-level investment information so the user can evaluate whether a property is a good opportunity.

## User Value

Users can:

- Understand property price and expected rent.
- Review investment metrics.
- See estimated yield and cash flow.
- Understand the area context.
- Save property to watchlist.
- Compare property with alternatives.

## Main User Actions

- Open property details.
- Review investment metrics.
- Save or remove property from watchlist.
- Open area details.
- Add property to comparison.
- Open calculator with property values pre-filled if supported by a task.

## Displayed Information

Property details may show:

- Property title
- Area name
- Price
- Size in sqft
- Price per sqft
- Bedrooms
- Bathrooms
- Expected annual rent
- Gross rental yield
- Net rental yield
- Service charges
- Estimated annual costs
- Monthly cash flow
- Property type
- Completion status
- Developer name
- Investment score
- Risk level
- AI insight

## Business Rules

- Every property must belong to an area.
- Every property must have price and size.
- Rental yield must use documented formula.
- Net rental yield must subtract annual costs.
- Negative cash flow must be shown clearly.
- Investment score must be treated as an estimate.
- Property details must not include contact-agent or purchase flows in the demo version unless explicitly added later.
- Property can be saved to watchlist.

## Acceptance Criteria

- User can open a property details page.
- User can see key property information.
- User can see investment metrics.
- User can understand gross and net rental yield.
- User can save or unsave the property if watchlist is included.
- User can add the property to comparison if comparison is included.
