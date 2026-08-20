package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import payrollbilling.PayrollBillingModule;
import payrollbilling.record.BillingRecord;
import payrollbilling.record.Invoice;
import payrollbilling.request.BillingRequest;

public class BillingAnalystPanel extends JPanel {

    private final PayrollBillingModule module;

    private final JTable table = new JTable();
    private final JLabel statusLabel = new JLabel("Ready");

    private final List<BillingRequest> displayedRequests =
            new ArrayList<>();

    public BillingAnalystPanel(PayrollBillingModule module) {

        if (module == null) {
            throw new IllegalArgumentException(
                    "Payroll/Billing module is required.");
        }

        this.module = module;

        buildUi();
        showPendingBilling();
    }

    private void buildUi() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        16, 16, 16, 16));

        JPanel header =
                new JPanel(new BorderLayout());

        JLabel title =
                new JLabel("Billing Analyst Work Area");

        title.setFont(
                title.getFont().deriveFont(
                        Font.BOLD, 22f));

        JLabel subtitle =
                new JLabel(
                        "Review payroll handoffs, calculate client billing, and generate invoices.");

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        add(
                new JScrollPane(table),
                BorderLayout.CENTER);

        JPanel controls =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT));

        JButton pendingButton =
                new JButton("Pending Billing");

        JButton invoiceButton =
                new JButton("Generate Invoice");

        JButton billingRecordsButton =
                new JButton("Billing Records");

        JButton invoicesButton =
                new JButton("Invoices");

        pendingButton.addActionListener(
                e -> showPendingBilling());

        invoiceButton.addActionListener(
                e -> processSelectedBillingRequest());

        billingRecordsButton.addActionListener(
                e -> showBillingRecords());

        invoicesButton.addActionListener(
                e -> showInvoices());

        controls.add(pendingButton);
        controls.add(invoiceButton);
        controls.add(billingRecordsButton);
        controls.add(invoicesButton);
        controls.add(statusLabel);

        add(controls, BorderLayout.SOUTH);
    }

    public void refresh() {
        showPendingBilling();
    }

    private void showPendingBilling() {

        displayedRequests.clear();

        DefaultTableModel model =
                readOnlyModel(new String[]{
                    "Timecard ID",
                    "Contractor",
                    "Hours",
                    "Client Billing Rate",
                    "Expected Invoice",
                    "Status"
                });

        for (BillingRequest request
                : module.getBillingRequests()) {

            if (request == null) {
                continue;
            }

            if (request.getInvoice() != null) {
                continue;
            }

            displayedRequests.add(request);

            BigDecimal billRate =
                    request.getAssignment()
                            .getContract()
                            .getBillRate();

            BigDecimal expectedInvoice =
                    billRate.multiply(
                            BigDecimal.valueOf(
                                    request.getHoursBilled()));

            model.addRow(new Object[]{
                request.getTimecard().getTimecardId(),
                request.getAssignment()
                        .getContractor()
                        .getFullName(),
                request.getHoursBilled(),
                money(billRate),
                money(expectedInvoice),
                request.getStatus()
            });
        }

        table.setModel(model);

        if (displayedRequests.isEmpty()) {
            statusLabel.setText(
                    "No pending billing requests");
        } else {
            statusLabel.setText(
                    displayedRequests.size()
                    + " billing request(s) pending");
        }
    }

    private void processSelectedBillingRequest() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow < 0
                || selectedRow
                >= displayedRequests.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a pending billing request first.",
                    "No Request Selected",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        BillingRequest request =
                displayedRequests.get(selectedRow);

        if (request.getInvoice() != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "An invoice has already been generated for this request.",
                    "Duplicate Invoice Prevented",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {

            Invoice invoice =
                    request.processBilling();

            BillingRecord billingRecord =
                    request.getBillingRecord();

            if (!module.getBillingRecords()
                    .contains(billingRecord)) {

                module.getBillingRecords()
                        .add(billingRecord);
            }

            if (!module.getInvoices()
                    .contains(invoice)) {

                module.getInvoices()
                        .add(invoice);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Invoice generated successfully.\n"
                    + "Client billing amount: "
                    + money(invoice.getInvoiceTotal())
                    + "\nInvoice status: "
                    + invoice.getInvoiceStatus());

            showInvoices();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Billing Processing Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBillingRecords() {

        DefaultTableModel model =
                readOnlyModel(new String[]{
                    "Billing ID",
                    "Contractor",
                    "Hours",
                    "Client Billing Rate",
                    "Invoice Amount",
                    "Status"
                });

        for (BillingRecord record
                : module.getBillingRecords()) {

            model.addRow(new Object[]{
                record.getBillingId(),
                record.getAssignment()
                        .getContractor()
                        .getFullName(),
                record.getHoursBilled(),
                money(record.getBillRate()),
                money(record.getInvoiceAmount()),
                record.getBillingStatus()
            });
        }

        table.setModel(model);

        statusLabel.setText(
                "Viewing billing records");
    }

    private void showInvoices() {

        DefaultTableModel model =
                readOnlyModel(new String[]{
                    "Invoice ID",
                    "Timecard ID",
                    "Hours",
                    "Invoice Total",
                    "Invoice Date",
                    "Due Date",
                    "Status"
                });

        for (Invoice invoice
                : module.getInvoices()) {

            model.addRow(new Object[]{
                invoice.getInvoiceId(),
                invoice.getTimeCardId(),
                invoice.getTotalHours(),
                money(invoice.getInvoiceTotal()),
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                invoice.getInvoiceStatus()
            });
        }

        table.setModel(model);

        statusLabel.setText(
                "Viewing generated invoices");
    }

    private DefaultTableModel readOnlyModel(
            String[] columns) {

        return new DefaultTableModel(
                columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };
    }

    private String money(BigDecimal amount) {

        if (amount == null) {
            return "$0.00";
        }

        return "$"
                + amount
                        .setScale(
                                2,
                                RoundingMode.HALF_UP)
                        .toPlainString();
    }
}