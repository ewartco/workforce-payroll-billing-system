
# Workforce Payroll & Billing System

A standalone Java portfolio project demonstrating payroll processing, client billing, role-based workflows, financial record management, and business process integration.

## Overview

This project models the payroll and billing portion of a workforce staffing process. Approved contractor time is used to calculate contractor compensation and generate a separate client billing amount based on the terms of the contractor's assignment.

The system tracks payroll requests, payroll records, payment records, billing requests, billing records, and invoices while maintaining the relationship between each stage of the workflow.

The project focuses on translating a real-world business process into clearly separated application workflows and data models.

## Business Workflow

```text
Approved Timecard
       ↓
Payroll Specialist
       ↓
Payroll Request
       ↓
Contractor Pay Calculation
       ↓
Payroll & Payment Records
       ↓
Billing Request
       ↓
Billing Analyst
       ↓
Client Billing Calculation
       ↓
Billing Record & Invoice
       ↓
Client Approval Handoff
```

The portfolio version uses a simplified workflow layer so the Payroll and Billing functionality can operate independently from the larger original team application.

## Key Business Rule

Contractor compensation and client billing are intentionally calculated separately.

```text
Contractor Pay = Approved Hours × Contractor Pay Rate

Client Billing = Approved Hours × Client Billing Rate
```

A contractor's pay rate represents the amount paid to the contractor, while the client billing rate represents the amount charged to the client for the same work.

Keeping these values separate allows the application to model the financial relationship between contractor compensation and client billing accurately.

## Features

* Process approved contractor timecards
* Calculate contractor compensation using assignment-specific pay rates
* Create and track payroll records
* Create contractor payment records and payment statuses
* Generate billing requests from processed timecards
* Calculate client invoices using separate billing rates
* Generate and track invoices
* Validate required assignments and submitted hours before processing
* Prevent duplicate invoice generation
* Track workflow and financial record status
* Produce summary reporting for payroll, billing, contractor hours, and invoices
* Provide separate Payroll Specialist and Billing Analyst work areas
* Demonstrate handoffs between functional roles through a simplified workflow queue

## Architecture

The project separates the application into several functional areas:

**Model**
Represents contractors, contracts, assignments, and submitted time.

**Payroll & Billing Records**
Maintains payroll, payment, billing, and invoice information.

**Workflow Requests**
Coordinates the transition from approved time to payroll processing and from payroll processing to client billing.

**Reporting**
Aggregates payroll totals, billing totals, contractor hours, record counts, invoice counts, and pending work.

**User Interface**
Provides separate work areas for Payroll Specialist and Billing Analyst responsibilities.

**Workflow Support**
Provides a lightweight standalone mechanism for routing work between functional roles without relying on the infrastructure of the original team application.

## My Contribution

My work on the original project focused on the Payroll and Billing portion of a larger workforce staffing system.

I designed and implemented the Payroll/Billing domain model, including payroll, payment, billing, and invoice records; workflow request classes; status tracking; reporting functionality; Payroll Specialist and Billing Analyst roles; and their associated Java Swing work areas.

I also implemented the workflow that converts processed time into a billing request, allowing work completed in payroll processing to move into the billing workflow.

During final testing, I refined the system to maintain a clear distinction between contractor compensation and client billing and improved the handling of contractor assignments used during payroll processing.

## Original Project Context

The original INFO 5100 team project modeled a workforce staffing network containing Client, Staffing Agency, Compliance, and Payroll/Billing functions.

This repository isolates the Payroll and Billing functionality that I developed and adapts it into a standalone portfolio project. Supporting classes required to make the module independent of the original team architecture have been recreated specifically for this repository.

## Coursework Disclosure

This project is a portfolio adaptation of original coursework completed for **INFO 5100: Application Engineering & Development at Northeastern University**.

The original course project was developed collaboratively as a team. This repository contains only my original Payroll/Billing work and supporting code and documentation independently created for this portfolio version.

Instructor-provided course materials, assignment instructions, starter or skeleton code, grading feedback, and teammates' source code are not included.

## Technologies

* Java
* Java Swing
* Object-Oriented Programming
* `BigDecimal` for monetary calculations
* Role-based workflow design
* Business process modeling
* Git and GitHub

## Testing Focus

The standalone version tests several important business and workflow behaviors:

* A timecard with zero submitted hours cannot be processed.
* Payroll requires a valid contractor assignment.
* Contractor compensation uses the contractor pay rate.
* Client billing uses the client billing rate.
* Processing payroll creates the corresponding payroll records.
* Completed payroll work can generate a billing request.
* A billing request generates only one invoice.
* Invoice and payment statuses update as the workflow progresses.
* Summary reports accurately aggregate payroll and billing activity.

## What I Learned

This project helped me better understand how business requirements translate into application structure. The most important challenge was maintaining the connection between different parts of the workflow while ensuring that each role operated on the correct records and business rules.

It also reinforced the importance of testing a system across functional boundaries rather than treating each screen or class as an isolated component.
