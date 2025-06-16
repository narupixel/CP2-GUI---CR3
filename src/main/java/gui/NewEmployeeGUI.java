package gui;

import dataLoader.LoadEmployeeData;
import models.EmployeeProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * NewEmployeeGUI.java
 * 
 * This class provides a modal dialog interface for adding new employees to the system.
 * The form includes the basic employee information fields as specified in the
 * change request and automatically generates employee numbers to prevent duplicates
 * and ensure consistency. Upon successful submission, the new employee data is
 * appended to the TSV file and the parent employee list is refreshed.
 * 
 * Key Features:
 * - Auto-generates employee numbers for data integrity
 * - Validates all required fields before submission
 * - Provides clear error messages for invalid input
 * - Saves new employee data to the TSV file
 * - Refreshes the parent employee list upon successful addition
 * - Implements proper modal dialog behavior for better user experience
 * 
 * Design Decision: Auto-generated Employee Numbers
 * Employee numbers are automatically generated rather than manually entered because:
 * - Prevents duplicate employee number errors
 * - Reduces user input errors and cognitive load
 * - Maintains consistent 5-digit numbering format
 * - Eliminates the need for users to track the next available number
 * - Follows best practices for primary key generation
 * 
 * @author Payroll System Team
 * @version 1.0
 */
public class NewEmployeeGUI extends JDialog {
    
    // Reference to the parent window for refreshing the employee list
    private final PayrollGUI parentWindow;
    
    // Form input fields for employee information
    private JTextField employeeNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField sssNumberField;
    private JTextField philhealthNumberField;
    private JTextField tinNumberField;
    private JTextField pagibigNumberField;
    
    // Form control buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    // File path to the employee data source
    private final String employeeFile = "src/main/resources/Employee Details.tsv";

    /**
     * Constructor that initializes the New Employee dialog with all necessary components.
     * This method sets up the modal dialog, creates the form fields, generates the next
     * available employee number, and configures all user interface elements.
     * 
     * @param parentWindow Reference to the PayrollGUI that opened this dialog,
     *                    used for refreshing the employee list after successful addition
     */
    public NewEmployeeGUI(PayrollGUI parentWindow) {
        super(parentWindow, "Add New Employee", true); // Create modal dialog
        this.parentWindow = parentWindow;
        
        // Initialize components and setup the dialog
        initializeComponents();
        setupEventHandlers();
        layoutComponents();
        
        // Generate the next employee number after components are initialized
        generateNextEmployeeNumber();
        
        // Configure dialog properties  
        setSize(500, 400);
        setLocationRelativeTo(parentWindow);
        setResizable(false);
    }

    /**
     * Initializes all UI components for the New Employee dialog.
     * This method creates the form fields, labels, and buttons used in the dialog,
     * setting up their properties and default values as needed.
     */
    private void initializeComponents() {
        // Form input fields
        employeeNumberField = new JTextField(20);
        lastNameField = new JTextField(20);
        firstNameField = new JTextField(20);
        sssNumberField = new JTextField(20);
        philhealthNumberField = new JTextField(20);
        tinNumberField = new JTextField(20);
        pagibigNumberField = new JTextField(20);
        
        // Control buttons
        saveButton = new JButton("Save Employee");
        cancelButton = new JButton("Cancel");
        
        // Configure employee number field (editable with distinctive styling)
        employeeNumberField.setEditable(true);
        employeeNumberField.setBackground(new Color(255, 255, 240)); // Light yellow background to indicate auto-generated
        employeeNumberField.setToolTipText("Auto-generated employee number - you can modify if needed");
        
        // Add a subtle border to make it clear it's editable but special
        employeeNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 150), 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
    }

    /**
     * Sets up event handlers for user interactions with the dialog.
     * This method configures action listeners for the Save and Cancel buttons,
     * as well as keyboard shortcuts for improved user experience.
     */
    private void setupEventHandlers() {
        // Save button - validates and saves the new employee
        saveButton.addActionListener(this::saveEmployee);
        
        // Cancel button - closes the dialog without saving
        cancelButton.addActionListener(e -> dispose());
        
        // Add keyboard shortcuts
        getRootPane().setDefaultButton(saveButton); // Enter key triggers save
        
        // Add Escape key binding for cancel
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "CANCEL");
        getRootPane().getActionMap().put("CANCEL", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Lays out all components in the dialog using appropriate layout managers.
     * This method arranges the form fields and buttons in a user-friendly
     * manner, ensuring proper alignment, spacing, and grouping of related elements.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Create and add form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Employee Information"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Define consistent fonts for form elements
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 12);
        
        // Employee Number field (auto-generated and read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel empNumLabel = new JLabel("Employee Number:");
        empNumLabel.setFont(labelFont);
        formPanel.add(empNumLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        employeeNumberField.setFont(fieldFont);
        employeeNumberField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(employeeNumberField, gbc);
        
        // Last Name field (required)
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel lastNameLabel = new JLabel("Last Name: *");
        lastNameLabel.setFont(labelFont);
        formPanel.add(lastNameLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        lastNameField.setFont(fieldFont);
        lastNameField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(lastNameField, gbc);
        
        // First Name field (required)
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel firstNameLabel = new JLabel("First Name: *");
        firstNameLabel.setFont(labelFont);
        formPanel.add(firstNameLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        firstNameField.setFont(fieldFont);
        firstNameField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(firstNameField, gbc);
        
        // SSS Number field (required)
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel sssLabel = new JLabel("SSS Number: *");
        sssLabel.setFont(labelFont);
        formPanel.add(sssLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        sssNumberField.setFont(fieldFont);
        sssNumberField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(sssNumberField, gbc);
        
        // PhilHealth Number field (required)
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel philhealthLabel = new JLabel("PhilHealth Number: *");
        philhealthLabel.setFont(labelFont);
        formPanel.add(philhealthLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        philhealthNumberField.setFont(fieldFont);
        philhealthNumberField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(philhealthNumberField, gbc);
        
        // TIN Number field (required)
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel tinLabel = new JLabel("TIN Number: *");
        tinLabel.setFont(labelFont);
        formPanel.add(tinLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        tinNumberField.setFont(fieldFont);
        tinNumberField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(tinNumberField, gbc);
        
        // Pag-IBIG Number field (required)
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        JLabel pagibigLabel = new JLabel("Pag-IBIG Number: *");
        pagibigLabel.setFont(labelFont);
        formPanel.add(pagibigLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        pagibigNumberField.setFont(fieldFont);
        pagibigNumberField.setPreferredSize(new Dimension(250, 25));
        formPanel.add(pagibigNumberField, gbc);
        
        // Add note about required fields
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER; gbc.weightx = 0.0;
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        noteLabel.setForeground(Color.GRAY);
        formPanel.add(noteLabel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Create and add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Generates the next available employee number by scanning existing employees.
     * This method loads all current employee records, finds the highest employee
     * number, and sets the employee number field to the next sequential number.
     * 
     * Auto-generation Benefits:
     * - Prevents duplicate employee numbers which could cause data integrity issues
     * - Reduces user error by eliminating manual number entry
     * - Maintains consistent 5-digit format (10001, 10002, etc.)
     * - Improves user experience by handling technical details automatically
     */
    private void generateNextEmployeeNumber() {
        try {
            // Load all existing employee records from the TSV file
            List<EmployeeProfile> employees = LoadEmployeeData.loadFromFile(employeeFile);
            
            // Find the highest existing employee number
            int maxEmployeeNumber = employees.stream()
                .mapToInt(emp -> Integer.parseInt(emp.getEmployeeNumber()))
                .max()
                .orElse(10000); // Default to 10000 if no employees exist
            
            // Set the next available number in the form field
            int nextEmployeeNumber = maxEmployeeNumber + 1;
            employeeNumberField.setText(String.valueOf(nextEmployeeNumber));
            
        } catch (Exception e) {
            // Handle any errors in number generation
            JOptionPane.showMessageDialog(this,
                "Error generating employee number: " + e.getMessage(),
                "Number Generation Error",
                JOptionPane.ERROR_MESSAGE);
            
            // Provide a default number if generation fails
            employeeNumberField.setText("10001");
        }
    }

    /**
     * Validates and saves the new employee information to the TSV file.
     * This method performs comprehensive validation of all required fields,
     * formats the data properly for TSV storage, and provides appropriate
     * user feedback for both success and error conditions.
     * 
     * @param e ActionEvent from the Save Employee button click
     */
    private void saveEmployee(ActionEvent e) {
        // Validate all required fields before attempting to save
        if (!validateFields()) {
            return; // Exit if validation fails
        }
        
        try {
            // Prepare the new employee data in TSV format
            String newEmployeeRecord = formatEmployeeRecord();
            
            // Append the new record to the TSV file
            appendToTSVFile(newEmployeeRecord);
            
            // Provide success feedback to the user
            JOptionPane.showMessageDialog(this,
                "Employee added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the parent window's employee list
            String newEmployeeNumber = employeeNumberField.getText().trim(); // Get the generated employee number
            parentWindow.refreshEmployeeDataAndSelect(newEmployeeNumber);
            
            // Close this dialog after successful save
            dispose();
            
        } catch (IOException ex) {
            // Handle file writing errors
            JOptionPane.showMessageDialog(this,
                "Error saving employee data: " + ex.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Handle any other unexpected errors
            JOptionPane.showMessageDialog(this,
                "An unexpected error occurred: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates all required form fields to ensure data completeness and basic format compliance.
     * This method checks each required field for empty values, validates the employee number
     * for uniqueness and proper format, and provides specific error messages to guide the user.
     * 
     * @return true if all validations pass, false if any required field is empty or invalid
     */
    private boolean validateFields() {
        // Check Employee Number field
        if (employeeNumberField.getText().trim().isEmpty()) {
            showValidationError("Employee Number is required.");
            employeeNumberField.requestFocus();
            return false;
        }
        
        // Validate employee number format (should be numeric and reasonable length)
        String empNumber = employeeNumberField.getText().trim();
        try {
            int number = Integer.parseInt(empNumber);
            if (number <= 0) {
                showValidationError("Employee Number must be a positive number.");
                employeeNumberField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showValidationError("Employee Number must be a valid number.");
            employeeNumberField.requestFocus();
            return false;
        }
        
        // Check for duplicate employee number
        if (isEmployeeNumberExists(empNumber)) {
            showValidationError("Employee Number " + empNumber + " already exists. Please choose a different number.");
            employeeNumberField.requestFocus();
            return false;
        }
        
        // Check Last Name field
        if (lastNameField.getText().trim().isEmpty()) {
            showValidationError("Last Name is required.");
            lastNameField.requestFocus();
            return false;
        }
        
        // Check First Name field
        if (firstNameField.getText().trim().isEmpty()) {
            showValidationError("First Name is required.");
            firstNameField.requestFocus();
            return false;
        }
        
        // Check SSS Number field
        if (sssNumberField.getText().trim().isEmpty()) {
            showValidationError("SSS Number is required.");
            sssNumberField.requestFocus();
            return false;
        }
        
        // Check PhilHealth Number field
        if (philhealthNumberField.getText().trim().isEmpty()) {
            showValidationError("PhilHealth Number is required.");
            philhealthNumberField.requestFocus();
            return false;
        }
        
        // Check TIN Number field
        if (tinNumberField.getText().trim().isEmpty()) {
            showValidationError("TIN Number is required.");
            tinNumberField.requestFocus();
            return false;
        }
        
        // Check Pag-IBIG Number field
        if (pagibigNumberField.getText().trim().isEmpty()) {
            showValidationError("Pag-IBIG Number is required.");
            pagibigNumberField.requestFocus();
            return false;
        }
        
        return true; // All validations passed
    }

    /**
     * Displays a standardized validation error message to the user.
     * This helper method ensures consistent error message formatting
     * and user experience across all validation scenarios.
     * 
     * @param message The specific validation error message to display
     */
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Validation Error",
            JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Formats the employee data into a properly structured TSV record.
     * This method creates a tab-separated string containing only the basic
     * employee information fields as specified in the change request.
     * The remaining fields are populated with default or placeholder values
     * to maintain the existing TSV file structure.
     * 
     * @return A formatted TSV string ready for file appending
     */
    private String formatEmployeeRecord() {
        // Extract trimmed values from form fields
        String employeeNumber = employeeNumberField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String sssNumber = sssNumberField.getText().trim();
        String philhealthNumber = philhealthNumberField.getText().trim();
        String tinNumber = tinNumberField.getText().trim();
        String pagibigNumber = pagibigNumberField.getText().trim();
        
        // Create TSV record with provided data and default values for missing fields
        // Note: The remaining fields are set to default values since this form
        // only collects the basic employee information as per change request
        return String.join("\t",
            employeeNumber,           // Employee #
            lastName,                 // Last Name
            firstName,                // First Name
            "TBD",                   // Birthday (To Be Determined)
            "TBD",                   // Address (To Be Determined)
            "TBD",                   // Phone Number (To Be Determined)
            sssNumber,               // SSS #
            philhealthNumber,        // Philhealth #
            tinNumber,               // TIN #
            pagibigNumber,           // Pag-ibig #
            "Probationary",          // Status (Default for new employees)
            "TBD",                   // Position (To Be Determined)
            "TBD",                   // Immediate Supervisor (To Be Determined)
            "0",                     // Basic Salary (Default)
            "0",                     // Rice Subsidy (Default)
            "0",                     // Phone Allowance (Default)
            "0",                     // Clothing Allowance (Default)
            "0",                     // Gross Semi-monthly Rate (Default)
            "0"                      // Hourly Rate (Default)
        );
    }

    /**
     * Appends the new employee record to the TSV file using proper file handling.
     * This method ensures that the new employee data is safely written to the
     * file system with appropriate error handling and resource management.
     * 
     * @param record The formatted TSV record to append to the file
     * @throws IOException If any file writing operation fails
     */
    private void appendToTSVFile(String record) throws IOException {
        // Use try-with-resources to ensure proper file handle cleanup
        try (FileWriter writer = new FileWriter(employeeFile, true)) {
            // Add newline and write the record
            writer.write("\n" + record);
            writer.flush(); // Ensure data is written to disk immediately
        }
        // FileWriter is automatically closed by try-with-resources
    }

    /**
     * Checks if the given employee number already exists in the system.
     * This method prevents duplicate employee numbers which could cause data integrity issues.
     * 
     * @param employeeNumber The employee number to check for existence
     * @return true if the employee number already exists, false otherwise
     */
    private boolean isEmployeeNumberExists(String employeeNumber) {
        try {
            // Load all existing employee records from the TSV file
            List<EmployeeProfile> employees = LoadEmployeeData.loadFromFile(employeeFile);
            
            // Check if any employee has the same number
            return employees.stream()
                .anyMatch(emp -> emp.getEmployeeNumber().equals(employeeNumber));
                
        } catch (Exception e) {
            // If we can't load employees, assume it doesn't exist to allow the save to proceed
            // The save operation will handle any file access errors
            return false;
        }
    }
}