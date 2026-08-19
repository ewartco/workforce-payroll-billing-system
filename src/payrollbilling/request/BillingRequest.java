package payrollbilling.request;

import payrollbilling.record.BillingRecord;
import payrollbilling.record.Invoice;
import workflow.WorkItem;
import workflow.WorkStatus;
import model.Timecard;
import model.ContractorAssignment;

public class BillingRequest extends WorkItem {

    private Timecard timecard;
    private ContractorAssignment assignment;
    private BillingRecord billingRecord;
    private Invoice invoice;

    public BillingRequest(Timecard timecard, ContractorAssignment assignment) {
        super();
        this.timecard = timecard;
        this.assignment = assignment;
        setStatus(WorkStatus.PENDING);
    }

    public Invoice processBilling() {
        validateReadyToProcess();

        this.billingRecord = new BillingRecord(assignment, timecard.getTotalHours());
        this.invoice = new Invoice(billingRecord, timecard.getTimecardId());
        this.invoice.generateInvoice();

        setStatus(WorkStatus.COMPLETED);
        return invoice;
    }

    private void validateReadyToProcess() {
        if (timecard == null) {
            throw new IllegalArgumentException("Timecard is required to process billing.");
        }

        if (assignment == null) {
            throw new IllegalArgumentException("Contractor assignment is required to process billing.");
        }

        if (timecard.getTotalHours() <= 0) {
            throw new IllegalArgumentException("Timecard must have more than zero hours.");
        }
    }

    public Timecard getTimecard() {
        return timecard;
    }

    public ContractorAssignment getAssignment() {
        return assignment;
    }

    public double getHoursBilled() {
        return timecard == null ? 0.0 : timecard.getTotalHours();
    }

    public BillingRecord getBillingRecord() {
        return billingRecord;
    }

    public Invoice getInvoice() {
        return invoice;
    }
}