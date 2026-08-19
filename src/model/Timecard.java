package model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Timecard {

    private static final AtomicInteger ID_SEQUENCE =
            new AtomicInteger(3000);

    private final int timecardId;

    private LocalDate weekEndingDate;
    private double[] dailyHours;
    private String workSummary;

    public Timecard(LocalDate weekEndingDate) {

        if (weekEndingDate == null) {
            throw new IllegalArgumentException(
                    "Week ending date is required.");
        }

        this.timecardId = ID_SEQUENCE.incrementAndGet();
        this.weekEndingDate = weekEndingDate;
        this.dailyHours = new double[7];
    }

    public int getTimecardId() {
        return timecardId;
    }

    public LocalDate getWeekEndingDate() {
        return weekEndingDate;
    }

    public void setWeekEndingDate(LocalDate weekEndingDate) {
        this.weekEndingDate = weekEndingDate;
    }

    public String getWorkSummary() {
        return workSummary;
    }

    public void setWorkSummary(String workSummary) {
        this.workSummary = workSummary;
    }

    public double[] getDailyHours() {
        return Arrays.copyOf(
                dailyHours,
                dailyHours.length);
    }

    public void setDailyHours(double[] dailyHours) {

        if (dailyHours == null || dailyHours.length != 7) {
            throw new IllegalArgumentException(
                    "Timecard must contain exactly 7 days.");
        }

        for (double hours : dailyHours) {
            if (hours < 0) {
                throw new IllegalArgumentException(
                        "Daily hours cannot be negative.");
            }
        }

        this.dailyHours =
                Arrays.copyOf(
                        dailyHours,
                        dailyHours.length);
    }

    public double getTotalHours() {

        double total = 0.0;

        for (double hours : dailyHours) {
            total += hours;
        }

        return total;
    }
}