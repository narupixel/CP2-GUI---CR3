package payrollCalculations;

import models.EmployeeProfile;
import models.WeeklyTotals;

/**
 * This class is responsible for calculating the total weekly allowances for an employee,
 * such as rice subsidy, phone allowance, and clothing allowance, based on their profile.
 */
public class CalculateWeeklyAllowances {

    /**
     * Calculates the total weekly allowances for the given employee.
     * @param employee The employee profile.
     * @return The total weekly allowance amount.
     */
    public static double calculateWeeklyAllowances(EmployeeProfile employee) {
        // If allowances are given per pay period, simply sum them up.
        return employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance();
    }

    /**
     * Optionally, you can display the breakdown of weekly allowances.
     */
    public static void displayWeeklyAllowances(EmployeeProfile employee, WeeklyTotals weeklyTotals) {
        System.out.println("Allowances for " + employee.getFirstName() + " " + employee.getLastName() + " (Week " + weeklyTotals.getWeekNumber() + "):");
        System.out.printf("Rice Subsidy: %.2f%n", employee.getRiceSubsidy());
        System.out.printf("Phone Allowance: %.2f%n", employee.getPhoneAllowance());
        System.out.printf("Clothing Allowance: %.2f%n", employee.getClothingAllowance());
        System.out.printf("Total Allowances: %.2f%n", calculateWeeklyAllowances(employee));
        System.out.println("---------------------------------------------------");
    }
}