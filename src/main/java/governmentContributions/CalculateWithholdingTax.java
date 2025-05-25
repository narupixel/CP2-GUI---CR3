package governmentContributions;

/**
 * CalculateWithholdingTax.java
 * This class calculates the withholding tax based on an employee's weekly earnings,
 * using the 2023 Philippine BIR withholding tax table for monthly rates.
 * The weekly earnings are annualized and then the monthly tax is computed and divided by 4.
 */
public class CalculateWithholdingTax {

    /**
     * Calculates the withholding tax based on weekly earnings.
     * @param weeklyEarnings The employee's gross weekly earnings.
     * @return The withholding tax for the week.
     */
    public static double compute(double weeklyEarnings) {
        // Convert weekly earnings to monthly for tax bracket computation
        double monthlyEarnings = weeklyEarnings; // Approximate 4 weeks per month

        double tax = 0.0;
        if (monthlyEarnings <= 20832) {
            tax = 0.0;
        } else if (monthlyEarnings <= 33333) {
            tax = (monthlyEarnings - 20833) * 0.20;
        } else if (monthlyEarnings <= 66667) {
            tax = 2500 + (monthlyEarnings - 33333) * 0.25;
        } else if (monthlyEarnings <= 166667) {
            tax = 10833 + (monthlyEarnings - 66667) * 0.30;
        } else if (monthlyEarnings <= 666667) {
            tax = 40833.33 + (monthlyEarnings - 166667) * 0.32;
        } else {
            tax = 200833.33 + (monthlyEarnings - 666667) * 0.35;
        }

        // Convert monthly tax to weekly
        return tax;
    }
}