package governmentContributions;
/**
 * CalculatePhilhealth.java
 * This class calculates the PhilHealth employee contribution based on gross monthly or weekly pay.
 * The contribution is capped at 5000.00.
 */

public class CalculatePhilhealth {
    // 2024 PhilHealth: 5% of monthly basic salary, min 400, max 5000 (employee share is half)
    private static final double RATE = 0.05;
    private static final double MIN_MONTHLY_CONTRIBUTION = 400.0;
    private static final double MAX_MONTHLY_CONTRIBUTION = 5000.0;

    /**
     * Calculates the PhilHealth employee share based on gross monthly pay.
     * @param grossMonthlyPay The employee's gross monthly pay.
     * @return The employee's PhilHealth contribution (employee share).
     */
    public static double compute(double grossMonthlyPay) {
        double totalContribution = grossMonthlyPay * RATE;
        totalContribution = Math.max(totalContribution, MIN_MONTHLY_CONTRIBUTION);
        totalContribution = Math.min(totalContribution, MAX_MONTHLY_CONTRIBUTION);
        return totalContribution / 2.0; // Employee share
    }

    /**
     * Calculates the PhilHealth employee share based on gross weekly pay.
     * @param grossWeeklyPay The employee's gross weekly pay.
     * @return The employee's PhilHealth contribution (employee share).
     */
    public static double computeFromWeekly(double grossWeeklyPay) {
        double grossMonthlyPay = grossWeeklyPay * 4; // Approximate 4 weeks per month
        return compute(grossMonthlyPay);
    }
}