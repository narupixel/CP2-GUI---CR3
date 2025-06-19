package gui;

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

import dataLoader.LoadEmployeeData;
import models.EmployeeProfile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;

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
    private String filePath = "src/main/resources/Employee Details.tsv"; // Path to the employee data file

    /**
     * Constructor that initializes the main Employee Management GUI and sets up all components.
     * This method loads employee data from the file system, creates the employee table with
     * sorting capabilities, configures action buttons, and establishes the overall layout
     * for optimal user experience and intuitive navigation.
     */
    public PayrollGUI() {
        // Load all employees from the data file at startup to populate the main table
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");

        // Configure the main window properties for optimal display and user interaction
        setTitle("MotorPH Employee Management System");
        setSize(1200, 800);  // Increased size to accommodate edit fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window on screen

        // Initialize and configure the main user interface components
        initializeComponents();
        setupTableData();
        setupEventHandlers();
        
        // Apply the layout to organize all components properly
        layoutComponents();
    }

    /**
     * Initializes all GUI components including the employee table, action buttons,
     * and table model. Sets up the basic properties and configurations for each
     * component to ensure proper functionality and visual appearance.
     */
    private void initializeComponents() {
        // Define column headers for the employee table as specified in requirements
        // Only showing essential employee identification information for quick reference
        String[] columnHeaders = {
            "Employee Number", 
            "Last Name", 
            "First Name", 
            "SSS Number", 
            "PhilHealth Number", 
            "TIN", 
            "Pag-IBIG Number"
        };

        // Create the table model with non-editable cells to prevent accidental data modification
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable for data integrity
            }
        };

        // Initialize the main employee table with the configured model
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single row selection
        employeeTable.setRowHeight(25); // Set comfortable row height for readability
        employeeTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        
        // Configure table appearance for better user experience
        employeeTable.setGridColor(new Color(230, 230, 230));
        employeeTable.setSelectionBackground(new Color(184, 207, 229));
        employeeTable.setSelectionForeground(Color.BLACK);
        
        // Add sorting capability to the table for improved data navigation
        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        // Create action buttons for employee management operations
        viewEmployeeButton = new JButton("View Employee Details");
        viewEmployeeButton.setPreferredSize(new Dimension(180, 35));
        viewEmployeeButton.setToolTipText("View detailed information and calculate payroll for selected employee");
        viewEmployeeButton.setEnabled(false); // Initially disabled until an employee is selected

        newEmployeeButton = new JButton("Add New Employee");
        newEmployeeButton.setPreferredSize(new Dimension(160, 35));
        newEmployeeButton.setToolTipText("Create a new employee record and add to the system");

        updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.setPreferredSize(new Dimension(150, 35));
        updateEmployeeButton.setToolTipText("Save changes to the selected employee record");
        updateEmployeeButton.setEnabled(false); // Initially disabled until an employee is selected

        deleteEmployeeButton = new JButton("Delete Employee");
        deleteEmployeeButton.setPreferredSize(new Dimension(150, 35));
        deleteEmployeeButton.setToolTipText("Remove the selected employee record from the system");
        deleteEmployeeButton.setEnabled(false); // Initially disabled until an employee is selected

        // Initialize employee editing text fields with appropriate configurations
        initializeEditingFields();
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
     * Sets up event handlers for user interactions including table selection,
     * button clicks, and mouse events. Configures the behavior for viewing,
     * updating, and deleting employee records.
     */
    private void setupEventHandlers() {
        // Configure table selection listener to enable/disable buttons and populate fields
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                boolean hasSelection = selectedRow != -1;
                
                // Enable/disable buttons based on selection
                viewEmployeeButton.setEnabled(hasSelection);
                updateEmployeeButton.setEnabled(hasSelection);
                deleteEmployeeButton.setEnabled(hasSelection);
                
                // Update the selected employee and populate fields
                if (hasSelection) {
                    // Convert view row index to model row index to handle sorting
                    int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
                    String employeeNumber = (String) tableModel.getValueAt(modelRow, 0);
                    
                    // Find the corresponding employee object from the list
                    selectedEmployee = employees.stream()
                            .filter(emp -> emp.getEmployeeNumber().equals(employeeNumber))
                            .findFirst()
                            .orElse(null);
                    
                    // Populate the editing fields with selected employee data
                    populateEditingFields(selectedEmployee);
                } else {
                    selectedEmployee = null;
                    clearEditingFields();
                }
                
                // Reset when selection changes
            }
        });

        // Add double-click functionality to the table for quick employee detail viewing
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle double-click events to open employee details directly
                if (e.getClickCount() == 2 && employeeTable.getSelectedRow() != -1) {
                    viewEmployeeDetails();
                }
            }
        });

        // Configure button actions
        viewEmployeeButton.addActionListener(e -> viewEmployeeDetails());
        newEmployeeButton.addActionListener(e -> addNewEmployee());
        updateEmployeeButton.addActionListener(e -> updateEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteEmployee());
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add action buttons to the button panel
        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(newEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);

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
     * Opens the employee details window for the currently selected employee.
     * Creates a new EmployeeDetailGUI instance and displays it while keeping
     * the main window open in the background for continued navigation.
     */
    private void viewEmployeeDetails() {
        if (selectedEmployee != null) {
            SwingUtilities.invokeLater(() -> {
                // Create and display the employee details window with payroll calculation
                EmployeeDetailGUI detailsGUI = new EmployeeDetailGUI(selectedEmployee, this);
                detailsGUI.setVisible(true);
            });
        } else {
            // Display error message if no employee is selected
            JOptionPane.showMessageDialog(this, 
                "Please select an employee from the table first.", 
                "No Employee Selected", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Opens the new employee creation dialog to add a new employee record.
     * Creates a NewEmployeeGUI instance and displays it as a modal dialog.
     */
    private void addNewEmployee() {
        SwingUtilities.invokeLater(() -> {
            // Create and display the new employee form
            NewEmployeeGUI newEmployeeGUI = new NewEmployeeGUI(this);
            newEmployeeGUI.setVisible(true);
        });
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
     * Saves all employee data to the TSV file.
     * Writes the current employee list to the file system using TSV format.
     * 
     * @throws IOException if an error occurs while writing to the file
     */
    private void saveEmployeesToFile() throws IOException {
        String filePath = "src/main/resources/Employee Details.tsv";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header row
            writer.println("Employee Number\tLast Name\tFirst Name\tBirthday\tAddress\tPhone Number\t" +
                          "SSS Number\tPhilHealth Number\tTIN\tPag-IBIG Number\tStatus\tPosition\t" +
                          "Immediate Supervisor\tBasic Salary\tRice Subsidy\tPhone Allowance\t" +
                          "Clothing Allowance\tGross Semi-monthly Rate\tHourly Rate");
            
            // Write employee data
            for (EmployeeProfile employee : employees) {
                writer.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f%n",
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
                );
            }
        }
    }

    /**
     * Refreshes the employee table data by reloading from the file and updating
     * the table model. This method is called after new employees are added,
     * updated, or deleted to ensure the display is synchronized with the current data state.
     */
    public void refreshEmployeeData() {
        // Reload employee data from the file to get the most current information
        employees = LoadEmployeeData.loadFromFile("src/main/resources/Employee Details.tsv");
        
        // Update the table display with the refreshed data
        setupTableData();
        
        // Update the status label with the new employee count
        Component[] components = ((JPanel)getContentPane().getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel)components[0]).setText("Total Employees: " + employees.size());
        }
        
        // Clear any current selection and reset buttons since the table data has changed
        employeeTable.clearSelection();
        selectedEmployee = null;
        viewEmployeeButton.setEnabled(false);
        updateEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);
        
        // Clear the editing fields
        clearEditingFields();
    }

    /**
     * Refreshes the employee data and automatically selects the specified employee.
     * This method is useful when a new employee is added and we want to show their
     * information immediately after the refresh.
     * 
     * @param employeeNumber The employee number to select after refresh
     */
    public void refreshEmployeeDataAndSelect(String employeeNumber) {
        // First refresh all the data
        refreshEmployeeData();
        
        // Then find and select the specified employee
        if (employeeNumber != null && !employeeNumber.trim().isEmpty()) {
            selectEmployeeByNumber(employeeNumber);
        }
    }

    /**
     * Selects an employee in the table by their employee number and populates the editing fields.
     * This method is used to automatically select a newly added employee.
     * 
     * @param employeeNumber The employee number to select
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
                
                // The selection listener will automatically populate the fields
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
}