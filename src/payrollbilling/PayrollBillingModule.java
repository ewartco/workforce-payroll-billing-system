package payrollbilling;

import payrollbilling.record.BillingRecord;
import payrollbilling.record.Invoice;
import payrollbilling.record.PaymentRecord;
import payrollbilling.record.PayrollRecord;
import payrollbilling.report.PayrollReport;
import payrollbilling.request.BillingRequest;
import payrollbilling.request.ContractorPaymentRequest;
import payrollbilling.request.PayrollRequest;
import java.util.ArrayList;
import payrollbilling.request.ClientApprovalRequest;

public class PayrollBillingModule {

    private String moduleName;

    private ArrayList<PayrollRequest> payrollRequests;
    private ArrayList<BillingRequest> billingRequests;
    private ArrayList<ContractorPaymentRequest> contractorPaymentRequests;
    private ArrayList<ClientApprovalRequest> clientApprovalRequests;

    private ArrayList<PayrollRecord> payrollRecords;
    private ArrayList<PaymentRecord> paymentRecords;
    private ArrayList<BillingRecord> billingRecords;
    private ArrayList<Invoice> invoices;
    private ArrayList<PayrollReport> payrollReports;

    public PayrollBillingModule() {
        this.moduleName = "Payroll and Billing Module";

        payrollRequests = new ArrayList<>();
        billingRequests = new ArrayList<>();
        contractorPaymentRequests = new ArrayList<>();
        clientApprovalRequests = new ArrayList<>();

        payrollRecords = new ArrayList<>();
        paymentRecords = new ArrayList<>();
        billingRecords = new ArrayList<>();
        invoices = new ArrayList<>();
        payrollReports = new ArrayList<>();
    }

    public String getModuleName() {
        return moduleName;
    }

    public ArrayList<PayrollRequest> getPayrollRequests() {
        return payrollRequests;
    }

    public ArrayList<BillingRequest> getBillingRequests() {
        return billingRequests;
    }

    public ArrayList<ContractorPaymentRequest> getContractorPaymentRequests() {
        return contractorPaymentRequests;
    }
    
    public ArrayList<ClientApprovalRequest> getClientApprovalRequests() {
        return clientApprovalRequests;
    }

    public ArrayList<PayrollRecord> getPayrollRecords() {
        return payrollRecords;
    }

    public ArrayList<PaymentRecord> getPaymentRecords() {
        return paymentRecords;
    }

    public ArrayList<BillingRecord> getBillingRecords() {
        return billingRecords;
    }

    public ArrayList<Invoice> getInvoices() {
        return invoices;
    }

    public ArrayList<PayrollReport> getPayrollReports() {
        return payrollReports;
    }
}