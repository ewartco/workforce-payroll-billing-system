package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.ContractorAssignment;
import model.Timecard;
import payrollbilling.PayrollBillingModule;
import payrollbilling.record.PaymentRecord;
import payrollbilling.record.PayrollRecord;
import payrollbilling.report.PayrollReport;
import payrollbilling.request.BillingRequest;
import payrollbilling.request.ContractorPaymentRequest;
import payrollbilling.request.PayrollRequest;

public class PayrollSpecialistPanel extends JPanel {

    private final PayrollBillingModule module;
    private final Timecard timecard;
    private final ContractorAssignment assignment;

    private final JTable table = new JTable();
    private final JLabel statusLabel = new JLabel("Ready");

    public PayrollSpecialistPanel(
            PayrollBillingModule module,
            Timecard timecard,
            ContractorAssignment assignment) {

        if (module == null || timecard == null || assignment == null) {
            throw new IllegalArgumentException(
                    "Module, timecard, and assignment are required.");
        }

        this.module = module;
        this.timecard = timecard;
        this.assignment = assignment;

        buildUi();
        showIncomingTimecard();
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Payroll Specialist Work Area");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JLabel subtitle = new JLabel(
                "Review approved time, process contractor payroll, and create billing handoffs.");

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton incomingButton = new JButton("Incoming Timecard");
        JButton processButton = new JButton("Process Payroll");
        JButton payrollButton = new JButton("Payroll Records");
        JButton paymentsButton = new JButton("Payment Records");
        JButton reportButton = new JButton("Payroll Report");

        incomingButton.addActionListener(e -> showIncomingTimecard());
        processButton.addActionListener(e -> processTimecard());
        payrollButton.addActionListener(e -> showPayrollRecords());
        paymentsButton.addActionListener(e -> showPaymentRecords());
        reportButton.addActionListener(e -> showPayrollReport());

        controls.add(incomingButton);
        controls.add(processButton);
        controls.add(payrollButton);
        controls.add(paymentsButton);
        controls.add(reportButton);
        controls.add(statusLabel);

        add(controls, BorderLayout.SOUTH);
    }

    private void showIncomingTimecard() {
        DefaultTableModel model = readOnlyModel(new String[]{
            "Timecard ID",
            "Contractor",
            "Week Ending",
            "Hours",
            "Pay Rate",
            "Status",
            "Summary"
        });

        String status = isProcessed() ? "PROCESSED" : "READY";

        model.addRow(new Object[]{
            timecard.getTimecardId(),
            assignment.getContractor().getFullName(),
            timecard.getWeekEndingDate(),
            timecard.getTotalHours(),
            money(assignment.getContract().getPayRate()),
            status,
            timecard.getWorkSummary()
        });

        table.setModel(model);
        statusLabel.setText("Viewing incoming timecard");
    }

    private void processTimecard() {
        if (isProcessed()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This timecard has already been processed.",
                    "Duplicate Processing Prevented",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (timecard.getTotalHours() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "The timecard must contain more than zero hours.",
                    "Invalid Timecard",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (assignment.getContract() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "The contractor assignment does not have a contract.",
                    "Missing Contract",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            PayrollRequest payrollRequest =
                    new PayrollRequest(timecard, assignment);

            PayrollRecord payrollRecord =
                    payrollRequest.processPayroll();

            PaymentRecord paymentRecord =
                    new PaymentRecord(payrollRecord);

            ContractorPaymentRequest paymentRequest =
                    new ContractorPaymentRequest(paymentRecord);

            paymentRequest.confirmPayment();

            BillingRequest billingRequest =
                    new BillingRequest(timecard, assignment);

            module.getPayrollRequests().add(payrollRequest);
            module.getPayrollRecords().add(payrollRecord);
            module.getPaymentRecords().add(paymentRecord);
            module.getContractorPaymentRequests().add(paymentRequest);
            module.getBillingRequests().add(billingRequest);

            JOptionPane.showMessageDialog(
                    this,
                    "Payroll processed successfully.\n"
                    + "Contractor payment: "
                    + money(payrollRecord.getTotalAmount())
                    + "\nBilling request created for the Billing Analyst.");

            showPayrollRecords();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Payroll Processing Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPayrollRecords() {
        DefaultTableModel model = readOnlyModel(new String[]{
            "Payroll ID",
            "Contractor",
            "Hours",
            "Contractor Pay Rate",
            "Contractor Pay Total",
            "Processed Date",
            "Payment Status"
        });

        for (PayrollRecord record : module.getPayrollRecords()) {
            model.addRow(new Object[]{
                record.getPayrollId(),
                record.getAssignment().getContractor().getFullName(),
                record.getHoursWorked(),
                money(record.getPayRate()),
                money(record.getTotalAmount()),
                record.getProcessedDate(),
                record.getPaymentStatus()
            });
        }

        table.setModel(model);
        statusLabel.setText("Viewing payroll records");
    }

    private void showPaymentRecords() {
        DefaultTableModel model = readOnlyModel(new String[]{
            "Payment ID",
            "Payroll ID",
            "Contractor",
            "Amount",
            "Payment Date",
            "Status"
        });

        for (PaymentRecord record : module.getPaymentRecords()) {
            model.addRow(new Object[]{
                record.getPaymentId(),
                record.getPayrollRecord() == null
                        ? "N/A"
                        : record.getPayrollRecord().getPayrollId(),
                record.getContractor() == null
                        ? "N/A"
                        : record.getContractor().getFullName(),
                money(record.getPaymentAmount()),
                record.getPaymentDate(),
                record.getPaymentStatus()
            });
        }

        table.setModel(model);
        statusLabel.setText("Viewing payment records");
    }

    private void showPayrollReport() {
        PayrollReport report = new PayrollReport();

        report.generateSummary(
                module.getPayrollRecords(),
                module.getBillingRecords(),
                module.getInvoices(),
                module.getPayrollRequests());

        DefaultTableModel model = readOnlyModel(
                new String[]{"Metric", "Value"});

        model.addRow(new Object[]{
            "Total Contractor Hours",
            report.getTotalContractorHours()
        });

        model.addRow(new Object[]{
            "Total Payroll Amount",
            money(report.getTotalPayrollAmount())
        });

        model.addRow(new Object[]{
            "Total Client Billing",
            money(report.getTotalBillingAmount())
        });

        model.addRow(new Object[]{
            "Payroll Records Processed",
            report.getPayrollRecordsProcessed()
        });

        model.addRow(new Object[]{
            "Billing Records Processed",
            report.getBillingRecordsProcessed()
        });

        model.addRow(new Object[]{
            "Invoices Generated",
            report.getInvoicesGenerated()
        });

        model.addRow(new Object[]{
            "Pending Payroll Requests",
            report.getPendingPayrollRequests()
        });

        table.setModel(model);
        statusLabel.setText("Viewing payroll report");
    }

    private boolean isProcessed() {
        for (PayrollRequest request : module.getPayrollRequests()) {
            if (request != null && request.getTimecard() == timecard) {
                return true;
            }
        }

        return false;
    }

    private DefaultTableModel readOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private String money(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }

        return "$" + amount
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}