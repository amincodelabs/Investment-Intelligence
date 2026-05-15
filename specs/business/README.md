# Business Specifications

This folder contains the business specifications for the Real Estate Investment Intelligence mobile app.

These documents define what the product is, who it is for, the business rules, core domain concepts, investment logic, and feature-level requirements.

The AI agent must treat these files as the business source of truth.

## How the AI Agent Should Use This Folder

Before implementing any business-related feature, the agent must:

1. Read this `README.md`.
2. Read `product-vision.md`.
3. Read `glossary.md`.
4. Read `business-rules.md`.
5. Read the relevant feature specification under `feature-specs/`.
6. Implement only what the assigned task requires.
7. Avoid inventing business behavior that is not documented.
8. If a requirement is unclear, make the smallest reasonable assumption and document it in code comments or the task output.

## Product Type

This app is not a property marketplace.

It is an investment intelligence mobile app for helping users evaluate real estate investment opportunities.

The app may show properties, areas, market metrics, and calculations, but its primary purpose is analysis and decision support, not buying, selling, or contacting agents.

## Current Stage

The current version is a demo mobile app.

Backend, real authentication services, real market data providers, payment flows, and legal/compliance requirements are out of scope unless explicitly introduced later.

Mock data is acceptable, but it must be provided only in data layer and must be realistic, internally consistent, and aligned with these business specifications.
