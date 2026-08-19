package payrollbilling.record;

import payrollbilling.enums.PaymentStatus;
import model.Contractor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentRecord {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(9000);

    private final int paymentId;
    private PayrollRecord payrollRecord;
    private Contractor contractor;
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;

    public PaymentRecord(PayrollRecord payrollRecord) {
        this.paymentId = ID_SEQUENCE.incrementAndGet();
        this.payrollRecord = payrollRecord;
        this.paymentDate = LocalDate.now();
        this.paymentStatus = PaymentStatus.PENDING;

        if (payrollRecord != null) {
            this.paymentAmount = payrollRecord.getTotalAmount();

            if (payrollRecord.getAssignment() != null) {
                this.contractor = payrollRecord.getAssignment().getContractor();
            }
        } else {
            this.paymentAmount = BigDecimal.ZERO;
        }
    }

    public void processPayment() {
        this.paymentStatus = PaymentStatus.PAID;

        if (payrollRecord != null) {
            payrollRecord.markAsPaid();
        }
    }

    public int getPaymentId() {
        return paymentId;
    }

    public PayrollRecord getPayrollRecord() {
        return payrollRecord;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(paymentId);
    }
}