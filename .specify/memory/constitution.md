<!--
Sync Impact Report:
- Version Change: 1.0.0 -> 1.1.0
- Modified Principles:
  - Code Quality & Standards -> Code Quality & Consistency (Expanded with specific BPs)
  - Performance & Scalability (Added DTOs/StringBuilder rules)
- Added Principles:
  - Reliability & Resource Management
  - Security
- Templates Status:
  - .specify/templates/plan-template.md: ✅ (Compatible)
  - .specify/templates/spec-template.md: ✅ (Compatible)
  - .specify/templates/tasks-template.md: ✅ (Compatible)
-->

# Scalable E-Commerce Platform Constitution

This Constitution defines the fundamental architectural and coding principles for the Scalable E-Commerce Platform. Developers MUST adhere to these principles to ensure the system's long-term maintainability, security, and performance.

Specific coding standards referenced as **BPXXX** (e.g., BP201) are detailed in the [Generic Coding Best Practices](../../Generic-Coding-Best-Practices.md) document, which serves as the primary reference for implementation details.

## Core Principles

### I. Code Quality & Consistency
Code must be clean, readable, and free of "traps".
- **Formatting**: Strict adherence to `eclipse-format.xml` and `eclipse.importorder`.
- **Logic**: Prefer for-each loops (BP201), use `.equals()` over `==` (BP204), and minimize variable scope (BP206).
- **Constants**: Avoid hard-coded values (BP207); prefer Enums over integer constants (BP506).
- **Maintenance**: TODOs must include Who, When, Why, and What (BP203). Classes/methods should be `final` unless designed for extension (BP503).

### II. Reliability & Resource Management
The system must be robust against failures and resource leaks.
- **Exceptions**: Never swallow exceptions (empty catch blocks forbidden - BP301). Throw specific, meaningful exceptions with context (BP302, BP303). Always log with appropriate severity (BP305).
- **Resources**: All external resources (streams, connections) MUST be closed in a `finally` block or try-with-resources (BP401, BP304).
- **Transactions**: Single business unit-of-work = Single Database Transaction (BP602). Avoid DDL within transactions (BP601).
- **Assertions**: Do NOT use `assert` for business logic; use standard validation instead (BP202).

### III. Security
Security is baked in, not bolted on.
- **Data Access**: MUST use parameterized queries or ORM criteria to prevent SQL Injection (BP801). String concatenation for SQL is strictly prohibited.
- **Input Validation**: Validate all external inputs at the boundary.

### IV. Performance & Scalability
Designed for high volume and low latency.
- **Communication**: Limit remote invocations between layers; use DTOs to transfer data in bulk (BP701).
- **Optimization**: Use `StringBuilder` for string concatenation inside loops (BP702). Avoid lazy initialization in multi-threaded contexts unless double-checked locking is correctly implemented (BP402, BP403).
- **Architecture**: Services MUST be stateless (BP501), containerized (Docker), and independently scalable.

### V. Comprehensive Testing
Testing is mandatory for all features.
- **Scope**: Unit tests for logic, Integration tests for contracts, E2E for critical journeys.
- **Constraint**: Test code is the ONLY place where `assert` keyword is acceptable (BP202).

## Governance

This Constitution supersedes all other project practices.

### Amendments
Any changes to this constitution require:
1.  A documented reason for the change.
2.  Approval from project maintainers.
3.  A migration plan if the change invalidates existing code or practices.

### Versioning
This document follows Semantic Versioning (MAJOR.MINOR.PATCH).
-   **MAJOR**: Backward incompatible governance or principle changes.
-   **MINOR**: New principles added or material expansion of guidance.
-   **PATCH**: Clarifications, wording fixes.

**Version**: 1.1.0 | **Ratified**: 2026-01-06 | **Last Amended**: 2026-01-06