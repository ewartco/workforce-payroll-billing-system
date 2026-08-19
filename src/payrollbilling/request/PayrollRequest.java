package payrollbilling.request;

import payrollbilling.record.PayrollRecord;
import workflow.WorkItem;
import workflow.WorkStatus;
import model.Timecard;
import model.ContractorAssignment;
import java.time.LocalDate;

public class PayrollRequest extends WorkItem {

    private Timecard timecard;
    private ContractorAssignment assignment;
    private PayrollRecord payrollRecord;

    public PayrollRequest(Timecard timecard, ContractorAssignment assignment) {
        super();
        this.timecard = timecard;
        this.assignment = assignment;
        setStatus(WorkStatus.PENDING);
    }

    // Backward-compatible constructor in case older code still passes raw hours.
    public PayrollRequest(ContractorAssignment assignment, double hoursWorked) {
        super();
        this.assignment = assignment;
        setStatus(WorkStatus.PENDING);

        Timecard tempTimecard = new Timecard(LocalDate.now());
        tempTimecard.setDailyHours(new double[]{hoursWorked, 0, 0, 0, 0, 0, 0});
        this.timecard = tempTimecard;
    }

    public PayrollRecord processPayroll() {
        validateReadyToProcess();

        this.payrollRecord = new PayrollRecord(assignment, timecard.getTotalHours());
        setStatus(WorkStatus.COMPLETED);
        return payrollRecord;
    }

    private void validateReadyToProcess() {
        if (timecard == null) {
            throw new IllegalArgumentException("Timecard is required to process payroll.");
        }

        if (assignment == null) {
            throw new IllegalArgumentException("Contractor assignment is required to process payroll.");
        }

        if (timecard.getTotalHours() <= 0) {
            throw new IllegalArgumentException("Timecard must have more than zero hours.");
        }
    }

    public Timecard getTimecard() {
        return timecard;
    }

    public ContractorAssignment getAssignment() {
        return assignment;
    }

    public double getHoursWorked() {
        return timecard == null ? 0.0 : timecard.getTotalHours();
    }

    public PayrollRecord getPayrollRecord() {
        return payrollRecord;
    }
}