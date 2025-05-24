package governmentContributions;
/**
 * CalculatePagibig.java
 * This class calculates the Pag-IBIG employee contribution based on gross monthly or weekly pay.
 * The contribution is capped at 100.00.
 */

public class CalculatePagibig {
    private static final double MAX_CONTRIBUTION = 100.0;

    /**
     * Calculates the Pag-IBIG employee contribution based on gross monthly pay.
     * @param grossMonthlyPay The employee's gross monthly pay.
     * @return The employee's Pag-IBIG contribution (capped at 100).
     */
    public static double compute(double grossMonthlyPay) {
        double contribution;
        if (grossMonthlyPay >= 1000 && grossMonthlyPay <= 1500) {
            contribution = grossMonthlyPay * 0.01;
        } else if (grossMonthlyPay > 1500) {
            contribution = grossMonthlyPay * 0.02;
        } else {
            contribution = 0.0;
        }
        return Math.min(contribution, MAX_CONTRIBUTION);
    }

    /**
     * Calculates the Pag-IBIG employee contribution based on gross weekly pay.
     * @param grossWeeklyPay The employee's gross weekly pay.
     * @return The employee's Pag-IBIG contribution (capped at 100).
     */
    public static double computeFromWeekly(double grossWeeklyPay) {
        double grossMonthlyPay = grossWeeklyPay * 4; // Approximate 4 weeks per month
        return compute(grossMonthlyPay);
    }
}