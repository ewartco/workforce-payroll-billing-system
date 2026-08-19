package workflow;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class WorkItem {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(1000);

    private final int workItemId;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private WorkStatus status;

    protected WorkItem() {
        this.workItemId = ID_SEQUENCE.incrementAndGet();
        this.createdAt = LocalDateTime.now();
        this.status = WorkStatus.PENDING;
    }

    public int getWorkItemId() {
        return workItemId;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setStatus(WorkStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        this.status = status;

        if (status == WorkStatus.COMPLETED
                || status == WorkStatus.REJECTED
                || status == WorkStatus.CANCELLED) {
            this.completedAt = LocalDateTime.now();
        }
    }
}