import dataLoader.LoadEmployeeData;
import dataLoader.LoadTimeSheet;
import models.EmployeeProfile;
import models.TimeLog;
import models.WeeklyTotals;
import payrollCalculations.CalculateWeeklyTotals;
import payrollCalculations.CalculateAndDisplay;
import ui.ConsoleUI;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        gui.PayrollGUI.main(args);
        String employeeFile = "src/main/resources/Employee Details.tsv";
        String attendanceFile = "src/main/resources/Employee Attendance Record.tsv";

        List<EmployeeProfile> employees = LoadEmployeeData.loadFromFile(employeeFile);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            ConsoleUI.displayMenu();
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Enter Employee Number: ");
                    String empNum = scanner.nextLine().trim();
                    EmployeeProfile selected = employees.stream()
                            .filter(e -> e.getEmployeeNumber().equals(empNum))
                            .findFirst().orElse(null);

                    if (selected != null) {
                        ConsoleUI.displayEmployeeDetails(selected);

                        List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, empNum);
                        List<WeeklyTotals> weeklyTotalsList = CalculateWeeklyTotals.calculateWeeklyTotals(selected, logs);

                        for (WeeklyTotals weeklyTotals : weeklyTotalsList) {
                            ConsoleUI.displayWeeklySalary(weeklyTotals);
                            CalculateAndDisplay.processAndDisplay(selected, weeklyTotals);
                        }
                    } else {
                        ConsoleUI.displayMessage("Employee not found.");
                    }
                    break;
                case "2":
                    System.out.print("Enter Employee Number: ");
                    String empNum2 = scanner.nextLine().trim();
                    EmployeeProfile selected2 = employees.stream()
                            .filter(e -> e.getEmployeeNumber().equals(empNum2))
                            .findFirst().orElse(null);

                    if (selected2 != null) {
                        List<TimeLog> logs2 = LoadTimeSheet.loadForEmployee(attendanceFile, empNum2);
                        List<WeeklyTotals> weeklyTotalsList2 = CalculateWeeklyTotals.calculateWeeklyTotals(selected2, logs2);

                        if (weeklyTotalsList2.isEmpty()) {
                            ConsoleUI.displayMessage("No weekly salary information found for this employee.");
                        } else {
                            for (WeeklyTotals weeklyTotals : weeklyTotalsList2) {
                                ConsoleUI.displayWeeklySalary(weeklyTotals);
                            }
                        }
                    } else {
                        ConsoleUI.displayMessage("Employee not found.");
                    }
                    break;
                case "3":
                    running = false;
                    ConsoleUI.displayMessage("Exiting...");
                    break;
                default:
                    ConsoleUI.displayMessage("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}