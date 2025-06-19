package gui;

import models.EmployeeProfile;
import models.TimeLog;
import dataLoader.LoadTimeSheet;
import governmentContributions.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;  // Add this import
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * EmployeeDetailGUI.java
 * 
 * This class provides a detailed view form for individual employee information
 * displaying exactly the fields specified in the change request: Employee Number,
 * Last Name, First Name, SSS Number, PhilHealth Number, TIN, and Pag-IBIG Number.
 * 
 * Additionally, it prompts the user to select a month for salary computation and
 * displays both employee details and computed salary information in the same frame.
 * 
 * Required Fields (as per change request):
 * 1. Employee Number
 * 2. Last Name
 * 3. First Name
 * 4. SSS Number
 * 5. PhilHealth Number
 * 6. TIN
 * 7. Pag-IBIG Number
 * 
 * Additional Features:
 * - Month selection for salary computation
 * - Detailed salary calculation display within the same frame
 * - Comprehensive payroll breakdown
 * 
 * @author Payroll System Team
 * @version 1.0
 */
public class EmployeeDetailGUI extends JDialog {
    
    private EmployeeProfile employee;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JButton computeButton;
    private JPanel salaryDisplayPanel;
    private JScrollPane salaryScrollPane;
    
    // Add the attendance file path
    private static final String attendanceFile = "src/main/resources/Employee Attendance Record.tsv";

    /**
     * Constructor for the Employee Detail dialog
     */
    public EmployeeDetailGUI(PayrollGUI parent, EmployeeProfile employee) {
        super(parent, "Employee Details", true);
        this.employee = employee;
        
        initializeComponents();
        setupLayout();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(true);
    }

    /**
     * Initializes all GUI components for the employee details dialog.
     */
    private void initializeComponents() {
        // Initialize the salary results panel first (even though it starts empty)
        salaryDisplayPanel = new JPanel();
        salaryDisplayPanel.setLayout(new BoxLayout(salaryDisplayPanel, BoxLayout.Y_AXIS));
        salaryDisplayPanel.setBackground(Color.WHITE);
        salaryDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create scroll pane for salary results (initially empty) - Fix the missing field
        salaryScrollPane = new JScrollPane(salaryDisplayPanel);
        salaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        salaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        salaryScrollPane.setPreferredSize(new Dimension(750, 300));
        salaryScrollPane.setBorder(BorderFactory.createTitledBorder("Salary Calculation Results"));

        // Add a placeholder message in the results panel
        JLabel placeholderLabel = new JLabel("Select a month and click 'Compute Salary' to see results");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.GRAY);
        salaryDisplayPanel.add(placeholderLabel);
        
        // Create the Compute Salary button
        computeButton = new JButton("Compute Salary");
        computeButton.setPreferredSize(new Dimension(140, 30));
        computeButton.setToolTipText("Calculate salary for the selected month and year");
        computeButton.addActionListener(e -> computeSalaryForSelectedMonth());
    }

    /**
     * Organizes and positions all GUI components within the dialog using
     * appropriate layout managers for optimal user experience and visual appeal.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create and add the employee information panel at the top
        JPanel employeeInfoPanel = createEmployeeInfoPanel();
        add(employeeInfoPanel, BorderLayout.NORTH);

        // Create and add the salary computation panel in the center-top
        JPanel computationPanel = createSalaryComputationPanel();
        
        // Create a wrapper panel for the computation controls
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(computationPanel, BorderLayout.NORTH);
        centerPanel.add(salaryScrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);

        // Add buttons panel at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        
        // Fix: Ensure proper dialog closure
        closeButton.addActionListener(e -> {
            // Close the dialog properly
            setVisible(false);
            dispose(); // This releases resources and closes the dialog
        });
        
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set default close operation to properly handle window closing
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates the main information panel displaying exactly the employee fields
     * specified in the change request. The panel uses a clean grid layout to
     * present the seven required fields in a professional, easy-to-read format.
     */
    private JPanel createEmployeeInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Employee Information"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        infoPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Define consistent fonts for professional appearance
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 12);
        
        // Field 1: Employee Number
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel empNumLabel = new JLabel("Employee Number:");
        empNumLabel.setFont(labelFont);
        infoPanel.add(empNumLabel, gbc);
        
        gbc.gridx = 1;
        JLabel empNumValue = new JLabel(employee.getEmployeeNumber());
        empNumValue.setFont(valueFont);
        infoPanel.add(empNumValue, gbc);
        
        // Field 2: Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(labelFont);
        infoPanel.add(lastNameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lastNameValue = new JLabel(employee.getLastName());
        lastNameValue.setFont(valueFont);
        infoPanel.add(lastNameValue, gbc);
        
        // Field 3: First Name
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(labelFont);
        infoPanel.add(firstNameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel firstNameValue = new JLabel(employee.getFirstName());
        firstNameValue.setFont(valueFont);
        infoPanel.add(firstNameValue, gbc);
        
        // Field 4: SSS Number
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel sssLabel = new JLabel("SSS Number:");
        sssLabel.setFont(labelFont);
        infoPanel.add(sssLabel, gbc);
        
        gbc.gridx = 1;
        JLabel sssValue = new JLabel(employee.getSssNumber());
        sssValue.setFont(valueFont);
        infoPanel.add(sssValue, gbc);
        
        // Field 5: PhilHealth Number
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel philhealthLabel = new JLabel("PhilHealth Number:");
        philhealthLabel.setFont(labelFont);
        infoPanel.add(philhealthLabel, gbc);
        
        gbc.gridx = 1;
        JLabel philhealthValue = new JLabel(employee.getPhilhealthNumber());
        philhealthValue.setFont(valueFont);
        infoPanel.add(philhealthValue, gbc);
        
        // Field 6: TIN
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel tinLabel = new JLabel("TIN:");
        tinLabel.setFont(labelFont);
        infoPanel.add(tinLabel, gbc);
        
        gbc.gridx = 1;
        JLabel tinValue = new JLabel(employee.getTinNumber());
        tinValue.setFont(valueFont);
        infoPanel.add(tinValue, gbc);
        
        // Field 7: Pag-IBIG Number
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel pagibigLabel = new JLabel("Pag-IBIG Number:");
        pagibigLabel.setFont(labelFont);
        infoPanel.add(pagibigLabel, gbc);
        
        gbc.gridx = 1;
        JLabel pagibigValue = new JLabel(employee.getPagibigNumber());
        pagibigValue.setFont(valueFont);
        infoPanel.add(pagibigValue, gbc);
        
        return infoPanel;
    }

    /**
     * Creates the salary computation panel with month/year selection and compute button.
     * This panel allows users to select a specific month and year for salary calculation
     * and displays the compute button to trigger the payroll calculation process.
     */
    private JPanel createSalaryComputationPanel() {
        // Create the main panel for salary computation controls
        JPanel computationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        computationPanel.setBackground(Color.WHITE);
        computationPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Salary Computation", 
            TitledBorder.LEFT, 
            TitledBorder.TOP
        ));

        // Create and configure the month selection combo box
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setPreferredSize(new Dimension(120, 30));
        monthComboBox.setSelectedIndex(0); // Default to January

        // Create and configure the year selection combo box - Fix the type inference
        String[] years = {"2022", "2023", "2024", "2025", "2026"}; // Use String array instead
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setPreferredSize(new Dimension(80, 30));
        yearComboBox.setSelectedIndex(2); // Default to 2024

        // Add labels and components to the panel
        computationPanel.add(new JLabel("Month:"));
        computationPanel.add(monthComboBox);
        computationPanel.add(new JLabel("Year:"));
        computationPanel.add(yearComboBox);
        computationPanel.add(computeButton);

        return computationPanel;
    }

    /**
     * Computes the salary for the selected month and displays the results
     * in the same frame along with the employee details.
     */
    private void computeSalaryForSelectedMonth() {
        // Clear previous results
        salaryDisplayPanel.removeAll();
        
        // Get selected month and year
        int monthIdx = monthComboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem()); // Parse String to int
        
        // Calculate the start and end dates for the selected month
        LocalDate startDate = LocalDate.of(year, monthIdx, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        try {
            // Load the time logs for the employee
            List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, employee.getEmployeeNumber());
            
            // Check if there are any records for the selected year
            LocalDate yearStart = LocalDate.of(year, 1, 1);
            LocalDate yearEnd = LocalDate.of(year, 12, 31);
            List<TimeLog> yearLogs = logs.stream()
                    .filter(log -> !log.getDate().isBefore(yearStart) && !log.getDate().isAfter(yearEnd))
                    .toList();

            // Filter for the selected month
            List<TimeLog> filteredLogs = logs.stream()
                    .filter(log -> !log.getDate().isBefore(startDate) && !log.getDate().isAfter(endDate))
                    .toList();
              
            if (filteredLogs.isEmpty()) {
                // Create error message panel
                JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                errorPanel.setBackground(Color.WHITE);
                
                JLabel noRecords;
                if (yearLogs.isEmpty()) {
                    noRecords = new JLabel("No attendance records found for this employee in " + year + ".");
                } else {
                    noRecords = new JLabel("No attendance records found for " + startDate.getMonth() + " " + year + ".");
                }
                noRecords.setForeground(Color.RED);
                noRecords.setFont(new Font("SansSerif", Font.BOLD, 12));
                
                errorPanel.add(noRecords);
                errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                
                salaryDisplayPanel.add(Box.createVerticalStrut(20));
                salaryDisplayPanel.add(errorPanel);
                
            } else {
                // Calculate payroll details
                double totalHours = filteredLogs.stream().mapToDouble(TimeLog::getHoursWorked).sum();
                double totalOvertime = filteredLogs.stream().mapToDouble(TimeLog::getOvertime).sum();
                
                // Extract allowances
                double rice = employee.getRiceSubsidy();
                double phone = employee.getPhoneAllowance();
                double clothing = employee.getClothingAllowance();
                double totalAllowances = rice + phone + clothing;
                
                // Calculate gross pay components
                double basicGrossMonthlyPay = totalHours * employee.getHourlyRate();
                double grossMonthlyPay = basicGrossMonthlyPay + totalAllowances; // Total gross including allowances
                
                // Calculate government contributions (based on basic pay only, not including allowances)
                double pagibig = CalculatePagibig.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                double philhealth = CalculatePhilhealth.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                double sss = CalculateSss.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                double withholdingTax = CalculateWithholdingTax.compute(basicGrossMonthlyPay);
                
                // Calculate final amounts
                double totalDeductions = pagibig + philhealth + sss + withholdingTax;
                double netMonthlyPay = grossMonthlyPay - totalDeductions; // Use grossMonthlyPay here

                // Create the detailed salary breakdown panel
                createSalaryBreakdownPanel(startDate, endDate, totalHours, totalOvertime, 
                    basicGrossMonthlyPay, rice, phone, clothing, totalAllowances,
                    pagibig, philhealth, sss, withholdingTax, totalDeductions, netMonthlyPay);
        }
        
    } catch (Exception ex) {
        // Create error message panel
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        
        JLabel errorLabel = new JLabel("Error calculating salary: " + ex.getMessage());
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        errorPanel.add(errorLabel);
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        salaryDisplayPanel.add(Box.createVerticalStrut(20));
        salaryDisplayPanel.add(errorPanel);
    }
    
    // Force layout update
    salaryDisplayPanel.revalidate();
    salaryDisplayPanel.repaint();
    
    // Scroll to top to show results
    SwingUtilities.invokeLater(() -> {
        salaryScrollPane.getVerticalScrollBar().setValue(0);
    });
}

/**
 * Creates a detailed salary breakdown panel with all computed values.
 */
private void createSalaryBreakdownPanel(LocalDate startDate, LocalDate endDate, 
        double totalHours, double totalOvertime, double basicGrossMonthlyPay,
        double rice, double phone, double clothing, double totalAllowances,
        double pagibig, double philhealth, double sss, double withholdingTax,
        double totalDeductions, double netMonthlyPay) {

    // Create main container panel
    JPanel containerPanel = new JPanel();
    containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
    containerPanel.setBackground(Color.WHITE);
    containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Create the breakdown panel with grid layout
    JPanel breakdownPanel = new JPanel(new GridLayout(0, 2, 10, 8));
    breakdownPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Salary Breakdown for " + startDate.getMonth() + " " + startDate.getYear()),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
    breakdownPanel.setBackground(Color.WHITE);

    // Define fonts
    Font boldFont = new Font("SansSerif", Font.BOLD, 12);
    Font sectionFont = new Font("SansSerif", Font.BOLD, 11);

    // Period
    JLabel periodLabel = new JLabel("Period:");
    periodLabel.setFont(sectionFont);
    breakdownPanel.add(periodLabel);
    breakdownPanel.add(new JLabel(startDate + " to " + endDate));

    // Hours
    breakdownPanel.add(new JLabel("Total Hours Worked:"));
    breakdownPanel.add(new JLabel(String.format("%.2f hours", totalHours)));
    breakdownPanel.add(new JLabel("Total Overtime Hours:"));
    breakdownPanel.add(new JLabel(String.format("%.2f hours", totalOvertime)));
    breakdownPanel.add(new JLabel("Hourly Rate:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", employee.getHourlyRate())));

    // Basic Pay
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel(""));
    JLabel basicPayLabel = new JLabel("Basic Gross Pay:");
    basicPayLabel.setFont(sectionFont);
    breakdownPanel.add(basicPayLabel);
    JLabel basicPayValue = new JLabel(String.format("₱%.2f", basicGrossMonthlyPay));
    basicPayValue.setFont(sectionFont);
    breakdownPanel.add(basicPayValue);

    // Allowances
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel(""));
    JLabel allowancesLabel = new JLabel("Allowances:");
    allowancesLabel.setFont(sectionFont);
    breakdownPanel.add(allowancesLabel);
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel("  Rice Subsidy:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", rice)));
    breakdownPanel.add(new JLabel("  Phone Allowance:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", phone)));
    breakdownPanel.add(new JLabel("  Clothing Allowance:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", clothing)));
    JLabel totalAllowLabel = new JLabel("Total Allowances:");
    totalAllowLabel.setFont(sectionFont);
    breakdownPanel.add(totalAllowLabel);
    JLabel totalAllowValue = new JLabel(String.format("₱%.2f", totalAllowances));
    totalAllowValue.setFont(sectionFont);
    breakdownPanel.add(totalAllowValue);

    // Deductions
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel(""));
    JLabel deductionsLabel = new JLabel("Deductions:");
    deductionsLabel.setFont(sectionFont);
    breakdownPanel.add(deductionsLabel);
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel("  SSS Contribution:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", sss)));
    breakdownPanel.add(new JLabel("  PhilHealth Contribution:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", philhealth)));
    breakdownPanel.add(new JLabel("  Pag-IBIG Contribution:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", pagibig)));
    breakdownPanel.add(new JLabel("  Withholding Tax:"));
    breakdownPanel.add(new JLabel(String.format("₱%.2f", withholdingTax)));
    JLabel totalDeductLabel = new JLabel("Total Deductions:");
    totalDeductLabel.setFont(sectionFont);
    breakdownPanel.add(totalDeductLabel);
    JLabel totalDeductValue = new JLabel(String.format("₱%.2f", totalDeductions));
    totalDeductValue.setFont(sectionFont);
    breakdownPanel.add(totalDeductValue);

    // Net Pay
    breakdownPanel.add(new JLabel(""));
    breakdownPanel.add(new JLabel(""));
    JLabel netPayLabel = new JLabel("NET MONTHLY PAY:");
    netPayLabel.setFont(boldFont);
    netPayLabel.setForeground(new Color(0, 128, 0));
    JLabel netPayValue = new JLabel(String.format("₱%.2f", netMonthlyPay));
    netPayValue.setFont(boldFont);
    netPayValue.setForeground(new Color(0, 128, 0));
    breakdownPanel.add(netPayLabel);
    breakdownPanel.add(netPayValue);

    // Add breakdown panel to container
    containerPanel.add(breakdownPanel);
    
    // Set maximum size to prevent stretching
    containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, containerPanel.getPreferredSize().height));

    // Add the container panel to the results
    salaryDisplayPanel.add(containerPanel);
    salaryDisplayPanel.add(Box.createVerticalGlue()); // Push content to top
}
}