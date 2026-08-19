package payrollbilling.report;

import workflow.WorkStatus;
import payrollbilling.record.BillingRecord;
import payrollbilling.record.Invoice;
import payrollbilling.record.PayrollRecord;
import payrollbilling.request.PayrollRequest;
import java.math.BigDecimal;
import java.util.ArrayList;

public class PayrollReport {

    private BigDecimal totalPayrollAmount;
    private BigDecimal totalBillingAmount;
    private double totalContractorHours;
    private int payrollRecordsProcessed;
    private int billingRecordsProcessed;
    private int invoicesGenerated;
    private int pendingPayrollRequests;

    public PayrollReport() {
        this.totalPayrollAmount = BigDecimal.ZERO;
        this.totalBillingAmount = BigDecimal.ZERO;
    }

    public void generateSummary(ArrayList<PayrollRecord> payrollRecords,
                                ArrayList<BillingRecord> billingRecords,
                                ArrayList<Invoice> invoices,
                                ArrayList<PayrollRequest> payrollRequests) {

        totalPayrollAmount = BigDecimal.ZERO;
        totalBillingAmount = BigDecimal.ZERO;
        totalContractorHours = 0.0;
        payrollRecordsProcessed = 0;
        billingRecordsProcessed = 0;
        invoicesGenerated = 0;
        pendingPayrollRequests = 0;

        if (payrollRecords != null) {
            payrollRecordsProcessed = payrollRecords.size();

            for (PayrollRecord record : payrollRecords) {
                if (record != null) {
                    totalPayrollAmount = totalPayrollAmount.add(record.getTotalAmount());
                    totalContractorHours += record.getHoursWorked();
                }
            }
        }

        if (billingRecords != null) {
            billingRecordsProcessed = billingRecords.size();

            for (BillingRecord record : billingRecords) {
                if (record != null) {
                    totalBillingAmount = totalBillingAmount.add(record.getInvoiceAmount());
                }
            }
        }

        if (invoices != null) {
            invoicesGenerated = invoices.size();
        }

        if (payrollRequests != null) {
            for (PayrollRequest request : payrollRequests) {
                if (request != null && request.getStatus() == WorkStatus.PENDING) {
                    pendingPayrollRequests++;
                }
            }
        }
    }

    public BigDecimal getTotalPayrollAmount() {
        return totalPayrollAmount;
    }

    public BigDecimal getTotalBillingAmount() {
        return totalBillingAmount;
    }

    public double getTotalContractorHours() {
        return totalContractorHours;
    }

    public int getPayrollRecordsProcessed() {
        return payrollRecordsProcessed;
    }

    public int getBillingRecordsProcessed() {
        return billingRecordsProcessed;
    }

    public int getInvoicesGenerated() {
        return invoicesGenerated;
    }

    public int getPendingPayrollRequests() {
        return pendingPayrollRequests;
    }
}