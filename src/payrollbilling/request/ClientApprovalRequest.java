package payrollbilling.request;

import payrollbilling.enums.ApprovalStatus;
import payrollbilling.record.Invoice;
import workflow.WorkItem;
import workflow.WorkStatus;

public class ClientApprovalRequest extends WorkItem {

    private final Invoice invoice;
    private ApprovalStatus approvalStatus;

    public ClientApprovalRequest(Invoice invoice) {

        if (invoice == null) {
            throw new IllegalArgumentException(
                    "Invoice is required for approval.");
        }

        this.invoice = invoice;
        this.approvalStatus = ApprovalStatus.PENDING;

        setStatus(WorkStatus.PENDING);
    }

    public void approve() {

        if (approvalStatus != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "This invoice has already been reviewed.");
        }

        approvalStatus = ApprovalStatus.APPROVED;
        setStatus(WorkStatus.COMPLETED);
    }

    public void reject() {

        if (approvalStatus != ApprovalStatus.PENDING) {
            throw new IllegalStateException(
                    "This invoice has already been reviewed.");
        }

        approvalStatus = ApprovalStatus.REJECTED;
        setStatus(WorkStatus.REJECTED);
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}