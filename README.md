# GoodBudget Automation Framework

A test automation framework for the [GoodBudget](https://goodbudget.com/) application built using:

Tool : Playwright
Language : Java
Test Framework : JUnit
Design Pattern : Page Object Model (POM)

## Project Overview

This framework is designed using the **Page Object Model** pattern to separate page logic from test logic, improving maintainability and readability.

### Key Components

- Page Classes: Represent major application entities such as:
    - `HomePage`
    - `EnvelopesPage`
    - `TransactionsPage`
    - `AccountsPage`
    - `FillEnvelopesPage`
    - `ReportsPage`

- Test Classes:
    - `E2EScenarios` contains the end-to-end test cases.

- Base Class:
    - Handles setup, tearDown, and shared initializations.

- Utilities:
    - Common helper methods and utilities used throughout the tests.

- JsonDataLoader:
    - Loads data from `testdata.json`.

## Parallel Cross-Browser Execution

The framework supports parallel execution across Chromium, Firefox, and WebKit.
Note: Due to user-specific shared data in GoodBudget, it's recommended to run tests on one browser at a time unless multiple users are configured.

## Test Scenarios

### 1. e2eScenario1
Covers the full flow:
- Adding funds
- Creating envelopes
- Filling envelopes
- Performing transactions (single & multiple)
- Generating reports
- Validations for financial calculations

### 2. e2eScenario2
Focuses on:
- Overspending
- Fund transfers between envelopes
- Deleting envelopes
- Validating balance adjustments

### 3. validationChecks
Includes:
- Envelope name validations
- Overdraft restrictions
- Scheduling logic
- Overfill validations

## Data Reset

Each scenario includes a `resetApplication()` step to:
- Clear all transactions
- Reset envelope states
- Ensure data consistency across runs

---

## Extendability

The framework is:
- Modular: New pages and test cases can be added easily.
- Maintainable: Clear separation of test logic and page interactions.

---

## Notes & Future Improvements

- Incorporate better **synchronization** (e.g. using Playwright waits more efficiently)
- Refactor code to more strongly follow **SOLID** and **OOP** principles
- Improve test **scalability** with richer data handling and dynamic assertions



---


