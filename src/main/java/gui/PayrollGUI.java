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
    private JButton searchButton;
    private JPanel detailsPanel;
    private JLabel detailsLabel;

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton calculateButton;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;

    private EmployeeProfile selectedEmployee;
    private List<EmployeeProfile> employees;
    private String attendanceFile = "src/main/resources/Employee Attendance Record.tsv";

    public PayrollGUI() {
        // Load employees at startup
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");

        setTitle("Payroll System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: Employee search
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Employee Number:"));
        employeeNumberField = new JTextField(10);
        searchPanel.add(employeeNumberField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Center panel: Details and pay coverage
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsLabel = new JLabel("Enter an employee number and click Search.");
        detailsPanel.add(detailsLabel);

        // Date pickers for pay coverage
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(new JLabel("Pay Coverage Start:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "MM/dd/yyyy");
        startDateSpinner.setEditor(startEditor);
        datePanel.add(startDateSpinner);

        datePanel.add(new JLabel("End:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "MM/dd/yyyy");
        endDateSpinner.setEditor(endEditor);
        datePanel.add(endDateSpinner);

        calculateButton = new JButton("Calculate");
        datePanel.add(calculateButton);

        detailsPanel.add(datePanel);

        // Output area (replaced by resultsPanel)
        // outputArea = new JTextArea(12, 50);
        // outputArea.setEditable(false);
        // scrollPane = new JScrollPane(outputArea);
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setPreferredSize(new Dimension(550, 250));
        detailsPanel.add(resultsScrollPane);

        add(detailsPanel, BorderLayout.CENTER);

        // Always show pay coverage and output for debugging
        datePanel.setVisible(true);
        resultsScrollPane.setVisible(true);
        calculateButton.setEnabled(true);

        // Action: Search employee
        searchButton.addActionListener((ActionEvent e) -> {
            String empNum = employeeNumberField.getText().trim();
            selectedEmployee = employees.stream()
                    .filter(emp -> emp.getEmployeeNumber().equals(empNum))
                    .findFirst().orElse(null);

            if (selectedEmployee != null) {
                detailsLabel.setText("<html><b>Employee:</b> " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName()
                        + "<br><b>Position:</b> " + selectedEmployee.getPosition()
                        + "<br><b>Status:</b> " + selectedEmployee.getStatus() + "</html>");
                datePanel.setVisible(true);
                resultsScrollPane.setVisible(true); // <-- change from false to true
                resultsPanel.removeAll();
                resultsPanel.revalidate();
                resultsPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                detailsLabel.setText("Enter an employee number and click Search.");
                datePanel.setVisible(false);
                resultsScrollPane.setVisible(false);
                resultsPanel.removeAll();
                resultsPanel.revalidate();
                resultsPanel.repaint();
            }
        });

        // Action: Calculate payroll for date range
        calculateButton.addActionListener((ActionEvent e) -> {
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            resultsScrollPane.setVisible(true);
            if (selectedEmployee == null) {
                JLabel noEmp = new JLabel("No employee selected.");
                noEmp.setForeground(Color.RED);
                resultsPanel.add(noEmp);
                resultsPanel.revalidate();
                return;
            }
            try {
                LocalDate startDate = ((Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = ((Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<TimeLog> logs = LoadTimeSheet.loadForEmployee(attendanceFile, selectedEmployee.getEmployeeNumber());
                List<TimeLog> filteredLogs = logs.stream()
                        .filter(log -> !log.getDate().isBefore(startDate) && !log.getDate().isAfter(endDate))
                        .toList();
                if (filteredLogs.isEmpty()) {
                    JLabel noRecords = new JLabel("No attendance records found for this employee in the selected date range.");
                    noRecords.setForeground(Color.RED);
                    resultsPanel.add(noRecords);
                } else {
                    List<WeeklyTotals> weeklyTotalsList = CalculateWeeklyTotals.calculateWeeklyTotals(selectedEmployee, filteredLogs);
                    for (WeeklyTotals weeklyTotals : weeklyTotalsList) {
                        JPanel weekPanel = new JPanel();
                        weekPanel.setLayout(new GridLayout(0, 2, 8, 2));
                        weekPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200,200,200)),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                        weekPanel.setBackground(Color.WHITE);
                        // Minimalist, professional font
                        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
                        Font boldFont = new Font("SansSerif", Font.BOLD, 14);
                        // Header
                        JLabel header = new JLabel("Payroll Summary for Week " + weeklyTotals.getWeekNumber());
                        header.setFont(boldFont);
                        header.setForeground(new Color(30, 30, 30));
                        weekPanel.add(header);
                        weekPanel.add(new JLabel(""));
                        weekPanel.add(new JLabel("Pay Period:"));
                        weekPanel.add(new JLabel(weeklyTotals.getPeriodStart() + " to " + weeklyTotals.getPeriodEnd()));
                        double rice = selectedEmployee.getRiceSubsidy();
                        double phone = selectedEmployee.getPhoneAllowance();
                        double clothing = selectedEmployee.getClothingAllowance();
                        double totalAllowances = rice + phone + clothing;
                        double grossWeeklyPay = weeklyTotals.getTotalHoursWorked() * selectedEmployee.getHourlyRate() + totalAllowances;
                        double pagibig = governmentContributions.CalculatePagibig.computeFromWeekly(grossWeeklyPay);
                        double philhealth = governmentContributions.CalculatePhilhealth.computeFromWeekly(grossWeeklyPay);
                        double sss = governmentContributions.CalculateSss.computeFromWeekly(grossWeeklyPay);
                        double withholdingTax = governmentContributions.CalculateWithholdingTax.compute(grossWeeklyPay);
                        double totalDeductions = pagibig + philhealth + sss + withholdingTax;
                        double netWeeklyPay = grossWeeklyPay - totalDeductions;
                        weekPanel.add(new JLabel("Gross Weekly Pay:"));
                        weekPanel.add(new JLabel(String.format("%.2f", grossWeeklyPay)));
                        weekPanel.add(new JLabel("Rice Subsidy:"));
                        weekPanel.add(new JLabel(String.format("%.2f", rice)));
                        weekPanel.add(new JLabel("Phone Allowance:"));
                        weekPanel.add(new JLabel(String.format("%.2f", phone)));
                        weekPanel.add(new JLabel("Clothing Allowance:"));
                        weekPanel.add(new JLabel(String.format("%.2f", clothing)));
                        weekPanel.add(new JLabel("Total Allowances:"));
                        weekPanel.add(new JLabel(String.format("%.2f", totalAllowances)));
                        weekPanel.add(new JLabel("Pag-IBIG:"));
                        weekPanel.add(new JLabel(String.format("%.2f", pagibig)));
                        weekPanel.add(new JLabel("PhilHealth:"));
                        weekPanel.add(new JLabel(String.format("%.2f", philhealth)));
                        weekPanel.add(new JLabel("SSS:"));
                        weekPanel.add(new JLabel(String.format("%.2f", sss)));
                        weekPanel.add(new JLabel("Withholding Tax:"));
                        weekPanel.add(new JLabel(String.format("%.2f", withholdingTax)));
                        weekPanel.add(new JLabel("Total Deductions:"));
                        weekPanel.add(new JLabel(String.format("%.2f", totalDeductions)));
                        weekPanel.add(new JLabel("Total Hours Worked:"));
                        weekPanel.add(new JLabel(String.format("%.2f", weeklyTotals.getTotalHoursWorked())));
                        weekPanel.add(new JLabel("Total Overtime:"));
                        weekPanel.add(new JLabel(String.format("%.2f", weeklyTotals.getTotalOvertime())));
                        // Net Weekly Pay - bold, green, bottom
                        JLabel netPayLabel = new JLabel("NET WEEKLY PAY:");
                        netPayLabel.setFont(boldFont);
                        netPayLabel.setForeground(new Color(0, 128, 0));
                        JLabel netPayValue = new JLabel(String.format("%.2f", netWeeklyPay));
                        netPayValue.setFont(boldFont);
                        netPayValue.setForeground(new Color(0, 128, 0));
                        weekPanel.add(netPayLabel);
                        weekPanel.add(netPayValue);
                        resultsPanel.add(Box.createVerticalStrut(10));
                        resultsPanel.add(weekPanel);
                    }
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