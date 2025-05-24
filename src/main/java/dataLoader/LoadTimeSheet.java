package dataLoader;

import models.TimeLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading time sheet data for a specific employee from an attendance record file.
 * It also contains constants related to date/time formatting, store opening time, and grace period for logins.
 */
public class LoadTimeSheet {

    // Constants for date/time formatting and attendance rules
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");
    public static final LocalTime STANDARD_LOGIN = LocalTime.of(8, 0);
    public static final LocalTime GRACE_PERIOD = LocalTime.of(8, 10);

    /**
     * Loads all time logs for a specific employee from the attendance record file.
     * @param filePath Path to the attendance record file (TSV).
     * @param employeeNumber The employee number to filter by.
     * @return List of TimeLog objects for the employee.
     */
    public static List<TimeLog> loadForEmployee(String filePath, String employeeNumber) {
        List<TimeLog> logs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length < 6) continue;
                if (!fields[0].equals(employeeNumber)) continue;
                try {
                    LocalDate date = LocalDate.parse(fields[3], DATE_FORMAT);
                    LocalTime logIn = LocalTime.parse(fields[4], TIME_FORMAT);
                    LocalTime logOut = LocalTime.parse(fields[5], TIME_FORMAT);
                    logs.add(new TimeLog(
                            fields[0], // employeeNumber
                            fields[1], // lastName
                            fields[2], // firstName
                            date,
                            logIn,
                            logOut
                    ));
                } catch (Exception parseEx) {
                    System.err.println("[ERROR] Failed to parse line: " + line);
                    parseEx.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading time sheet: " + e.getMessage());
        }
        return logs;
    }
}