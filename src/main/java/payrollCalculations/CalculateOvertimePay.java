package payrollCalculations;

/**
 * This class is responsible for calculating the overtime pay for an employee.
 * By default, it uses a 1.25x multiplier for overtime hours, but you can adjust as needed.
 */
public class CalculateOvertimePay {

    private static final double OVERTIME_MULTIPLIER = 1.25;

    /**
     * Calculates the overtime pay for the given overtime hours and hourly rate.
     * @param overtimeHours The number of overtime hours worked.
     * @param hourlyRate The employee's hourly rate.
     * @return The total overtime pay.
     */
    public static double calculateOverTimePay(double overtimeHours, double hourlyRate) {
        return overtimeHours * hourlyRate * OVERTIME_MULTIPLIER;
    }
}