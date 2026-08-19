package payrollbilling.record;

import payrollbilling.enums.BillingStatus;
import model.Contract;
import model.ContractorAssignment;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class BillingRecord {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(10000);

    private final int billingId;
    private ContractorAssignment assignment;
    private double hoursBilled;
    private BigDecimal billRate;
    private BigDecimal invoiceAmount;
    private BillingStatus billingStatus;

    public BillingRecord(ContractorAssignment assignment, double hoursBilled) {
        if (hoursBilled <= 0) {
            throw new IllegalArgumentException("Hours billed must be greater than zero.");
        }

        this.billingId = ID_SEQUENCE.incrementAndGet();
        this.assignment = assignment;
        this.hoursBilled = hoursBilled;
        this.billingStatus = BillingStatus.PENDING;

        if (assignment != null && assignment.getContract() != null) {
            Contract contract = assignment.getContract();
            this.billRate = contract.getBillRate();
        } else {
            this.billRate = BigDecimal.ZERO;
        }

        this.invoiceAmount = calculateInvoiceAmount();
    }

    public BigDecimal calculateInvoiceAmount() {
        if (billRate == null) {
            return BigDecimal.ZERO;
        }

        return billRate.multiply(BigDecimal.valueOf(hoursBilled));
    }

    public void markInvoiced() {
        this.billingStatus = BillingStatus.INVOICED;
    }

    public void markPaid() {
        this.billingStatus = BillingStatus.PAID;
    }

    public int getBillingId() {
        return billingId;
    }

    public ContractorAssignment getAssignment() {
        return assignment;
    }

    public double getHoursBilled() {
        return hoursBilled;
    }

    public BigDecimal getBillRate() {
        return billRate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public BillingStatus getBillingStatus() {
        return billingStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(billingId);
    }
}