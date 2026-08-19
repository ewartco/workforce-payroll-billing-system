package payrollbilling.request;

import workflow.WorkItem;
import workflow.WorkStatus;
import payrollbilling.enums.PaymentStatus;
import payrollbilling.record.PaymentRecord;

public class ContractorPaymentRequest extends WorkItem {

    private PaymentRecord paymentRecord;
    private PaymentStatus paymentStatus;

    public ContractorPaymentRequest(PaymentRecord paymentRecord) {
        super();
        this.paymentRecord = paymentRecord;
        this.paymentStatus = PaymentStatus.PENDING;
        setStatus(WorkStatus.PENDING);
    }

    public void confirmPayment() {
        if (paymentRecord == null) {
            throw new IllegalArgumentException("Payment record is required to confirm payment.");
        }

        paymentRecord.processPayment();
        this.paymentStatus = paymentRecord.getPaymentStatus();
        setStatus(WorkStatus.COMPLETED);
    }

    public PaymentRecord getPaymentRecord() {
        return paymentRecord;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}