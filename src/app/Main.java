package app;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.Contract;
import model.Contractor;
import model.ContractorAssignment;
import model.Timecard;

import payrollbilling.PayrollBillingModule;
import payrollbilling.record.Invoice;
import payrollbilling.record.PaymentRecord;
import payrollbilling.record.PayrollRecord;
import payrollbilling.report.PayrollReport;
import payrollbilling.request.BillingRequest;
import payrollbilling.request.ContractorPaymentRequest;
import payrollbilling.request.PayrollRequest;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== Workforce Payroll & Billing Demo ===");
        System.out.println();

        // 1. Create the payroll/billing module
        PayrollBillingModule module = new PayrollBillingModule();

        // 2. Create a contractor
        Contractor contractor = new Contractor(
                "Jordan",
                "Lee",
                "jordan.lee@example.com"
        );

        // 3. Assign the contractor to work
        ContractorAssignment assignment =
                new ContractorAssignment(
                        contractor,
                        LocalDate.of(2026, 8, 17)
                );

        // 4. Create the contract
        // Contractor earns $40/hour.
        // Client is billed $65/hour.
        Contract contract = new Contract(
                assignment,
                LocalDate.of(2026, 8, 17),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("40.00"),
                new BigDecimal("65.00")
        );

        // 5. Create an approved 40-hour timecard
        Timecard timecard =
                new Timecard(LocalDate.of(2026, 8, 23));

        timecard.setDailyHours(
                new double[]{
                    8, 8, 8, 8, 8, 0, 0
                }
        );

        timecard.setWorkSummary(
                "Software implementation support"
        );

        System.out.println(
                "Contractor: "
                + contractor.getFullName()
        );

        System.out.println(
                "Approved Hours: "
                + timecard.getTotalHours()
        );

        System.out.printf(
                "Contractor Pay Rate: $%,.2f/hour%n",
                contract.getPayRate()
        );

        System.out.printf(
                "Client Billing Rate: $%,.2f/hour%n",
                contract.getBillRate()
        );

        System.out.println();

        // 6. PAYROLL WORKFLOW

        PayrollRequest payrollRequest =
                new PayrollRequest(
                        timecard,
                        assignment
                );

        module.getPayrollRequests().add(payrollRequest);

        PayrollRecord payrollRecord =
                payrollRequest.processPayroll();

        module.getPayrollRecords().add(payrollRecord);

        PaymentRecord paymentRecord =
                new PaymentRecord(payrollRecord);

        module.getPaymentRecords().add(paymentRecord);

        ContractorPaymentRequest paymentRequest =
                new ContractorPaymentRequest(paymentRecord);

        module.getContractorPaymentRequests()
                .add(paymentRequest);

        paymentRequest.confirmPayment();

        System.out.println("--- Payroll Processing ---");

        System.out.printf(
                "Contractor Pay: $%,.2f%n",
                payrollRecord.getTotalAmount()
        );

        System.out.println(
                "Payment Status: "
                + paymentRecord.getPaymentStatus()
        );

        System.out.println();

        // 7. BILLING WORKFLOW

        BillingRequest billingRequest =
                new BillingRequest(
                        timecard,
                        assignment
                );

        module.getBillingRequests().add(billingRequest);

        Invoice invoice =
                billingRequest.processBilling();

        module.getBillingRecords()
                .add(billingRequest.getBillingRecord());

        module.getInvoices().add(invoice);

        System.out.println("--- Client Billing ---");

        System.out.printf(
                "Client Invoice: $%,.2f%n",
                invoice.getInvoiceTotal()
        );

        System.out.println(
                "Invoice Status: "
                + invoice.getInvoiceStatus()
        );

        System.out.println();

        // 8. GENERATE SUMMARY REPORT

        PayrollReport report = new PayrollReport();

        report.generateSummary(
                module.getPayrollRecords(),
                module.getBillingRecords(),
                module.getInvoices(),
                module.getPayrollRequests()
        );

        module.getPayrollReports().add(report);

        System.out.println("--- Summary Report ---");

        System.out.printf(
                "Total Payroll: $%,.2f%n",
                report.getTotalPayrollAmount()
        );

        System.out.printf(
                "Total Client Billing: $%,.2f%n",
                report.getTotalBillingAmount()
        );

        System.out.println(
                "Total Contractor Hours: "
                + report.getTotalContractorHours()
        );

        System.out.println(
                "Invoices Generated: "
                + report.getInvoicesGenerated()
        );

        System.out.println();

        System.out.println(
                "=== Workflow Completed Successfully ==="
        );
    }
}