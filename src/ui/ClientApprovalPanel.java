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
import payrollbilling.enums.ApprovalStatus;
import payrollbilling.request.ClientApprovalRequest;

public class ClientApprovalPanel extends JPanel {

    private final PayrollBillingModule module;

    private final JTable table = new JTable();
    private final JLabel statusLabel = new JLabel("Ready");

    private final List<ClientApprovalRequest> displayedRequests =
            new ArrayList<>();

    public ClientApprovalPanel(PayrollBillingModule module) {

        if (module == null) {
            throw new IllegalArgumentException(
                    "Payroll/Billing module is required.");
        }

        this.module = module;

        buildUi();
        showPendingApprovals();
    }

    private void buildUi() {

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        16, 16, 16, 16));

        JPanel header = new JPanel(new BorderLayout());

        JLabel title =
                new JLabel("Client Approval Work Area");

        title.setFont(
                title.getFont().deriveFont(
                        Font.BOLD, 22f));

        JLabel subtitle =
                new JLabel(
                        "Review submitted invoices and approve or reject billing.");

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
                new JButton("Pending Approvals");

        JButton approveButton =
                new JButton("Approve Invoice");

        JButton rejectButton =
                new JButton("Reject Invoice");

        JButton allButton =
                new JButton("All Reviews");

        pendingButton.addActionListener(
                e -> showPendingApprovals());

        approveButton.addActionListener(
                e -> approveSelected());

        rejectButton.addActionListener(
                e -> rejectSelected());

        allButton.addActionListener(
                e -> showAllReviews());

        controls.add(pendingButton);
        controls.add(approveButton);
        controls.add(rejectButton);
        controls.add(allButton);
        controls.add(statusLabel);

        add(controls, BorderLayout.SOUTH);
    }

    public void refresh() {
        showPendingApprovals();
    }

    private void showPendingApprovals() {

        displayedRequests.clear();

        DefaultTableModel model =
                readOnlyModel(new String[]{
                    "Invoice ID",
                    "Contractor",
                    "Hours",
                    "Invoice Total",
                    "Invoice Status",
                    "Approval Status"
                });

        for (ClientApprovalRequest request
                : module.getClientApprovalRequests()) {

            if (request == null
                    || request.getApprovalStatus()
                    != ApprovalStatus.PENDING) {

                continue;
            }

            displayedRequests.add(request);

            model.addRow(new Object[]{
                request.getInvoice().getInvoiceId(),
                request.getInvoice()
                        .getBillingRecord()
                        .getAssignment()
                        .getContractor()
                        .getFullName(),
                request.getInvoice().getTotalHours(),
                money(
                        request.getInvoice()
                                .getInvoiceTotal()),
                request.getInvoice()
                        .getInvoiceStatus(),
                request.getApprovalStatus()
            });
        }

        table.setModel(model);

        if (displayedRequests.isEmpty()) {
            statusLabel.setText(
                    "No pending approvals");
        } else {
            statusLabel.setText(
                    displayedRequests.size()
                    + " approval request(s) pending");
        }
    }

    private void approveSelected() {

        ClientApprovalRequest request =
                getSelectedRequest();

        if (request == null) {
            return;
        }

        try {

            request.approve();

            JOptionPane.showMessageDialog(
                    this,
                    "Invoice "
                    + request.getInvoice().getInvoiceId()
                    + " approved successfully.");

            showPendingApprovals();

        } catch (IllegalStateException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Approval Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectSelected() {

        ClientApprovalRequest request =
                getSelectedRequest();

        if (request == null) {
            return;
        }

        try {

            request.reject();

            JOptionPane.showMessageDialog(
                    this,
                    "Invoice "
                    + request.getInvoice().getInvoiceId()
                    + " rejected.");

            showPendingApprovals();

        } catch (IllegalStateException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Approval Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private ClientApprovalRequest getSelectedRequest() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0
                || selectedRow >= displayedRequests.size()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select an invoice first.",
                    "No Invoice Selected",
                    JOptionPane.WARNING_MESSAGE);

            return null;
        }

        return displayedRequests.get(selectedRow);
    }

    private void showAllReviews() {

        DefaultTableModel model =
                readOnlyModel(new String[]{
                    "Invoice ID",
                    "Contractor",
                    "Invoice Total",
                    "Invoice Status",
                    "Approval Status"
                });

        for (ClientApprovalRequest request
                : module.getClientApprovalRequests()) {

            model.addRow(new Object[]{
                request.getInvoice().getInvoiceId(),
                request.getInvoice()
                        .getBillingRecord()
                        .getAssignment()
                        .getContractor()
                        .getFullName(),
                money(
                        request.getInvoice()
                                .getInvoiceTotal()),
                request.getInvoice()
                        .getInvoiceStatus(),
                request.getApprovalStatus()
            });
        }

        table.setModel(model);
        displayedRequests.clear();

        statusLabel.setText(
                "Viewing all approval reviews");
    }

    private DefaultTableModel readOnlyModel(
            String[] columns) {

        return new DefaultTableModel(columns, 0) {

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