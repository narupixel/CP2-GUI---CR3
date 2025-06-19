package gui;

import models.EmployeeProfile;
import dataLoader.LoadEmployeeData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollGUI.java
 * 
 * This class implements the main graphical user interface for the MotorPH Employee Management System.
 * The interface displays all employee records in a sortable table format, allowing users to view,
 * select, and manage employee information efficiently. Users can view detailed employee information
 * with payroll calculations, add new employees, and navigate through the system intuitively.
 * 
 * Key Features:
 * - Display all employees in a sortable table with key information
 * - Select employees to view detailed information and calculate payroll
 * - Add new employee records with validation and CSV file integration
 * - Update and delete existing employee records with confirmation dialogs
 * - Intuitive navigation and user-friendly interface design
 * - Real-time table updates and data synchronization
 * 
 * @author MotorPH Payroll System Team
 * @version 3.0 - Enhanced with employee update and delete functionalities
 */

public class PayrollGUI extends JFrame {
    
    /**
     * GUI components used throughout the interface for employee management and display.
     */
    private JTable employeeTable;                    // Main table displaying all employee records
    private DefaultTableModel tableModel;           // Model for managing table data and updates
    private JButton viewEmployeeButton;              // Button to view selected employee details
    private JButton newEmployeeButton;               // Button to create new employee records
    private JButton updateEmployeeButton;            // Button to update selected employee record
    private JButton deleteEmployeeButton;            // Button to delete selected employee record
    private TableRowSorter<DefaultTableModel> sorter; // Sorter for enabling table column sorting

    // Add search components
    private JTextField searchField;                  // Search field for employee number
    private JButton searchButton;                    // Button to execute search
    
    /**
     * Employee editing components for update functionality.
     * Only includes the essential identification fields.
     */
    private JTextField employeeNumberField;          // Field for employee number (read-only)
    private JTextField lastNameField;                // Field for last name
    private JTextField firstNameField;               // Field for first name
    private JTextField sssNumberField;               // Field for SSS number
    private JTextField philhealthNumberField;        // Field for PhilHealth number
    private JTextField tinNumberField;               // Field for TIN number
    private JTextField pagibigNumberField;           // Field for Pag-IBIG number

    /**
     * Data components used for employee management and system operations.
     */
    private List<EmployeeProfile> employees;         // List of all employees loaded from the system
    private EmployeeProfile selectedEmployee;       // Currently selected employee from the table

    /**
     * Constructor that initializes the main Employee Management GUI and sets up all components.
     * This method loads employee data from the file system, creates the employee table with
     * sorting capabilities, configures action buttons, and establishes the overall layout
     * for optimal user experience and intuitive navigation.
     */
    public PayrollGUI() {
        // Initialize collections first
        employees = new ArrayList<>();
        
        // Set up the main window
        setTitle("Payroll Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize all GUI components FIRST (including table model)
        initializeComponents();
        layoutComponents();
        
        // THEN load employee data after table model is initialized
        loadEmployeeData();
        
        // Configure window properties
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Initializes all GUI components required for the payroll management interface.
     * Sets up the main table, buttons, editing fields, and their initial configurations
     * for optimal user experience and system functionality.
     */
    private void initializeComponents() {
        // Initialize employee management components
        initializeTable();
        initializeButtons();
        initializeEditingFields();
        
        // Set up table selection listener to populate editing fields
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Convert view row to model row (in case of sorting)
                    int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
                    String employeeNumber = (String) tableModel.getValueAt(modelRow, 0);
                    
                    // Find the selected employee
                    selectedEmployee = employees.stream()
                        .filter(emp -> emp.getEmployeeNumber().equals(employeeNumber))
                        .findFirst()
                        .orElse(null);
                    
                    // Populate editing fields with selected employee data
                    if (selectedEmployee != null) {
                        populateEditingFields(selectedEmployee);
                    }
                } else {
                    // No selection - clear fields
                    selectedEmployee = null;
                    clearEditingFields();
                }
                
                // Update button states based on selection
                updateButtonStates();
            }
        });
    }

    /**
     * Initializes the main application buttons and their configurations.
     * Sets up all primary action buttons with appropriate text, tooltips,
     * and initial states for optimal user experience.
     */
    private void initializeButtons() {
        // Employee management buttons
        viewEmployeeButton = new JButton("View Details");
        viewEmployeeButton.setEnabled(false);
        viewEmployeeButton.setToolTipText("View detailed information for the selected employee");

        newEmployeeButton = new JButton("New Employee");
        newEmployeeButton.setToolTipText("Add a new employee to the system");

        updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setEnabled(false);
        updateEmployeeButton.setToolTipText("Update information for the selected employee");

        deleteEmployeeButton = new JButton("Delete Employee");
        deleteEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setToolTipText("Delete the selected employee from the system");

        // Search components
        searchField = new JTextField(15);
        searchField.setToolTipText("Enter employee number to search and view details");
        searchField.addActionListener(e -> searchAndViewEmployee()); // Allow Enter key to search
        
        searchButton = new JButton("Search & View");
        searchButton.setToolTipText("Search for employee by number and open their details");
        
        // Add event listeners
        viewEmployeeButton.addActionListener(e -> viewEmployeeDetails());
        newEmployeeButton.addActionListener(e -> openNewEmployeeDialog());
        updateEmployeeButton.addActionListener(e -> updateEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteEmployee());
        searchButton.addActionListener(e -> searchAndViewEmployee());
    }

    /**
     * Initializes all text fields used for editing employee information.
     * Sets up field properties, tooltips, and default configurations for
     * optimal user experience during employee data entry and modification.
     */
    private void initializeEditingFields() {
        // Create text fields for essential employee attributes only
        employeeNumberField = new JTextField(15);
        employeeNumberField.setEditable(false); // Read-only as requested by client
        employeeNumberField.setBackground(Color.LIGHT_GRAY); // Grey background to indicate read-only
        employeeNumberField.setToolTipText("Employee number - read-only field");
        
        employeeNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));

        lastNameField = new JTextField(15);
        lastNameField.setToolTipText("Enter employee's last name");

        firstNameField = new JTextField(15);
        firstNameField.setToolTipText("Enter employee's first name");

        sssNumberField = new JTextField(15);
        sssNumberField.setToolTipText("Enter SSS number");

        philhealthNumberField = new JTextField(15);
        philhealthNumberField.setToolTipText("Enter PhilHealth number");

        tinNumberField = new JTextField(15);
        tinNumberField.setToolTipText("Enter TIN number");

        pagibigNumberField = new JTextField(15);
        pagibigNumberField.setToolTipText("Enter Pag-IBIG number");

        addChangeListeners();
    }

    /**
     * Adds change listeners to all editable text fields to track when data has been modified.
     * Employee number field is excluded since it's read-only.
     */
    private void addChangeListeners() {
        JTextField[] editableFields = {
            lastNameField, firstNameField, sssNumberField, 
            philhealthNumberField, tinNumberField, pagibigNumberField
        };

        for (JTextField field : editableFields) {
            field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) { 
                    // Data modification tracking can be added here if needed
                }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) { 
                    // Data modification tracking can be added here if needed
                }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) { 
                    // Data modification tracking can be added here if needed
                }
            });
        }
    }

    /**
     * Populates the employee table with data from the loaded employee list.
     * Extracts the required fields from each employee profile and adds them
     * as rows to the table model for display to the user.
     */
    private void setupTableData() {
        // Clear any existing data in the table before adding new records
        tableModel.setRowCount(0);

        // Iterate through all loaded employees and add their information to the table
        for (EmployeeProfile employee : employees) {
            // Create a row array with the specified employee information fields
            Object[] rowData = {
                employee.getEmployeeNumber(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getSssNumber(),
                employee.getPhilhealthNumber(),
                employee.getTinNumber(),
                employee.getPagibigNumber()
            };
            
            // Add the employee data row to the table model
            tableModel.addRow(rowData);
        }
    }

    /**
     * Populates the editing fields with data from the specified employee.
     * This method is called when an employee is selected from the table to
     * display their essential information in the editing form.
     * 
     * @param employee The employee whose data should be displayed in the fields
     */
    private void populateEditingFields(EmployeeProfile employee) {
        if (employee != null) {
            employeeNumberField.setText(employee.getEmployeeNumber());
            lastNameField.setText(employee.getLastName());
            firstNameField.setText(employee.getFirstName());
            sssNumberField.setText(employee.getSssNumber());
            philhealthNumberField.setText(employee.getPhilhealthNumber());
            tinNumberField.setText(employee.getTinNumber());
            pagibigNumberField.setText(employee.getPagibigNumber());
        }
    }

    /**
     * Clears all editing fields when no employee is selected.
     * This provides a clean state for the editing form.
     */
    private void clearEditingFields() {
        JTextField[] fields = {
            employeeNumberField, lastNameField, firstNameField, 
            sssNumberField, philhealthNumberField, tinNumberField, pagibigNumberField
        };

        for (JTextField field : fields) {
            field.setText("");
        }
    }

    /**
     * Creates the employee editing panel with only the essential text fields organized in a grid layout.
     * This panel allows users to view and modify selected employee information for the core identification
     * fields: Employee Number, Last Name, First Name, SSS Number, PhilHealth Number, TIN, and Pag-IBIG Number.
     * 
     * @return JPanel containing the essential employee editing fields
     */
    private JPanel createEditingPanel() {
        JPanel editingPanel = new JPanel(new GridBagLayout());
        editingPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        editingPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Employee Number, Last Name, First Name
        gbc.gridx = 0; gbc.gridy = 0;
        editingPanel.add(new JLabel("Employee Number:"), gbc);
        gbc.gridx = 1;
        editingPanel.add(employeeNumberField, gbc);
        
        gbc.gridx = 2;
        editingPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        editingPanel.add(lastNameField, gbc);
        
        gbc.gridx = 4;
        editingPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 5;
        editingPanel.add(firstNameField, gbc);

        // Row 2: SSS Number, PhilHealth Number, TIN
        gbc.gridx = 0; gbc.gridy = 1;
        editingPanel.add(new JLabel("SSS Number:"), gbc);
        gbc.gridx = 1;
        editingPanel.add(sssNumberField, gbc);
        
        gbc.gridx = 2;
        editingPanel.add(new JLabel("PhilHealth Number:"), gbc);
        gbc.gridx = 3;
        editingPanel.add(philhealthNumberField, gbc);
        
        gbc.gridx = 4;
        editingPanel.add(new JLabel("TIN:"), gbc);
        gbc.gridx = 5;
        editingPanel.add(tinNumberField, gbc);

        // Row 3: Pag-IBIG Number (centered)
        gbc.gridx = 1; gbc.gridy = 2;
        editingPanel.add(new JLabel("Pag-IBIG Number:"), gbc);
        gbc.gridx = 2;
        editingPanel.add(pagibigNumberField, gbc);

        return editingPanel;
    }

    /**
     * Organizes and positions all GUI components within the main window using
     * appropriate layout managers. Creates panels for logical grouping of
     * related components and ensures proper spacing and alignment.
     */
    private void layoutComponents() {
        // Create the top panel for action buttons with proper spacing and alignment
        JPanel buttonPanel = createButtonPanel();

        // Create a title label for the main window to provide context
        JLabel titleLabel = new JLabel("Employee Records");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));

        // Create the table panel with scrolling capability for large datasets
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        tableScrollPane.setPreferredSize(new Dimension(1150, 300));
        
        // Configure scroll pane properties for optimal viewing experience
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Create the main content panel to organize the table and editing components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Add components to the main panel with appropriate positioning
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Create and add the employee editing panel
        JPanel editingPanel = createEditingPanel();
        JScrollPane editingScrollPane = new JScrollPane(editingPanel);
        editingScrollPane.setPreferredSize(new Dimension(1150, 250));
        editingScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        mainPanel.add(editingScrollPane, BorderLayout.SOUTH);

        // Add all panels to the main frame with proper layout positioning
        add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Create a status panel for employee count and system information
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(240, 240, 240));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("Total Employees: " + employees.size());
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        
        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates and returns the button panel containing all primary action buttons.
     * Organizes buttons using FlowLayout for intuitive user navigation and clear
     * visual separation of different functional areas.
     * 
     * @return JPanel containing all configured action buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);
        
        // Add search components first
        buttonPanel.add(new JLabel("Quick Search:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);
        
        // Add a separator
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 25));
        buttonPanel.add(separator);
        
        // Add employee management buttons
        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(newEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        
        return buttonPanel;
    }

    /**
     * Initializes the main employee table with appropriate columns and configurations.
     * Sets up the table model, column headers, and basic table properties for
     * optimal display and interaction with employee data.
     */
    private void initializeTable() {
        // Define column headers for the employee table
        String[] columnNames = {
            "Employee Number", "Last Name", "First Name", 
            "SSS Number", "PhilHealth Number", "TIN Number", "Pag-IBIG Number"
        };
        
        // Create table model with defined columns
        tableModel = new DefaultTableModel(columnNames, 0);
        
        // Create the table with the model
        employeeTable = new JTable(tableModel);
        
        // Configure table properties
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        
        // Set up table sorting
        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);
        
        // Configure column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Employee Number
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Last Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(150); // First Name
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(120); // SSS Number
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(130); // PhilHealth Number
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(120); // TIN Number
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(130); // Pag-IBIG Number
    }

    /**
     * Opens the employee details window for the currently selected employee.
     * Creates a new EmployeeDetailGUI instance and displays it while keeping
     * the main window open in the background for continued navigation.
     */
    private void viewEmployeeDetails() {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an employee to view details.", 
                "No Selection", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Fix: Correct parameter order (parent first, then employee)
        EmployeeDetailGUI detailDialog = new EmployeeDetailGUI(this, selectedEmployee);
        detailDialog.setVisible(true);
    }

    /**
     * Opens the New Employee dialog for adding a new employee to the system.
     * This method creates and displays the NewEmployeeGUI dialog, allowing users
     * to input information for a new employee record.
     */
    private void openNewEmployeeDialog() {
        try {
            // Create and display the New Employee dialog
            NewEmployeeGUI newEmployeeDialog = new NewEmployeeGUI(this);
            newEmployeeDialog.setVisible(true);
            
            // After the dialog is closed, refresh the employee data to show any new additions
            // This will happen automatically when the dialog calls refreshEmployeeData() on success
            
        } catch (Exception e) {
            // Handle any errors in opening the dialog
            JOptionPane.showMessageDialog(this, 
                "Error opening New Employee dialog:\n" + e.getMessage(), 
                "Dialog Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the selected employee record with the data from the editing fields.
     * Validates all required fields, confirms the update operation, and saves
     * the changes to the TSV file before refreshing the display.
     */
    private void updateEmployee() {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an employee to update.", 
                "No Employee Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate required fields before updating
        if (!validateEmployeeData()) {
            return;
        }

        // Show confirmation dialog with employee details (removed Current Position line)
        String confirmMessage = String.format(
            "Are you sure you want to update the record for:\n\n" +
            "Employee: %s %s (%s)\n\n" +
            "This action will modify the employee's information in the system.",
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            employeeNumberField.getText().trim()
        );

        int result = JOptionPane.showConfirmDialog(this, 
            confirmMessage, 
            "Confirm Employee Update", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            try {
                // Update the employee object with new data
                updateEmployeeObject();
                
                // Save changes to the TSV file
                saveEmployeesToFile();
                
                // Refresh the table display
                refreshEmployeeData();
                
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "Employee record updated successfully!", 
                    "Update Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error updating employee record:\n" + e.getMessage(), 
                    "Update Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Deletes the selected employee record from the system.
     * Shows a confirmation dialog with employee details before performing
     * the deletion operation and updates the file and display accordingly.
     */
    private void deleteEmployee() {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select an employee to delete.", 
                "No Employee Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Store employee information before deletion for the success message
        String employeeFirstName = selectedEmployee.getFirstName();
        String employeeLastName = selectedEmployee.getLastName();
        String employeeNumber = selectedEmployee.getEmployeeNumber();

        // Show confirmation dialog with employee details (removed Position line)
        String confirmMessage = String.format(
            "Are you sure you want to delete the record for:\n\n" +
            "Employee: %s %s (%s)\n\n" +
            "This action cannot be undone!",
            employeeFirstName,
            employeeLastName,
            employeeNumber
        );

        int result = JOptionPane.showConfirmDialog(this, 
            confirmMessage, 
            "Confirm Employee Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            try {
                // Remove the employee from the list
                employees.removeIf(emp -> emp.getEmployeeNumber().equals(selectedEmployee.getEmployeeNumber()));
                
                // Save changes to the TSV file
                saveEmployeesToFile();
                
                // Refresh the table display (this will set selectedEmployee to null)
                refreshEmployeeData();
                
                // Show success message using the stored employee information
                JOptionPane.showMessageDialog(this, 
                    String.format("Employee %s %s has been deleted successfully!", 
                        employeeFirstName, employeeLastName), 
                    "Deletion Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting employee record:\n" + e.getMessage(), 
                    "Deletion Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Validates all employee data entered in the editing fields.
     * Checks for required fields and proper data formats for essential employee information.
     * 
     * @return true if all data is valid, false otherwise
     */
    private boolean validateEmployeeData() {
        // Employee number validation removed since it's read-only
        
        // Check required string fields
        if (lastNameField.getText().trim().isEmpty()) {
            showValidationError("Last name is required.");
            lastNameField.requestFocus();
            return false;
        }

        if (firstNameField.getText().trim().isEmpty()) {
            showValidationError("First name is required.");
            firstNameField.requestFocus();
            return false;
        }

        // SSS, PhilHealth, TIN, and Pag-IBIG numbers can be optional or validated as needed
        // Add specific validation rules here if required

        return true;
    }

    /**
     * Displays a validation error message to the user.
     * 
     * @param message The validation error message to display
     */
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates the selected employee record by creating a new EmployeeProfile object
     * with the updated data and replacing it in the employee list.
     * This method handles the immutable nature of the EmployeeProfile class.
     */
    private void updateEmployeeObject() {
        if (selectedEmployee != null) {
            // Create a new EmployeeProfile object with updated data
            EmployeeProfile updatedEmployee = new EmployeeProfile(
                selectedEmployee.getEmployeeNumber(), // Keep the original employee number (read-only)
                lastNameField.getText().trim(),       // Fix: changed from setText() to getText()
                firstNameField.getText().trim(),      // Fix: changed from setText() to getText()
                selectedEmployee.getBirthday(), // Keep existing birthday
                selectedEmployee.getAddress(), // Keep existing address
                selectedEmployee.getPhoneNumber(), // Keep existing phone
                sssNumberField.getText().trim(),
                philhealthNumberField.getText().trim(),
                tinNumberField.getText().trim(),
                pagibigNumberField.getText().trim(),
                selectedEmployee.getStatus(), // Keep existing status
                selectedEmployee.getPosition(), // Keep existing position
                selectedEmployee.getImmediateSupervisor(), // Keep existing supervisor
                selectedEmployee.getBasicSalary(), // Keep existing salary
                selectedEmployee.getRiceSubsidy(), // Keep existing rice subsidy
                selectedEmployee.getPhoneAllowance(), // Keep existing phone allowance
                selectedEmployee.getClothingAllowance(), // Keep existing clothing allowance
                selectedEmployee.getGrossSemiMonthlyRate(), // Keep existing gross rate
                selectedEmployee.getHourlyRate() // Keep existing hourly rate
            );
            
            // Find and replace the employee in the list
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeNumber().equals(selectedEmployee.getEmployeeNumber())) {
                    employees.set(i, updatedEmployee);
                    selectedEmployee = updatedEmployee; // Update the reference
                    break;
                }
            }
        }
    }

    /**
     * Saves the current employee list to the TSV file.
     * This method writes all employee data back to the file system
     * to persist any changes made during the session.
     * 
     * @throws IOException if there are issues writing to the file
     */
    private void saveEmployeesToFile() throws IOException {
        File employeeFile = new File("src/main/resources/Employee Details.tsv");
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(employeeFile))) {
            // Write the header row
            writer.println("Employee Number\tLast Name\tFirst Name\tBirthday\tAddress\tPhone Number\t" +
                          "SSS Number\tPhilHealth Number\tTIN Number\tPag-IBIG Number\tStatus\tPosition\t" +
                          "Immediate Supervisor\tBasic Salary\tRice Subsidy\tPhone Allowance\t" +
                          "Clothing Allowance\tGross Semi-monthly Rate\tHourly Rate");
            
            // Write each employee's data
            for (EmployeeProfile employee : employees) {
                writer.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f",
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getBirthday(),
                    employee.getAddress(),
                    employee.getPhoneNumber(),
                    employee.getSssNumber(),
                    employee.getPhilhealthNumber(),
                    employee.getTinNumber(),
                    employee.getPagibigNumber(),
                    employee.getStatus(),
                    employee.getPosition(),
                    employee.getImmediateSupervisor(),
                    employee.getBasicSalary(),
                    employee.getRiceSubsidy(),
                    employee.getPhoneAllowance(),
                    employee.getClothingAllowance(),
                    employee.getGrossSemiMonthlyRate(),
                    employee.getHourlyRate()
                ));
            }
        }
    }

    /**
     * Loads employee data from the TSV file into the application.
     * This method reads the employee details file and populates the employees list
     * with EmployeeProfile objects for display and management.
     */
    private void loadEmployeeData() {
        try {
            // Define the path to the employee data file as String
            String employeeFilePath = "src/main/resources/Employee Details.tsv";
            
            // Load employees using the LoadEmployeeData utility class with String path
            employees = LoadEmployeeData.loadFromFile(employeeFilePath);
            
            // Refresh the table display with the loaded data
            refreshEmployeeData();
            
        } catch (Exception e) {
            // Handle file loading errors
            JOptionPane.showMessageDialog(this, 
                "Error loading employee data:\n" + e.getMessage() + 
                "\n\nThe application will start with an empty employee list.", 
                "Data Loading Error", 
                JOptionPane.WARNING_MESSAGE);
            
            // Initialize with empty list if loading fails
            employees = new ArrayList<>();
            refreshEmployeeData();
        }
    }

    /**
     * Refreshes the employee data display by reloading the table model.
     * This method updates the table with the current employee list and
     * clears any selected employee and editing fields.
     */
    private void refreshEmployeeData() {
        // Check if table model is initialized
        if (tableModel == null) {
            // If table model is not initialized, just return
            // This can happen during initial setup
            return;
        }
        
        // Clear the table model
        tableModel.setRowCount(0);
        
        // Add all employees to the table
        for (EmployeeProfile employee : employees) {
            Object[] rowData = {
                employee.getEmployeeNumber(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getSssNumber(),
                employee.getPhilhealthNumber(),
                employee.getTinNumber(),
                employee.getPagibigNumber()
            };
            tableModel.addRow(rowData);
        }
        
        // Clear selection and editing fields
        selectedEmployee = null;
        clearEditingFields();
        updateButtonStates();
        
        // Update the table display
        if (employeeTable != null) {
            employeeTable.revalidate();
            employeeTable.repaint();
        }
    }

    /**
     * Refreshes the employee data and selects the specified employee in the table.
     * This method is called after adding a new employee to update the display
     * and automatically select the newly added employee for user convenience.
     * 
     * @param employeeNumber The employee number of the employee to select after refresh
     */
    public void refreshEmployeeDataAndSelect(String employeeNumber) {
        try {
            // Reload employee data from file using String path
            String employeeFilePath = "src/main/resources/Employee Details.tsv";
            employees = LoadEmployeeData.loadFromFile(employeeFilePath);
            
            // Refresh the table display
            refreshEmployeeData();
            
            // Find and select the specified employee
            selectEmployeeByNumber(employeeNumber);
            
        } catch (Exception e) {
            // Handle any errors during refresh
            JOptionPane.showMessageDialog(this, 
                "Error refreshing employee data:\n" + e.getMessage(), 
                "Refresh Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Selects an employee in the table by their employee number.
     * This method searches for the employee and selects them in the table,
     * which automatically populates the editing fields through the selection listener.
     * 
     * @param employeeNumber The employee number to search for and select
     */
    private void selectEmployeeByNumber(String employeeNumber) {
        // Search through the table model to find the employee
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String tableEmployeeNumber = (String) tableModel.getValueAt(row, 0);
            if (employeeNumber.equals(tableEmployeeNumber)) {
                // Convert model row to view row (in case of sorting)
                int viewRow = employeeTable.convertRowIndexToView(row);
                
                // Select the row in the table
                employeeTable.setRowSelectionInterval(viewRow, viewRow);
                
                // Scroll to make the selected row visible
                employeeTable.scrollRectToVisible(employeeTable.getCellRect(viewRow, 0, true));
                
                // The selection listener will automatically set selectedEmployee and populate fields
                break;
            }
        }
    }

    /**
     * Searches for an employee by employee number and opens their details dialog.
     * This method provides quick access to employee information without needing
     * to find and select the employee in the table first.
     */
    private void searchAndViewEmployee() {
        String searchNumber = searchField.getText().trim();
        
        // Validate search input
        if (searchNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter an employee number to search.", 
                "Search Required", 
                JOptionPane.INFORMATION_MESSAGE);
            searchField.requestFocus();
            return;
        }
        
        // Search for the employee
        EmployeeProfile foundEmployee = null;
        for (EmployeeProfile employee : employees) {
            if (employee.getEmployeeNumber().equals(searchNumber)) {
                foundEmployee = employee;
                break;
            }
        }
        
        // Handle search results
        if (foundEmployee != null) {
            // Employee found - open their details
            EmployeeDetailGUI detailDialog = new EmployeeDetailGUI(this, foundEmployee);
            detailDialog.setVisible(true);
            
            // Optional: Also select the employee in the table for visual feedback
            selectEmployeeInTable(foundEmployee);
            
            // Clear the search field after successful search
            searchField.setText("");
            
        } else {
            // Employee not found
            JOptionPane.showMessageDialog(this, 
                "Employee with number '" + searchNumber + "' was not found.\n" +
                "Please check the employee number and try again.", 
                "Employee Not Found", 
                JOptionPane.WARNING_MESSAGE);
            searchField.selectAll(); // Select all text for easy correction
            searchField.requestFocus();
        }
    }
    
    /**
     * Selects the specified employee in the table and populates the editing fields.
     * This method provides visual feedback when an employee is found via search.
     * 
     * @param employee The employee to select in the table
     */
    private void selectEmployeeInTable(EmployeeProfile employee) {
        // Find the employee in the table model
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String tableEmployeeNumber = (String) tableModel.getValueAt(row, 0);
            if (employee.getEmployeeNumber().equals(tableEmployeeNumber)) {
                // Convert model row to view row (in case of sorting)
                int viewRow = employeeTable.convertRowIndexToView(row);
                
                // Select the row in the table
                employeeTable.setRowSelectionInterval(viewRow, viewRow);
                
                // Scroll to make the selected row visible
                employeeTable.scrollRectToVisible(employeeTable.getCellRect(viewRow, 0, true));
                
                // The selection listener will automatically populate the editing fields
                break;
            }
        }
    }

    /**
     * The main entry point for the MotorPH Employee Management System application.
     * Creates an instance of the main PayrollGUI window and displays it on the screen.
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

    /**
     * Updates the state of action buttons based on whether an employee is selected.
     * Enables or disables buttons appropriately to prevent invalid operations.
     */
    private void updateButtonStates() {
        boolean hasSelection = (selectedEmployee != null);
        
        viewEmployeeButton.setEnabled(hasSelection);
        updateEmployeeButton.setEnabled(hasSelection);
        deleteEmployeeButton.setEnabled(hasSelection);
    }
}