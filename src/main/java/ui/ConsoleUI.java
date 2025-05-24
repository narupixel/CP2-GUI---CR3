package ui;

import models.EmployeeProfile;
import models.WeeklyTotals;

public class ConsoleUI {

    /**
     * Displays a welcome message and main menu options.
     */
    public static void displayMenu() {
        System.out.println("=======================================");
        System.out.println("        PAYROLL SYSTEM CONSOLE UI      ");
        System.out.println("=======================================");
        System.out.println("1. View Employee Details");
        System.out.println("2. View Weekly Salary Information");
        System.out.println("3. Exit");
        System.out.print("Select an option: ");
    }

    /**
     * Displays employee profile details.
     */
    public static void displayEmployeeDetails(EmployeeProfile employee) {
        System.out.println("\n--- Employee Details ---");
        System.out.println("Employee Number: " + employee.getEmployeeNumber());
        System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Birthday: " + employee.getBirthday());
        System.out.println("Position: " + employee.getPosition());
        System.out.println("Status: " + employee.getStatus());
        System.out.println("Immediate Supervisor: " + employee.getImmediateSupervisor());
        System.out.println("Basic Salary: " + employee.getBasicSalary());
        System.out.println("Rice Subsidy: " + employee.getRiceSubsidy());
        System.out.println("Phone Allowance: " + employee.getPhoneAllowance());
        System.out.println("Clothing Allowance: " + employee.getClothingAllowance());
        System.out.println("Hourly Rate: " + employee.getHourlyRate());
        System.out.println("------------------------\n");
    }

    /**
     * Displays weekly salary and work summary.
     */
    public static void displayWeeklySalary(WeeklyTotals weeklyTotals) {
        System.out.println("\n--- Weekly Salary Information ---");
        System.out.println("Week Number: " + weeklyTotals.getWeekNumber());
        System.out.println("Pay Period: " + weeklyTotals.getPeriodStart() + " to " + weeklyTotals.getPeriodEnd());
        System.out.println("Total Hours Worked: " + weeklyTotals.getTotalHoursWorked());
        System.out.println("Total Overtime: " + weeklyTotals.getTotalOvertime());
        System.out.println("----------------------------------\n");
    }

    /**
     * Displays a generic message.
     */
    public static void displayMessage(String message) {
        System.out.println(message);
    }
}