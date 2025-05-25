package governmentContributions;
/**
 * CalculatePhilhealth.java
 * This class calculates the PhilHealth employee contribution based on gross monthly or weekly pay.
 * The contribution is capped at 5000.00.
 */

public class CalculatePhilhealth {
    // 2024 PhilHealth: 5% of monthly basic salary, min 400, max 5000 (employee share is half)
    /**
     * Calculates the PhilHealth employee share based on gross monthly salary.
     * @param totalMonthlyNetSalary The employee's gross monthly pay.
     * @return The employee's PhilHealth contribution (employee share).
     */
    public static double calculatePhilHealth(double totalMonthlyNetSalary) {
        if (totalMonthlyNetSalary <= 10000) {
            return 300.00; // Minimum PhilHealth contribution
        } else if (totalMonthlyNetSalary <= 59999.99) {
            return totalMonthlyNetSalary * 0.03 / 2; // 3% of salary, split between employer & employee
        } else {
            return 1800.00 / 2; // Maximum PhilHealth contribution
        }
    }

    /**
     * Calculates the PhilHealth employee share based on gross weekly pay.
     * @param grossWeeklyPay The employee's gross weekly pay.
     * @return The employee's PhilHealth contribution (employee share).
     */
    public static double computeFromWeekly(double grossWeeklyPay) {
        double grossMonthlyPay = grossWeeklyPay * 4; // Approximate 4 weeks per month
        return calculatePhilHealth(grossMonthlyPay);
        
    }
    
}