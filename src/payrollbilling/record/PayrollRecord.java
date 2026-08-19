package payrollbilling.record;

import payrollbilling.enums.PaymentStatus;
import model.Contract;
import model.ContractorAssignment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class PayrollRecord {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(7000);

    private final int payrollId;
    private ContractorAssignment assignment;
    private double hoursWorked;
    private BigDecimal payRate;
    private BigDecimal totalAmount;
    private LocalDate processedDate;
    private PaymentStatus paymentStatus;

    public PayrollRecord(ContractorAssignment assignment, double hoursWorked) {
        if (hoursWorked <= 0) {
            throw new IllegalArgumentException("Hours worked must be greater than zero.");
        }

        this.payrollId = ID_SEQUENCE.incrementAndGet();
        this.assignment = assignment;
        this.hoursWorked = hoursWorked;
        this.paymentStatus = PaymentStatus.PENDING;
        this.processedDate = LocalDate.now();

        if (assignment != null && assignment.getContract() != null) {
            Contract contract = assignment.getContract();
            this.payRate = contract.getPayRate();
        } else {
            this.payRate = BigDecimal.ZERO;
        }

        this.totalAmount = calculatePay();
    }

    public BigDecimal calculatePay() {
        if (payRate == null) {
            return BigDecimal.ZERO;
        }

        return payRate.multiply(BigDecimal.valueOf(hoursWorked));
    }

    public void markAsProcessing() {
        this.paymentStatus = PaymentStatus.PROCESSING;
    }

    public void markAsPaid() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public int getPayrollId() {
        return payrollId;
    }

    public ContractorAssignment getAssignment() {
        return assignment;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getProcessedDate() {
        return processedDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return String.valueOf(payrollId);
    }
}