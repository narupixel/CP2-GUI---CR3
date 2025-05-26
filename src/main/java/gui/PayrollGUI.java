package gui;

import dataLoader.LoadEmployeeData;
import dataLoader.LoadTimeSheet;
import models.EmployeeProfile;
import models.TimeLog;
import models.WeeklyTotals;
import payrollCalculations.CalculateWeeklyTotals;
import payrollCalculations.CalculateAndDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PayrollGUI extends JFrame {
    // GUI components
    private JTextField employeeNumberField;
    private JButton calculateButton;
    private JPanel detailsPanel;

    private EmployeeProfile selectedEmployee;
    private List<EmployeeProfile> employees;
    private String attendanceFile = "src/main/resources/Employee Attendance Record.tsv";

    public PayrollGUI() {
        // Load employees at startup
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");

        setTitle("Payroll System");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: Employee number, month, year, and calculate button
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Employee Number:"));
        employeeNumberField = new JTextField(10);
        searchPanel.add(employeeNumberField);
        // Month dropdown
        String[] months = java.util.Arrays.stream(java.time.Month.values())
            .map(m -> String.format("%02d - %s", m.getValue(), m.name().substring(0,1) + m.name().substring(1).toLowerCase()))
            .toArray(String[]::new);
        JComboBox<String> monthCombo = new JComboBox<>(months);
        searchPanel.add(new JLabel("Month:"));
        searchPanel.add(monthCombo);
        // Year dropdown (current year +/- 5 years)
        int currentYear = java.time.Year.now().getValue();
        Integer[] years = new Integer[11];
        for (int i = 0; i < 11; i++) years[i] = currentYear - 5 + i;
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(currentYear);
        searchPanel.add(new JLabel("Year:"));
        searchPanel.add(yearCombo);
        // Calculate button
        calculateButton = new JButton("Calculate");
        searchPanel.add(calculateButton);
        add(searchPanel, BorderLayout.NORTH);

        // Center panel: Details and pay coverage
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        // detailsLabel = new JLabel("Enter an employee number and click Search.");
        // detailsPanel.add(detailsLabel);

        // Output area (replaced by resultsPanel)
        // outputArea = new JTextArea(12, 50);
        // outputArea.setEditable(false);
        // scrollPane = new JScrollPane(outputArea);
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setPreferredSize(new Dimension(550, 250));
        detailsPanel.add(resultsScrollPane);

        add(detailsPanel, BorderLayout.CENTER);

        // Always show pay coverage and output for debugging
        resultsScrollPane.setVisible(true);
        calculateButton.setEnabled(true);

        // Action: Calculate payroll for selected month
        calculateButton.addActionListener((ActionEvent e) -> {
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            resultsScrollPane.setVisible(true);
            String empNum = employeeNumberField.getText().trim();
            selectedEmployee = employees.stream()
                    .filter(emp -> emp.getEmployeeNumber().equals(empNum))
                    .findFirst().orElse(null);
            if (selectedEmployee == null) {
                JLabel noEmp = new JLabel("No employee found for number: " + empNum);
                noEmp.setForeground(Color.RED);
                resultsPanel.add(noEmp);
                // detailsLabel.setText("Enter a valid employee number.");
                resultsPanel.revalidate();
                return;
            }
            // Show Employee Number, Name, and Birthday at the top of the resultsPanel, fully left-aligned
            String birthday = selectedEmployee.getBirthday();
            JPanel empPanel = new JPanel();
            empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.Y_AXIS));
            empPanel.setBackground(Color.WHITE);
            empPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel empNumLabel = new JLabel("Employee Number: " + selectedEmployee.getEmployeeNumber());
            empNumLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            empNumLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel nameLabel = new JLabel("Name: " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel bdayLabel = new JLabel("Birthday: " + birthday);
            bdayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            bdayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel posLabel = new JLabel("Position: " + selectedEmployee.getPosition());
            posLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            posLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            empPanel.add(empNumLabel);
            empPanel.add(nameLabel);
            empPanel.add(bdayLabel);
            empPanel.add(posLabel);
            empPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
            resultsPanel.add(empPanel);
            resultsPanel.add(Box.createVerticalStrut(5));
            int monthIdx = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            LocalDate startDate = LocalDate.of(year, monthIdx, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            try {                List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, selectedEmployee.getEmployeeNumber());
                // Check if there are any records for the selected year
                LocalDate yearStart = LocalDate.of(year, 1, 1);
                LocalDate yearEnd = LocalDate.of(year, 12, 31);
                List<TimeLog> yearLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(yearStart) && !log.getDate().isAfter(yearEnd))
                        .toList();
                  // Filter for just the selected month
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
                    resultsPanel.add(noRecords);
                } else {
                    // Monthly aggregation
                    double totalHours = filteredLogs.stream().mapToDouble(TimeLog::getHoursWorked).sum();
                    double totalOvertime = filteredLogs.stream().mapToDouble(TimeLog::getOvertime).sum();
                    double rice = selectedEmployee.getRiceSubsidy();
                    double phone = selectedEmployee.getPhoneAllowance();
                    double clothing = selectedEmployee.getClothingAllowance();
                    double totalAllowances = rice + phone + clothing;
                    double basicGrossMonthlyPay = totalHours * selectedEmployee.getHourlyRate();
                    double grossMonthlyPay = basicGrossMonthlyPay + totalAllowances;
                    double pagibig = governmentContributions.CalculatePagibig.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double philhealth = governmentContributions.CalculatePhilhealth.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double sss = governmentContributions.CalculateSss.computeFromWeekly(basicGrossMonthlyPay / 4.0);
                    double withholdingTax = governmentContributions.CalculateWithholdingTax.compute(basicGrossMonthlyPay);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollGUI gui = new PayrollGUI();
            gui.setVisible(true);
        });
    }
}