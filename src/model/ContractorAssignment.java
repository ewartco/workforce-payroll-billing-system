package model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class ContractorAssignment {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(5000);

    private final int assignmentId;
    private final Contractor contractor;
    private final LocalDate startDate;
    private Contract contract;

    public ContractorAssignment(
            Contractor contractor,
            LocalDate startDate) {

        if (contractor == null) {
            throw new IllegalArgumentException("Contractor is required.");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required.");
        }

        this.assignmentId = ID_SEQUENCE.incrementAndGet();
        this.contractor = contractor;
        this.startDate = startDate;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Contract getContract() {
        return contract;
    }

    public void assignContract(Contract contract) {
        this.contract = contract;
    }
}