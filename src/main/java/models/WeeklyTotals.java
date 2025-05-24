package models;

import java.time.LocalDate;
import java.util.List;

public class WeeklyTotals {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private int weekNumber;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double totalHoursWorked;
    private double totalOvertime;

    public WeeklyTotals(String employeeNumber, String lastName, String firstName, int weekNumber,
                        LocalDate periodStart, LocalDate periodEnd,
                        double totalHoursWorked, double totalOvertime) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.weekNumber = weekNumber;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalHoursWorked = totalHoursWorked;
        this.totalOvertime = totalOvertime;
    }

    // Factory method to aggregate from a list of TimeLog entries
    public static WeeklyTotals fromTimeLogs(List<TimeLog> logs) {
        if (logs == null || logs.isEmpty()) return null;
        String employeeNumber = logs.get(0).getEmployeeNumber();
        String lastName = logs.get(0).getLastName();
        String firstName = logs.get(0).getFirstName();
        int weekNumber = logs.get(0).getWeekNumber();
        LocalDate periodStart = logs.stream().map(TimeLog::getDate).min(LocalDate::compareTo).orElse(null);
        LocalDate periodEnd = logs.stream().map(TimeLog::getDate).max(LocalDate::compareTo).orElse(null);
        double totalHours = logs.stream().mapToDouble(TimeLog::getHoursWorked).sum();
        double totalOvertime = logs.stream().mapToDouble(TimeLog::getOvertime).sum();
        return new WeeklyTotals(employeeNumber, lastName, firstName, weekNumber, periodStart, periodEnd, totalHours, totalOvertime);
    }

    // Getters
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public int getWeekNumber() { return weekNumber; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public double getTotalHoursWorked() { return totalHoursWorked; }
    public double getTotalOvertime() { return totalOvertime; }

    @Override
    public String toString() {
        return String.format("%s %s (%s) - Week %d [%s to %s]: Total Hours = %.2f, Overtime = %.2f",
                firstName, lastName, employeeNumber, weekNumber, periodStart, periodEnd, totalHoursWorked, totalOvertime);
    }
}