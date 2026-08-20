package portfolio;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.Contract;
import model.Contractor;
import model.ContractorAssignment;
import model.Timecard;

import org.junit.Before;
import org.junit.Test;

import payrollbilling.PayrollBillingModule;
import payrollbilling.enums.ApprovalStatus;
import payrollbilling.record.Invoice;
import payrollbilling.record.PayrollRecord;
import payrollbilling.report.PayrollReport;
import payrollbilling.request.BillingRequest;
import payrollbilling.request.ClientApprovalRequest;
import payrollbilling.request.PayrollRequest;

import static org.junit.Assert.*;

public class WorkflowTest {

    private PayrollBillingModule module;
    private Contractor contractor;
    private ContractorAssignment assignment;
    private Timecard timecard;

    @Before
    public void setUp() {

        module = new PayrollBillingModule();

        contractor = new Contractor(
                "Jordan",
                "Lee",
                "jordan.lee@example.com"
        );

        assignment = new ContractorAssignment(
                contractor,
                LocalDate.of(2026, 8, 17)
        );

        new Contract(
                assignment,
                LocalDate.of(2026, 8, 17),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("40.00"),
                new BigDecimal("65.00")
        );

        timecard = new Timecard(
                LocalDate.of(2026, 8, 23)
        );

        timecard.setDailyHours(
                new double[]{
                    8, 8, 8, 8, 8, 0, 0
                }
        );
    }

    @Test
    public void contractorPayUsesPayRate() {

        PayrollRequest request =
                new PayrollRequest(timecard, assignment);

        PayrollRecord record =
                request.processPayroll();

        assertEquals(
                0,
                new BigDecimal("1600.00")
                        .compareTo(record.getTotalAmount())
        );
    }

    @Test
    public void clientBillingUsesBillingRate() {

        BillingRequest request =
                new BillingRequest(timecard, assignment);

        Invoice invoice =
                request.processBilling();

        assertEquals(
                0,
                new BigDecimal("2600.00")
                        .compareTo(invoice.getInvoiceTotal())
        );
    }

    @Test
    public void payAndBillingAmountsRemainSeparate() {

        PayrollRequest payrollRequest =
                new PayrollRequest(timecard, assignment);

        PayrollRecord payrollRecord =
                payrollRequest.processPayroll();

        BillingRequest billingRequest =
                new BillingRequest(timecard, assignment);

        Invoice invoice =
                billingRequest.processBilling();

        assertEquals(
                0,
                new BigDecimal("1600.00")
                        .compareTo(payrollRecord.getTotalAmount())
        );

        assertEquals(
                0,
                new BigDecimal("2600.00")
                        .compareTo(invoice.getInvoiceTotal())
        );

        assertNotEquals(
                payrollRecord.getTotalAmount(),
                invoice.getInvoiceTotal()
        );
    }

    @Test
    public void invoiceCanMoveThroughClientApproval() {

        BillingRequest billingRequest =
                new BillingRequest(timecard, assignment);

        Invoice invoice =
                billingRequest.processBilling();

        ClientApprovalRequest approvalRequest =
                new ClientApprovalRequest(invoice);

        assertEquals(
                ApprovalStatus.PENDING,
                approvalRequest.getApprovalStatus()
        );

        approvalRequest.approve();

        assertEquals(
                ApprovalStatus.APPROVED,
                approvalRequest.getApprovalStatus()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeTimecardHoursAreRejected() {

        timecard.setDailyHours(
                new double[]{
                    8, 8, -4, 8, 8, 0, 0
                }
        );
    }

    @Test
    public void summaryReportTracksPayrollAndBilling() {

        PayrollRequest payrollRequest =
                new PayrollRequest(timecard, assignment);

        PayrollRecord payrollRecord =
                payrollRequest.processPayroll();

        module.getPayrollRequests().add(payrollRequest);
        module.getPayrollRecords().add(payrollRecord);

        BillingRequest billingRequest =
                new BillingRequest(timecard, assignment);

        Invoice invoice =
                billingRequest.processBilling();

        module.getBillingRequests().add(billingRequest);

        module.getBillingRecords().add(
                billingRequest.getBillingRecord());

        module.getInvoices().add(invoice);

        PayrollReport report =
                new PayrollReport();

        report.generateSummary(
                module.getPayrollRecords(),
                module.getBillingRecords(),
                module.getInvoices(),
                module.getPayrollRequests()
        );

        assertEquals(
                0,
                new BigDecimal("1600.00")
                        .compareTo(
                                report.getTotalPayrollAmount())
        );

        assertEquals(
                0,
                new BigDecimal("2600.00")
                        .compareTo(
                                report.getTotalBillingAmount())
        );

        assertEquals(
                40.0,
                report.getTotalContractorHours(),
                0.001
        );

        assertEquals(
                1,
                report.getInvoicesGenerated()
        );
    }
}