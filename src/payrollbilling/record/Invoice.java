package payrollbilling.record;

import payrollbilling.enums.InvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class Invoice {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(11000);

    private final int invoiceId;
    private BillingRecord billingRecord;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private LocalDate billingStartDate;
    private LocalDate billingEndDate;
    private double totalHours;
    private BigDecimal invoiceTotal;
    private int timeCardId;
    private InvoiceStatus invoiceStatus;

    public Invoice(BillingRecord billingRecord) {
        this.invoiceId = ID_SEQUENCE.incrementAndGet();
        this.billingRecord = billingRecord;
        this.invoiceDate = LocalDate.now();
        this.dueDate = invoiceDate.plusDays(30);
        this.invoiceStatus = InvoiceStatus.DRAFT;

        if (billingRecord != null) {
            this.totalHours = billingRecord.getHoursBilled();
            this.invoiceTotal = billingRecord.getInvoiceAmount();

            if (billingRecord.getAssignment() != null
                    && billingRecord.getAssignment().getContract() != null) {
                this.billingStartDate = billingRecord.getAssignment().getContract().getStartDate();
                this.billingEndDate = billingRecord.getAssignment().getContract().getEndDate();
            }
        } else {
            this.totalHours = 0.0;
            this.invoiceTotal = BigDecimal.ZERO;
        }
    }

    public Invoice(BillingRecord billingRecord, int timeCardId) {
        this(billingRecord);
        this.timeCardId = timeCardId;
    }

    public void generateInvoice() {
        this.invoiceStatus = InvoiceStatus.SENT;

        if (billingRecord != null) {
            billingRecord.markInvoiced();
        }
    }

    public void markPaid() {
        this.invoiceStatus = InvoiceStatus.PAID;

        if (billingRecord != null) {
            billingRecord.markPaid();
        }
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public BillingRecord getBillingRecord() {
        return billingRecord;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getBillingStartDate() {
        return billingStartDate;
    }

    public LocalDate getBillingEndDate() {
        return billingEndDate;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public int getTimeCardId() {
        return timeCardId;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(invoiceId);
    }
}