package governmentContributions;

/**
 * CalculateSss.java
 * This class calculates the SSS employee contribution based on gross monthly or weekly pay.
 * For simplicity, this uses a sample SSS table logic. For production, load the table from a file.
 */
public class CalculateSss {
    // Example SSS table (2023): salary brackets and corresponding employee share
    // In a real system, load this from a file like SSS Contribution Table.tsv
    private static final double[][] SSS_TABLE = {
        // {minSalary, maxSalary, employeeShare}
        {0, 3249.99, 135.00},
        {3250, 3749.99, 157.50},
        {3750, 4249.99, 180.00},
        {4250, 4749.99, 202.50},
        {4750, 5249.99, 225.00},
        {5250, 5749.99, 247.50},
        {5750, 6249.99, 270.00},
        {6250, 6749.99, 292.50},
        {6750, 7249.99, 315.00},
        {7250, 7749.99, 337.50},
        {7750, 8249.99, 360.00},
        {8250, 8749.99, 382.50},
        {8750, 9249.99, 405.00},
        {9250, 9749.99, 427.50},
        {9750, 10249.99, 450.00},
        {10250, Double.MAX_VALUE, 472.50}
    };

    /**
     * Calculates the SSS employee contribution based on gross monthly pay.
     * @param grossMonthlyPay The employee's gross monthly pay.
     * @return The employee's SSS contribution.
     */
    public static double compute(double grossMonthlyPay) {
        for (double[] bracket : SSS_TABLE) {
            if (grossMonthlyPay >= bracket[0] && grossMonthlyPay <= bracket[1]) {
                return bracket[2];
            }
        }
        return 0.0;
    }

    /**
     * Calculates the SSS employee contribution based on gross weekly pay.
     * @param grossWeeklyPay The employee's gross weekly pay.
     * @return The employee's SSS contribution.
     */
    public static double computeFromWeekly(double grossWeeklyPay) {
        double grossMonthlyPay = grossWeeklyPay * 4; // Approximate 4 weeks per month
        return compute(grossMonthlyPay);
    }
}