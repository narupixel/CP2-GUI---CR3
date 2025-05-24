package payrollCalculations;

import models.EmployeeProfile;
import models.TimeLog;
import models.WeeklyTotals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateWeeklyTotals {

    // Aggregates TimeLog entries for an employee into WeeklyTotals per week
    public static List<WeeklyTotals> calculateWeeklyTotals(EmployeeProfile employee, List<TimeLog> allLogs) {
        Map<Integer, List<TimeLog>> weekToLogs = new HashMap<>();
        for (TimeLog log : allLogs) {
            if (log.getEmployeeNumber().equals(employee.getEmployeeNumber())) {
                int week = log.getWeekNumber();
                weekToLogs.computeIfAbsent(week, k -> new ArrayList<>()).add(log);
            }
        }

        List<WeeklyTotals> weeklyTotalsList = new ArrayList<>();
        for (List<TimeLog> logs : weekToLogs.values()) {
            weeklyTotalsList.add(WeeklyTotals.fromTimeLogs(logs));
        }
        return weeklyTotalsList;
    }


    // Updates the pay period start/end dates for a WeeklyTotals object
    private void updatePayPeriodDates(WeeklyTotals weeklyTotal, LocalDate date) {
        if (date.isBefore(weeklyTotal.getPeriodStart())) {
            // Update start date
            try {
                java.lang.reflect.Field field = WeeklyTotals.class.getDeclaredField("periodStart");
                field.setAccessible(true);
                field.set(weeklyTotal, date);
            } catch (Exception ignored) {}
        }
        if (date.isAfter(weeklyTotal.getPeriodEnd())) {
            // Update end date
            try {
                java.lang.reflect.Field field = WeeklyTotals.class.getDeclaredField("periodEnd");
                field.setAccessible(true);
                field.set(weeklyTotal, date);
            } catch (Exception ignored) {}
        }
    }
}