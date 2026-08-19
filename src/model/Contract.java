package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Contract {

    private final ContractorAssignment assignment;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private final BigDecimal payRate;
    private final BigDecimal billRate;

    public Contract(
            ContractorAssignment assignment,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal payRate,
            BigDecimal billRate) {

        if (assignment == null) {
            throw new IllegalArgumentException("Assignment is required.");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Contract dates are required.");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date.");
        }

        if (payRate == null || payRate.signum() < 0) {
            throw new IllegalArgumentException(
                    "Pay rate must be non-negative.");
        }

        if (billRate == null || billRate.signum() < 0) {
            throw new IllegalArgumentException(
                    "Bill rate must be non-negative.");
        }

        if (billRate.compareTo(payRate) < 0) {
            throw new IllegalArgumentException(
                    "Bill rate cannot be lower than pay rate.");
        }

        this.assignment = assignment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payRate = payRate;
        this.billRate = billRate;

        assignment.assignContract(this);
    }

    public ContractorAssignment getAssignment() {
        return assignment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public BigDecimal getBillRate() {
        return billRate;
    }
}