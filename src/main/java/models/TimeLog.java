package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

public class TimeLog {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private LocalDate date;
    private LocalTime logIn;
    private LocalTime logOut;
    private double hoursWorked;
    private double overtime;
    private int weekNumber;
    private boolean lateLoginDeduction;

    // Constants
    private static final LocalTime STANDARD_LOGIN = LocalTime.of(8, 0);
    private static final LocalTime GRACE_PERIOD = LocalTime.of(8, 10);

    public TimeLog(String employeeNumber, String lastName, String firstName, LocalDate date,
                   LocalTime logIn, LocalTime logOut) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        this.hoursWorked = calculateHoursWorked();
        this.overtime = calculateOvertime();
        this.weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        this.lateLoginDeduction = isLateLogin();
    }

    private double calculateHoursWorked() {
        Duration worked = Duration.between(logIn, logOut);
        return worked.toMinutes() / 60.0;
    }

    private double calculateOvertime() {
        return Math.max(0, hoursWorked - 8.0);
    }

    private boolean isLateLogin() {
        return logIn.isAfter(GRACE_PERIOD);
    }

    // Getters
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public LocalDate getDate() { return date; }
    public LocalTime getLogIn() { return logIn; }
    public LocalTime getLogOut() { return logOut; }
    public double getHoursWorked() { return hoursWorked; }
    public double getOvertime() { return overtime; }
    public int getWeekNumber() { return weekNumber; }
    public boolean hasLateLoginDeduction() { return lateLoginDeduction; }

    // For displaying the record
    @Override
    public String toString() {
        return String.format("%s %s (%s) worked on %s [Week %d]: %.2f hours | Overtime: %.2f hours | Late Deduction: %s",
                firstName, lastName, employeeNumber, date, weekNumber, hoursWorked, overtime,
                lateLoginDeduction ? "YES" : "NO");
    }

    // Example usage: parsing from a TSV line
    public static TimeLog fromTsvLine(String tsvLine, DateTimeFormatter dateFormatter) {
        String[] fields = tsvLine.split("\t");
        if (fields.length < 6) return null;
        String empId = fields[0];
        String lastName = fields[1];
        String firstName = fields[2];
        String dateStr = fields[3];
        String logIn = fields[4];
        String logOut = fields[5];
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        LocalTime inTime = LocalTime.parse(logIn.length() == 4 ? "0" + logIn : logIn);
        LocalTime outTime = LocalTime.parse(logOut.length() == 4 ? "0" + logOut : logOut);
        return new TimeLog(empId, lastName, firstName, date, inTime, outTime);
    }

    public static void main(String[] args) {
        String fileName = "src/main/resources/Employee Attendance Record.tsv";
        String line;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Read header
            br.readLine();
            while ((line = br.readLine()) != null) {
                TimeLog record = TimeLog.fromTsvLine(line, dateFormatter);
                if (record != null) {
                    System.out.println(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

