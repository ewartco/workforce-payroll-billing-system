package app;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.Contract;
import model.Contractor;
import model.ContractorAssignment;
import model.Timecard;
import payrollbilling.PayrollBillingModule;
import ui.PayrollSpecialistPanel;

public class PortfolioApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            PayrollBillingModule module =
                    new PayrollBillingModule();

            Contractor contractor =
                    new Contractor(
                            "Jordan",
                            "Lee",
                            "jordan.lee@example.com");

            ContractorAssignment assignment =
                    new ContractorAssignment(
                            contractor,
                            LocalDate.of(2026, 8, 17));

            new Contract(
                    assignment,
                    LocalDate.of(2026, 8, 17),
                    LocalDate.of(2026, 12, 31),
                    new BigDecimal("40.00"),
                    new BigDecimal("65.00"));

            Timecard timecard =
                    new Timecard(
                            LocalDate.of(2026, 8, 23));

            timecard.setDailyHours(
                    new double[]{
                        8, 8, 8, 8, 8, 0, 0
                    });

            timecard.setWorkSummary(
                    "Software implementation support");

            JFrame frame =
                    new JFrame(
                            "Workforce Payroll & Billing System");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE);

            frame.setContentPane(
                    new PayrollSpecialistPanel(
                            module,
                            timecard,
                            assignment));

            frame.setSize(1100, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}