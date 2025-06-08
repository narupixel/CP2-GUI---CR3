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
    
    /**
     * Reference to the parent window for proper modal behavior.
     */
    private final PayrollGUI parentWindow;
    
    /**
     * The employee whose details are being displayed.
     */
    private final EmployeeProfile employee;
    
    /**
     * Path to the attendance records file for salary computation.
     */
    private final String attendanceFile = "src/main/resources/Employee Attendance Record.tsv";
    
    /**
     * UI components for month selection and salary computation.
     */
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JButton computeSalaryButton;
    private JPanel salaryResultsPanel;
    private JScrollPane salaryScrollPane;

    /**
     * Constructor that initializes the Employee Detail dialog with the specified employee.
     * This method creates a modal dialog that displays employee information and provides
     * month selection for salary computation within the same frame.
     * 
     * @param employee The employee whose details should be displayed
     * @param parentWindow Reference to the parent PayrollGUI window for proper modal behavior
     */
    public EmployeeDetailGUI(EmployeeProfile employee, PayrollGUI parentWindow) {
        super(parentWindow, "Employee Details & Salary Computation - " + employee.getFirstName() + " " + employee.getLastName(), true);
        this.employee = employee;
        this.parentWindow = parentWindow;
        
        // Initialize components and setup the dialog
        initializeComponents();
        setupEventHandlers();
        layoutComponents();
        
        // Configure dialog properties
        setSize(800, 700);
        setLocationRelativeTo(parentWindow);
        setResizable(true);
    }

    /**
     * Initializes all GUI components for the employee details dialog.
     * This method sets up the employee information display, salary computation controls,
     * and results panel with proper configuration and styling.
     */
    private void initializeComponents() {
        // Initialize the salary results panel first (even though it starts empty)
        salaryResultsPanel = new JPanel();
        salaryResultsPanel.setLayout(new BoxLayout(salaryResultsPanel, BoxLayout.Y_AXIS));
        salaryResultsPanel.setBackground(Color.WHITE);
        salaryResultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create scroll pane for salary results (initially empty)
        salaryScrollPane = new JScrollPane(salaryResultsPanel);
        salaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        salaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        salaryScrollPane.setPreferredSize(new Dimension(750, 300));
        salaryScrollPane.setBorder(BorderFactory.createTitledBorder("Salary Calculation Results"));

        // Add a placeholder message in the results panel
        JLabel placeholderLabel = new JLabel("Select a month and click 'Compute Salary' to see results");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.GRAY);
        salaryResultsPanel.add(placeholderLabel);
    }

    /**
     * Sets up the event handlers for user interactions.
     */
    private void setupEventHandlers() {
        // Create the Compute Salary button
        computeSalaryButton = new JButton("Compute Salary");
        computeSalaryButton.setPreferredSize(new Dimension(140, 30));
        computeSalaryButton.setToolTipText("Calculate salary for the selected month and year");
        computeSalaryButton.addActionListener(e -> computeSalaryForSelectedMonth());
    }

    /**
     * Organizes and positions all GUI components within the dialog using
     * appropriate layout managers for optimal user experience and visual appeal.
     */
    private void layoutComponents() {
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
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
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
        monthCombo = new JComboBox<>(months);
        monthCombo.setPreferredSize(new Dimension(120, 30));
        monthCombo.setSelectedIndex(0); // Default to January

        // Create and configure the year selection combo box
        Integer[] years = {2022, 2023, 2024, 2025, 2026};
        yearCombo = new JComboBox<>(years);
        yearCombo.setPreferredSize(new Dimension(80, 30));
        yearCombo.setSelectedIndex(2); // Default to 2024

        // Add labels and components to the panel
        computationPanel.add(new JLabel("Month:"));
        computationPanel.add(monthCombo);
        computationPanel.add(new JLabel("Year:"));
        computationPanel.add(yearCombo);
        computationPanel.add(computeSalaryButton);

        return computationPanel;
    }

    /**
     * Computes the salary for the selected month and displays the results
     * in the same frame along with the employee details.
     */
    private void computeSalaryForSelectedMonth() {
        // Clear previous results
        salaryResultsPanel.removeAll();
        
        // Get selected month and year
        int monthIdx = monthCombo.getSelectedIndex() + 1;
        int year = (Integer) yearCombo.getSelectedItem();
        
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
                
                salaryResultsPanel.add(Box.createVerticalStrut(20));
                salaryResultsPanel.add(errorPanel);
                
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
        
        salaryResultsPanel.add(Box.createVerticalStrut(20));
        salaryResultsPanel.add(errorPanel);
    }
    
    // Force layout update
    salaryResultsPanel.revalidate();
    salaryResultsPanel.repaint();
    
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
    salaryResultsPanel.add(containerPanel);
    salaryResultsPanel.add(Box.createVerticalGlue()); // Push content to top
}

    /**
     * Creates the button panel containing the Close button for dialog navigation.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create Close button
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 30));
        closeButton.addActionListener(e -> dispose());
        
        // Set as default button for Enter key functionality
        getRootPane().setDefaultButton(closeButton);
        
        // Add Escape key binding for quick close
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "CLOSE");
        getRootPane().getActionMap().put("CLOSE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
}