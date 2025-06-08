package gui;

/**
 * PayrollGUI.java
 * 
 * This class implements the graphical user interface for the payroll system.
 * It allows users to search for employees by their employee number and
 * calculate their monthly pay details including hours worked, allowances,
 * deductions, and net pay. The GUI provides fields for entering employee 
 * number, selecting month and year, and displays detailed payment information 
 * in a scrollable panel.
 * 
 * Key Features:
 * - Employee lookup by employee number
 * - Month and year selection for payroll calculation
 * - Comprehensive payroll calculation including allowances and deductions
 * - Integration with employee management features
 * - Real-time validation and error handling
 * 
 * @author Payroll System Team
 * @version 1.0
 */

import dataLoader.LoadEmployeeData;
import dataLoader.LoadTimeSheet;
import models.EmployeeProfile;
import models.TimeLog;
import governmentContributions.*; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class PayrollGUI extends JFrame {
    
    /**
     * GUI components used throughout the interface for user interaction.
     */
    private JTextField employeeNumberField;  // Text field for employee ID input
    private JButton calculateButton;         // Button to trigger payroll calculation
    private JPanel detailsPanel;             // Panel that contains results display

    /**
     * Data components used for payroll calculation and employee management.
     */
    private EmployeeProfile selectedEmployee;  // Currently selected employee for calculation
    private List<EmployeeProfile> employees;   // List of all employees loaded from the system
    private String attendanceFile = "src/main/resources/Employee Attendance Record.tsv"; // Path to attendance records

    /**
     * Constructor that initializes the PayrollGUI and sets up all components.
     * This method loads employee data from the file system, configures the UI layout, 
     * creates all necessary panels and controls, and sets up event handlers for 
     * user interactions.
     */
    public PayrollGUI() {
        // Load employees from the data file at startup to populate the system
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");

        // Configure the main window properties for optimal user experience
        setTitle("Payroll System");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the top search panel with employee lookup controls
        // Use BorderLayout to better organize the components and ensure proper height
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setPreferredSize(new Dimension(700, 80)); // Set explicit height for the panel
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create the main controls panel for employee search and date selection
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.add(new JLabel("Employee Number:"));
        employeeNumberField = new JTextField(10);
        controlsPanel.add(employeeNumberField);

        // Add Enter key listener to trigger the Calculate button when Enter is pressed
        employeeNumberField.addActionListener(e -> calculateButton.doClick());
        
        // Create month dropdown with formatted month names for user-friendly selection
        String[] months = java.util.Arrays.stream(java.time.Month.values())
            .map(m -> String.format("%02d - %s", m.getValue(), 
                m.name().substring(0,1) + m.name().substring(1).toLowerCase()))
            .toArray(String[]::new);
        JComboBox<String> monthCombo = new JComboBox<>(months);
        controlsPanel.add(new JLabel("Month:"));
        controlsPanel.add(monthCombo);
        
        // Create year dropdown with a range of years centered around the current year
        int currentYear = java.time.Year.now().getValue();
        Integer[] years = new Integer[11]; // Current year plus/minus 5 years
        for (int i = 0; i < 11; i++) {
            years[i] = currentYear - 5 + i;
        }
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(currentYear); // Default to current year
        controlsPanel.add(new JLabel("Year:"));
        controlsPanel.add(yearCombo);
        
        // Add the search panel to the main layout
        searchPanel.add(controlsPanel, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // Create a button panel for the main action buttons placed prominently in the center-top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create the Calculate button for triggering payroll computation
        calculateButton = new JButton("Calculate Payroll");
        calculateButton.setPreferredSize(new Dimension(150, 35));
        calculateButton.setToolTipText("Calculate payroll for the selected employee and time period");
        buttonPanel.add(calculateButton);
        
        // Create the View All Employees button as the primary navigation button
        JButton viewAllEmployeesButton = new JButton("View All Employees");
        viewAllEmployeesButton.setPreferredSize(new Dimension(150, 35));
        viewAllEmployeesButton.setToolTipText("Open the employee management window to view, edit, and add employees");
        viewAllEmployeesButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                EmployeeListGUI employeeListGUI = new EmployeeListGUI();
                employeeListGUI.setVisible(true);
            });
        });
        buttonPanel.add(viewAllEmployeesButton);

        // Create the center panel for details and pay coverage display
        // This panel serves as a container for payroll details and calculation results
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        // Add the button panel at the top of the details section for better visibility
        detailsPanel.add(buttonPanel);
        
        // Create a results panel with a clean white background for displaying payroll information
        // Using BoxLayout for vertical stacking of information sections
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        
        // Add scrolling capability to the results panel to handle large payroll reports
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setPreferredSize(new Dimension(550, 400)); // Increased height since buttons moved
        detailsPanel.add(resultsScrollPane);

        // Add the details panel to the center of the main frame
        add(detailsPanel, BorderLayout.CENTER);

        // Set initial UI state to ensure proper component visibility
        resultsScrollPane.setVisible(true);
        calculateButton.setEnabled(true);

        // Configure the action listener for the Calculate button
        // When clicked, it processes the selected employee data and displays payroll information
        calculateButton.addActionListener((ActionEvent e) -> {
            // Clear previous results before showing new calculation to prevent data overlap
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            resultsScrollPane.setVisible(true);
            
            // Get the employee number entered by the user and trim whitespace for validation
            String empNum = employeeNumberField.getText().trim();
            
            // Find the employee with the matching employee number in our database
            // Using Java Streams API for efficient searching through the employee list
            selectedEmployee = employees.stream()
                    .filter(emp -> emp.getEmployeeNumber().equals(empNum))
                    .findFirst().orElse(null);
                    
            // Handle the case where no employee is found with the entered number
            if (selectedEmployee == null) {
                // Display an error message in red to clearly indicate the issue to the user
                JLabel noEmp = new JLabel("No employee found for number: " + empNum);
                noEmp.setForeground(Color.RED);
                resultsPanel.add(noEmp);
                resultsPanel.revalidate();
                return;
            }

            // Display Employee Number, Name, and Birthday at the top of the results panel
            String birthday = selectedEmployee.getBirthday();
            JPanel empPanel = new JPanel();
            empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.Y_AXIS));
            empPanel.setBackground(Color.WHITE);
            empPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Create and format employee information labels with consistent styling
            JLabel empNumLabel = new JLabel("Employee Number: " + selectedEmployee.getEmployeeNumber());
            empNumLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            empNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel nameLabel = new JLabel("Name: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel bdayLabel = new JLabel("Birthday: " + birthday);
            bdayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            bdayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Add all employee information labels to the panel
            empPanel.add(empNumLabel);
            empPanel.add(nameLabel);
            empPanel.add(bdayLabel);
            empPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

            // Add the employee panel to the results and create spacing
            resultsPanel.add(empPanel);
            resultsPanel.add(Box.createVerticalStrut(5));
            
            // Extract the selected month and year from the UI components for calculation
            int monthIdx = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            
            // Calculate the start and end dates for the selected month
            LocalDate startDate = LocalDate.of(year, monthIdx, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            
            try {
                // Load the time logs for the employee from the attendance record file
                List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, selectedEmployee.getEmployeeNumber());
                
                // Check if there are any records for the selected year for better error handling
                LocalDate yearStart = LocalDate.of(year, 1, 1);
                LocalDate yearEnd = LocalDate.of(year, 12, 31);
                List<TimeLog> yearLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(yearStart) && !log.getDate().isAfter(yearEnd))
                        .toList();

                // Filter for just the selected month to get relevant attendance data
                List<TimeLog> filteredLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(startDate) && !log.getDate().isAfter(endDate))
                        .toList();
                  
                if (filteredLogs.isEmpty()) {
                    JLabel noRecords;
                    if (yearLogs.isEmpty()) {
                        // Case: No records for month AND year - broader issue
                        noRecords = new JLabel("No attendance records found for this employee in the selected year.");
                    } else {
                        // Case: No records for month but records exist for year - specific month issue
                        noRecords = new JLabel("No attendance records found for this employee in the selected month.");
                    }
                    noRecords.setForeground(Color.RED);
                    resultsPanel.add(noRecords);
                } else {
                    // Perform monthly aggregation and payroll calculation
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
                    
                    // Calculate government-mandated contributions using weekly rates
                    // Note: Weekly rates are derived by dividing monthly pay by 4 weeks
                    double pagibig = governmentContributions.CalculatePagibig.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double philhealth = governmentContributions.CalculatePhilhealth.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double sss = governmentContributions.CalculateSss.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double withholdingTax = governmentContributions.CalculateWithholdingTax.compute(basicGrossMonthlyPay);
                    
                    // Calculate total deductions and net pay for final payroll summary
                    double totalDeductions = pagibig + philhealth + sss + withholdingTax;
                    double netMonthlyPay = basicGrossMonthlyPay - totalDeductions + totalAllowances;

                    // Create the payroll summary panel with organized layout
                    JPanel monthPanel = new JPanel();
                    monthPanel.setLayout(new GridLayout(0, 2, 8, 2));
                    monthPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200,200,200)),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                    monthPanel.setBackground(Color.WHITE);
                    monthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    // Define fonts for consistent formatting throughout the summary
                    Font boldFont = new Font("SansSerif", Font.BOLD, 14);
                    Font sectionFont = new Font("SansSerif", Font.BOLD, 13);

                    // Add header information for the payroll summary
                    JLabel header = new JLabel("Payroll Summary for " + startDate.getMonth() + " " + startDate.getYear());
                    header.setFont(boldFont);
                    header.setForeground(new Color(30, 30, 30));
                    monthPanel.add(header);
                    monthPanel.add(new JLabel(""));

                    // Add period and basic pay information
                    monthPanel.add(new JLabel("Period:"));
                    monthPanel.add(new JLabel(startDate + " to " + endDate));
                    monthPanel.add(new JLabel("Gross Monthly Pay:"));
                    monthPanel.add(new JLabel(String.format("%.2f", grossMonthlyPay)));
                    monthPanel.add(new JLabel(""));

                    // Add allowances section with proper formatting
                    JLabel allowanceLabel = new JLabel("Allowances");
                    allowanceLabel.setFont(sectionFont);
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

                    // Add deductions section with government contribution details
                    JLabel deductionsLabel = new JLabel("Deductions");
                    deductionsLabel.setFont(sectionFont);
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

                    // Add hours worked information for transparency
                    monthPanel.add(new JLabel("Total Hours Worked:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalHours)));
                    monthPanel.add(new JLabel("Total Overtime Hours:"));
                    monthPanel.add(new JLabel(String.format("%.2f", totalOvertime)));

                    // Add net pay with emphasis using green color and bold font
                    JLabel netPayLabel = new JLabel("NET MONTHLY PAY:");
                    netPayLabel.setFont(boldFont);
                    netPayLabel.setForeground(new Color(0, 128, 0));
                    JLabel netPayValue = new JLabel(String.format("%.2f", netMonthlyPay));
                    netPayValue.setFont(boldFont);
                    netPayValue.setForeground(new Color(0, 128, 0));
                    monthPanel.add(netPayLabel);
                    monthPanel.add(netPayValue);

                    // Add the completed summary panel to the results display
                    resultsPanel.add(Box.createVerticalStrut(10));
                    resultsPanel.add(monthPanel);
                }

                // Refresh the display to show the updated results
                resultsPanel.revalidate();
                resultsPanel.repaint();
                
            } catch (Exception ex) {
                // Handle any errors that occur during calculation or data loading
                JLabel err = new JLabel("Error parsing dates or loading records: " + ex.getMessage());
                err.setForeground(Color.RED);
                resultsPanel.add(err);
                resultsPanel.revalidate();
                resultsPanel.repaint();
            }
        });
    }

    /**
     * The main entry point for the application.
     * Creates an instance of the PayrollGUI and displays it on the screen.
     * Uses SwingUtilities.invokeLater to ensure that GUI creation happens on 
     * the Event Dispatch Thread for thread safety and proper Swing behavior.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollGUI gui = new PayrollGUI();
            gui.setVisible(true);
        });
    }
}