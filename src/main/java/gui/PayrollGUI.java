package gui;

// filepath: payroll-system/payroll-system/src/main/java/gui/PayrollGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayrollGUI extends JFrame {
    // GUI components
    private JTextField employeeNumberField;
    private JTextField employeeNameField;
    private JTextField birthdayField;
    private JTextField hoursWorkedField;
    private JTextArea outputArea;

    public PayrollGUI() {
        // Set up the frame
        setTitle("Payroll System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Initialize components
        employeeNumberField = new JTextField();
        employeeNameField = new JTextField();
        birthdayField = new JTextField();
        hoursWorkedField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        // Add components to the frame
        add(new JLabel("Employee Number:"));
        add(employeeNumberField);
        add(new JLabel("Employee Name:"));
        add(employeeNameField);
        add(new JLabel("Birthday:"));
        add(birthdayField);
        add(new JLabel("Hours Worked:"));
        add(hoursWorkedField);

        JButton calculateButton = new JButton("Calculate Salary");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateSalary();
            }
        });
        add(calculateButton);
        add(new JScrollPane(outputArea));

        // Make the GUI visible
        setVisible(true);
    }

    private void calculateSalary() {
        // Retrieve input values
        String employeeNumber = employeeNumberField.getText();
        String employeeName = employeeNameField.getText();
        String birthday = birthdayField.getText();
        int hoursWorked = Integer.parseInt(hoursWorkedField.getText());

        // Here you would typically call the PayrollCalculator to perform calculations
        // For demonstration, we will just display the input values
        outputArea.setText("Employee Number: " + employeeNumber + "\n" +
                           "Employee Name: " + employeeName + "\n" +
                           "Birthday: " + birthday + "\n" +
                           "Hours Worked: " + hoursWorked + "\n" +
                           "Gross Salary: " + (hoursWorked * 20) + "\n" + // Example calculation
                           "Net Salary: " + (hoursWorked * 20 * 0.8)); // Example deduction
    }

    public static void main(String[] args) {
        // Start the GUI application
        SwingUtilities.invokeLater(() -> new PayrollGUI());
    }
}