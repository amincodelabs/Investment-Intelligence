# Investment Scoring Model

The investment score is a simplified estimate of how attractive a property or area appears as an investment opportunity.

The score is for decision support only.

It is not financial advice.

## Score Range

Investment score must be from 0 to 100.

- 0 means very unattractive based on available assumptions.
- 100 means highly attractive based on available assumptions.

## Suggested Property Score Weights

For the first demo, a property investment score may use:

- Rental yield: 35%
- Price attractiveness: 20%
- Area demand: 20%
- Appreciation potential: 15%
- Risk level: 10%

Total: 100%

## Suggested Area Score Weights

For area intelligence, an area investment score may use:

- Average rental yield: 30%
- Demand level: 25%
- Appreciation potential: 20%
- Price attractiveness: 15%
- Risk level: 10%

Total: 100%

## Input Normalization

Different inputs should be normalized before scoring.

Examples:

- Higher rental yield should improve the score.
- Higher demand should improve the score.
- Higher appreciation potential should improve the score.
- Higher risk should reduce the score.
- Overpriced properties should reduce the score.

## Risk Mapping

Suggested risk mapping:

- Low risk: positive effect
- Medium risk: neutral or small negative effect
- High risk: negative effect

## Demand Mapping

Suggested demand mapping:

- High demand: positive effect
- Medium demand: neutral effect
- Low demand: negative effect

## Score Explanation

The app should explain why a score is high or low.

Example insights:

- Strong rental yield improves the score.
- High service charges reduce net yield.
- High area demand improves investment attractiveness.
- High risk level reduces the score.

## Business Rules

- The score must be explainable.
- The score must be consistent.
- The score must not be presented as guaranteed performance.
- If important inputs are missing, the score should be marked as estimated or incomplete.
- Do not create multiple unrelated scoring systems unless the specs are updated.
