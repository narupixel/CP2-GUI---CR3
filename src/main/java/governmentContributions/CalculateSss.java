package governmentContributions;

/**
 * CalculateSss.java
 * This class calculates the SSS employee contribution based on gross monthly or weekly pay.
 * For simplicity, this uses a sample SSS table logic. For production, load the table from a file.
 */
public class CalculateSss {
    // Example SSS table (2023): salary brackets and corresponding employee share
    // In a real system, load this from a file like SSS Contribution Table.tsv
    public static double compute(double totalMonthlyNetSalary) {
        if (totalMonthlyNetSalary < 3250) return 135.00;
        else if (totalMonthlyNetSalary < 3750) return 157.50;
        else if (totalMonthlyNetSalary < 4250) return 180.00;
        else if (totalMonthlyNetSalary < 4750) return 202.50;
        else if (totalMonthlyNetSalary < 5250) return 225.00;
        else if (totalMonthlyNetSalary < 5750) return 247.50;
        else if (totalMonthlyNetSalary < 6250) return 270.00;
        else if (totalMonthlyNetSalary < 6750) return 292.50;
        else if (totalMonthlyNetSalary < 7250) return 315.00;
        else if (totalMonthlyNetSalary < 7750) return 337.50;
        else if (totalMonthlyNetSalary < 8250) return 360.00;
        else if (totalMonthlyNetSalary < 8750) return 382.50;
        else if (totalMonthlyNetSalary < 9250) return 405.00;
        else if (totalMonthlyNetSalary < 9750) return 427.50;
        else if (totalMonthlyNetSalary < 10250) return 450.00;
        else if (totalMonthlyNetSalary < 10750) return 472.50;
        else if (totalMonthlyNetSalary < 11250) return 495.00;
        else if (totalMonthlyNetSalary < 11750) return 517.50;
        else if (totalMonthlyNetSalary < 12250) return 540.00;
        else if (totalMonthlyNetSalary < 12750) return 562.50;
        else if (totalMonthlyNetSalary < 13250) return 585.00;
        else if (totalMonthlyNetSalary < 13750) return 607.50;
        else if (totalMonthlyNetSalary < 14250) return 630.00;
        else if (totalMonthlyNetSalary < 14750) return 652.50;
        else if (totalMonthlyNetSalary < 15250) return 675.00;
        else if (totalMonthlyNetSalary < 15750) return 697.50;
        else if (totalMonthlyNetSalary < 16250) return 720.00;
        else if (totalMonthlyNetSalary < 16750) return 742.50;
        else if (totalMonthlyNetSalary < 17250) return 765.00;
        else if (totalMonthlyNetSalary < 17750) return 787.50;
        else if (totalMonthlyNetSalary < 18250) return 810.00;
        else if (totalMonthlyNetSalary < 18750) return 832.50;
        else if (totalMonthlyNetSalary < 19250) return 855.00;
        else if (totalMonthlyNetSalary < 19750) return 877.50;
        else if (totalMonthlyNetSalary < 20250) return 900.00;
        else if (totalMonthlyNetSalary < 20750) return 922.50;
        else if (totalMonthlyNetSalary < 21250) return 945.00;
        else if (totalMonthlyNetSalary < 21750) return 967.50;
        else if (totalMonthlyNetSalary < 22250) return 990.00;
        else if (totalMonthlyNetSalary < 22750) return 1012.50;
        else if (totalMonthlyNetSalary < 23250) return 1035.00;
        else if (totalMonthlyNetSalary < 23750) return 1057.50;
        else if (totalMonthlyNetSalary < 24250) return 1080.00;
        else if (totalMonthlyNetSalary < 24750) return 1102.50;
        else return 1125.00; // For salary 24750 and above
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