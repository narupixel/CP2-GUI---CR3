package gui;

/**
 * PayrollGUI.java
 * This class implements the graphical user interface for the payroll system.
 * It allows users to search for employees by their employee number and
 * calculate their monthly pay details including hours worked, allowances,
 * deductions, and net pay.
 * 
 * The GUI provides fields for entering employee number, selecting month and year,
 * and displays detailed payment information in a scrollable panel.
 * 
 * @author Payroll System Team
 * @version 1.0
 */

import dataLoader.LoadEmployeeData;
import dataLoader.LoadTimeSheet;
import models.EmployeeProfile;
import models.TimeLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class PayrollGUI extends JFrame {
    /**
     * GUI components used throughout the interface
     */
    private JTextField employeeNumberField;  // Text field for employee ID input
    private JButton calculateButton;         // Button to trigger payroll calculation
    private JPanel detailsPanel;             // Panel that contains results display

    /**
     * Data components used for payroll calculation
     */
    private EmployeeProfile selectedEmployee;  // Currently selected employee
    private List<EmployeeProfile> employees;   // List of all employees in the system
    private String attendanceFile = "src/main/resources/Employee Attendance Record.tsv"; // Path to attendance records    
    /**
     * Constructor that initializes the PayrollGUI and sets up all components.
     * This loads employee data, configures the UI layout, and sets up event handlers.
     */
    public PayrollGUI() {
        // Load employees from the data file at startup
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");

        // Configure the main window properties
        setTitle("Payroll System");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the top search panel with employee lookup controls
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Employee Number:"));
        employeeNumberField = new JTextField(10);
        searchPanel.add(employeeNumberField);

        // Add Enter key listener to trigger the Calculate button when Enter is pressed
        // This improves usability by allowing keyboard-based search
        employeeNumberField.addActionListener(e -> calculateButton.doClick());        // Month dropdown - Creates a formatted list of months (e.g., "01 - January")
        String[] months = java.util.Arrays.stream(java.time.Month.values())
            .map(m -> String.format("%02d - %s", m.getValue(), m.name().substring(0,1) + m.name().substring(1).toLowerCase()))
            .toArray(String[]::new);
        JComboBox<String> monthCombo = new JComboBox<>(months);
        searchPanel.add(new JLabel("Month:"));
        searchPanel.add(monthCombo);
        
        // Year dropdown - Provides a range of years (current year +/- 5 years)
        // This allows viewing historical payroll data as well as planning for future periods
        int currentYear = java.time.Year.now().getValue();
        Integer[] years = new Integer[11];
        for (int i = 0; i < 11; i++) years[i] = currentYear - 5 + i;
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(currentYear); // Set default to current year for convenience
        searchPanel.add(new JLabel("Year:"));
        searchPanel.add(yearCombo);
        
        // Calculate button - Triggers the payroll calculation process
        calculateButton = new JButton("Calculate");
        searchPanel.add(calculateButton);
        add(searchPanel, BorderLayout.NORTH);        // Center panel: Details and pay coverage
        // This panel serves as a container for payroll details display
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        // Create a results panel with a clean white background for displaying payroll information
        // Using BoxLayout for vertical stacking of information sections
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        
        // Add scrolling capability to the results panel to handle large payroll reports
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setPreferredSize(new Dimension(550, 250));
        detailsPanel.add(resultsScrollPane);

        // Add the details panel to the center of the main frame
        add(detailsPanel, BorderLayout.CENTER);

        // Set initial UI state
        resultsScrollPane.setVisible(true);
        calculateButton.setEnabled(true);        // Action listener for the Calculate button
        // When clicked, it processes the selected employee data and displays payroll information
        calculateButton.addActionListener((ActionEvent e) -> {
            // Clear previous results before showing new calculation
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            resultsScrollPane.setVisible(true);
            
            // Get the employee number entered by the user and trim whitespace
            String empNum = employeeNumberField.getText().trim();
            
            // Find the employee with the matching employee number in our database
            // Using Java Streams API for efficient searching through the employee list
            selectedEmployee = employees.stream()
                    .filter(emp -> emp.getEmployeeNumber().equals(empNum))
                    .findFirst().orElse(null);
                    
            // Handle the case where no employee is found with the entered number
            if (selectedEmployee == null) {
                // Display an error message in red to clearly indicate the issue
                JLabel noEmp = new JLabel("No employee found for number: " + empNum);
                noEmp.setForeground(Color.RED);
                resultsPanel.add(noEmp);
                resultsPanel.revalidate();
                return;
            }            // Show Employee Number, Name, and Birthday at the top of the resultsPanel, fully left-aligned
            String birthday = selectedEmployee.getBirthday();
            JPanel empPanel = new JPanel();
            empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.Y_AXIS));
            empPanel.setBackground(Color.WHITE);
            empPanel.setAlignmentX(Component.LEFT_ALIGNMENT);JLabel empNumLabel = new JLabel("Employee Number: " + selectedEmployee.getEmployeeNumber());
            empNumLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            empNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel nameLabel = new JLabel("Name: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel bdayLabel = new JLabel("Birthday: " + birthday);
            bdayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            bdayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            empPanel.add(empNumLabel);
            empPanel.add(nameLabel);
            empPanel.add(bdayLabel);
            empPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));            resultsPanel.add(empPanel);
            resultsPanel.add(Box.createVerticalStrut(5));
            
            // Extract the selected month and year from the UI components
            int monthIdx = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            
            // Calculate the start and end dates for the selected month
            LocalDate startDate = LocalDate.of(year, monthIdx, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            
            try {
                // Load the time logs for the employee from the attendance record file
                List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, selectedEmployee.getEmployeeNumber());
                
                // Check if there are any records for the selected year
                LocalDate yearStart = LocalDate.of(year, 1, 1);
                LocalDate yearEnd = LocalDate.of(year, 12, 31);
                List<TimeLog> yearLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(yearStart) && !log.getDate().isAfter(yearEnd))
                        .toList();                  // Filter for just the selected month
                List<TimeLog> filteredLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(startDate) && !log.getDate().isAfter(endDate))
                        .toList();
                  
                if (filteredLogs.isEmpty()) {
                    JLabel noRecords;
                    if (yearLogs.isEmpty()) {
                        // Case 3: No records for month AND year
                        noRecords = new JLabel("No attendance records found for this employee in the selected year.");
                    } else {
                        // Case 1: No records for month but records exist for year
                        noRecords = new JLabel("No attendance records found for this employee in the selected month.");
                    }
                    noRecords.setForeground(Color.RED);
                    resultsPanel.add(noRecords);                } else {
                    // Monthly aggregation
                    // Calculate total hours worked and overtime from the filtered time logs
                    double totalHours = filteredLogs.stream().mapToDouble(TimeLog::getHoursWorked).sum();
                    double totalOvertime = filteredLogs.stream().mapToDouble(TimeLog::getOvertime).sum();
                    
                    // Extract allowance values from the employee profile
                    double rice = selectedEmployee.getRiceSubsidy();
                    double phone = selectedEmployee.getPhoneAllowance();
                    double clothing = selectedEmployee.getClothingAllowance();
                    double totalAllowances = rice + phone + clothing;
                    
                    // Calculate gross pay based on hours worked and hourly rate
                    double basicGrossMonthlyPay = totalHours * selectedEmployee.getHourlyRate();
                    double grossMonthlyPay = basicGrossMonthlyPay + totalAllowances;
                    
                    // Calculate government-mandated contributions
                    // Note: Weekly rates are derived by dividing monthly pay by 4 weeks
                    double pagibig = governmentContributions.CalculatePagibig.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double philhealth = governmentContributions.CalculatePhilhealth.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double sss = governmentContributions.CalculateSss.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double withholdingTax = governmentContributions.CalculateWithholdingTax.compute(basicGrossMonthlyPay);
                    
                    // Calculate total deductions and net pay
                    double totalDeductions = pagibig + philhealth + sss + withholdingTax;
                    double netMonthlyPay = basicGrossMonthlyPay - totalDeductions + totalAllowances;
                    JPanel monthPanel = new JPanel();
                    monthPanel.setLayout(new GridLayout(0, 2, 8, 2));
                    monthPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200,200,200)),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                    monthPanel.setBackground(Color.WHITE);
                    monthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    Font boldFont = new Font("SansSerif", Font.BOLD, 14);
                    JLabel header = new JLabel("Payroll Summary for " + startDate.getMonth() + " " + startDate.getYear());
                    header.setFont(boldFont);
                    header.setForeground(new Color(30, 30, 30));
                    monthPanel.add(header);
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel("Period:"));
                    monthPanel.add(new JLabel(startDate + " to " + endDate));
                    monthPanel.add(new JLabel("Gross Monthly Pay:"));
                    monthPanel.add(new JLabel(String.format("%.2f", grossMonthlyPay)));
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel(""));
                    JLabel allowanceLabel = new JLabel("Allowance");
                    allowanceLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                    monthPanel.add(allowanceLabel);
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel("Rice Subsidy:"));
                    monthPanel.add(new JLabel(String.format("%.2f", rice)));
                    monthPanel.add(new JLabel("Phone Allowance:"));
                    monthPanel.add(new JLabel(String.format("%.2f", phone)));
                    monthPanel.add(new JLabel("Clothing Allowance:"));
                    monthPanel.add(new JLabel(String.format("%.2f", clothing)));
                    monthPanel.add(new JLabel("Total Allowances:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalAllowances)));
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel(""));
                    JLabel deductionsLabel = new JLabel("Deductions");
                    deductionsLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                    monthPanel.add(deductionsLabel);
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel("Pag-IBIG Contribution:"));
                    monthPanel.add(new JLabel(String.format("%.2f", pagibig)));
                    monthPanel.add(new JLabel("PhilHealth Contribution:"));
                    monthPanel.add(new JLabel(String.format("%.2f", philhealth)));
                    monthPanel.add(new JLabel("SSS Contribution:"));
                    monthPanel.add(new JLabel(String.format("%.2f", sss)));
                    monthPanel.add(new JLabel("Withholding Tax:"));
                    monthPanel.add(new JLabel(String.format("%.2f", withholdingTax)));
                    monthPanel.add(new JLabel("Total Deductions:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalDeductions)));
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel(""));
                    monthPanel.add(new JLabel("Total Hours Worked:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalHours)));
                    monthPanel.add(new JLabel("Total Overtime Hours:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalOvertime)));
                    JLabel netPayLabel = new JLabel("NET MONTHLY PAY:");
                    netPayLabel.setFont(boldFont);
                    netPayLabel.setForeground(new Color(0, 128, 0));
                    JLabel netPayValue = new JLabel(String.format("%.2f", netMonthlyPay));
                    netPayValue.setFont(boldFont);
                    netPayValue.setForeground(new Color(0, 128, 0));
                    monthPanel.add(netPayLabel);
                    monthPanel.add(netPayValue);
                    resultsPanel.add(Box.createVerticalStrut(10));
                    resultsPanel.add(monthPanel);
                }
                resultsPanel.revalidate();
                resultsPanel.repaint();
            } catch (Exception ex) {
                JLabel err = new JLabel("Error parsing dates or loading records: " + ex.getMessage());
                err.setForeground(Color.RED);
                resultsPanel.add(err);
                resultsPanel.revalidate();
                resultsPanel.repaint();
            }
        });
    }    /**
     * The main entry point for the application.
     * Creates an instance of the PayrollGUI and displays it on the screen.
     * Uses SwingUtilities.invokeLater to ensure that GUI creation happens on 
     * the Event Dispatch Thread for thread safety.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollGUI gui = new PayrollGUI();
            gui.setVisible(true);
        });
    }
}