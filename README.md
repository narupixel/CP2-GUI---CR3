# MotorPH Employee Management System

This project is a comprehensive employee management and payroll system developed in Java with a modern graphical user interface (GUI). The system provides complete CRUD (Create, Read, Update, Delete) functionality for employee records, detailed payroll calculations with government contributions, and an intuitive interface for managing all aspects of employee data and salary computation.

## Project Structure

The project is organized as follows:

```
motorph-employee-management-system
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Main.java                            # Alternative entry point
│   │   │   ├── dataLoader
│   │   │   │   ├── LoadEmployeeData.java            # Loads employee profiles from TSV files
│   │   │   │   └── LoadTimeSheet.java               # Loads time logs for employees
│   │   │   ├── governmentContributions
│   │   │   │   ├── CalculatePagibig.java            # Pag-IBIG contribution calculations
│   │   │   │   ├── CalculatePhilhealth.java         # PhilHealth contribution calculations
│   │   │   │   ├── CalculateSss.java                # SSS contribution calculations
│   │   │   │   └── CalculateWithholdingTax.java     # Tax withholding calculations
│   │   │   ├── gui
│   │   │   │   ├── PayrollGUI.java                  # Main GUI with employee table and management
│   │   │   │   ├── EmployeeDetailGUI.java           # Employee details and salary computation dialog
│   │   │   │   └── NewEmployeeGUI.java              # New employee creation dialog
│   │   │   ├── models
│   │   │   │   ├── EmployeeProfile.java             # Employee data model
│   │   │   │   ├── TimeLog.java                     # Attendance record model
│   │   │   │   └── WeeklyTotals.java                # Weekly aggregation model
│   │   │   ├── payrollCalculations
│   │   │   │   ├── CalculateAndDisplay.java         # Payroll calculation and display logic
│   │   │   │   ├── CalculateOvertimePay.java        # Overtime calculation utilities
│   │   │   │   ├── CalculateWeeklyAllowances.java   # Allowance calculation utilities
│   │   │   │   └── CalculateWeeklyTotals.java       # Weekly totals calculation
│   │   │   └── ui
│   │   │       └── ConsoleUI.java                   # Alternative console interface
│   │   └── resources
│   │       ├── Employee Attendance Record.tsv       # Employee time log data
│   │       ├── Employee Details.tsv                 # Employee personal and salary information
│   │       ├── Pag-ibig Contribution.tsv            # Pag-IBIG contribution rates
│   │       ├── Philhealth Contribution.tsv          # PhilHealth contribution rates
│   │       ├── SSS Contribution Schedule.tsv        # SSS contribution rates
│   │       └── Witholding Tax.tsv                   # Tax withholding rates
└── README.md                                        # Documentation for the project
```

## Features

### 🏢 **Employee Management**
- **Employee Table View**: Display all employees in a sortable table showing Employee Number, Last Name, First Name, SSS Number, PhilHealth Number, TIN, and Pag-IBIG Number
- **Add New Employees**: Create new employee records with comprehensive data validation and automatic employee number generation
- **Update Employee Information**: Modify existing employee records with real-time field population and validation
- **Delete Employee Records**: Remove employees from the system with detailed confirmation dialogs
- **Real-time Data Synchronization**: Automatic table refresh and TSV file updates after all operations

### 💰 **Payroll Calculation**
- **Employee Details View**: Comprehensive employee information display with professional formatting
- **Month/Year Selection**: Flexible date selection for payroll calculation periods
- **Detailed Salary Breakdown**:
  - Basic employee details (name, ID, position, contact information)
  - Total hours worked and overtime calculation for the selected month
  - Allowances (rice subsidy, phone allowance, clothing allowance)
  - Government-mandated contributions (Pag-IBIG, PhilHealth, SSS, withholding tax)
  - Gross and net monthly pay calculation with detailed breakdown

### 🎨 **User Interface**
- **Modern GUI Design**: Intuitive layout with proper spacing, tooltips, and professional styling
- **Table Interaction**: Single-click selection, double-click for quick details view, and sortable columns
- **Modal Dialogs**: Dedicated windows for employee details, new employee creation, and salary computation
- **Data Validation**: Comprehensive input validation with user-friendly error messages
- **Responsive Layout**: Scalable interface that adapts to different screen sizes

## Setup Instructions

### 1. **Prerequisites**
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.) or command line

### 2. **Clone the Repository**
```bash
git clone <repository-url>
cd motorph-employee-management-system
```

### 3. **Compile the Project**
```bash
# Using command line
javac -d target/classes src/main/java/**/*.java

# Or use your IDE's build functionality
```

### 4. **Run the Application**
```bash
# Using command line
java -cp "src/main/java" gui.PayrollGUI

# Or run directly from your IDE
```

## Usage Guidelines

### 🚀 **Getting Started**
1. **Launch the application** to access the main Employee Management window
2. **Browse employee records** in the sortable table showing all essential employee information
3. **Select an employee** by clicking on any row to populate the editing fields below

### 👥 **Employee Management Operations**

#### **Viewing Employee Details**
1. Select an employee from the table
2. Click "View Employee Details" to open detailed information
3. Select month and year for salary calculation
4. Click "Compute Salary" to see detailed payroll breakdown

#### **Adding New Employees**
1. Click "Add New Employee" to open the creation form
2. Fill in all required employee information
3. Submit to automatically generate employee number and save to system
4. Table will refresh automatically to show the new employee

#### **Updating Employee Information**
1. Select an employee from the table
2. Modify the information in the editing fields below the table
3. Click "Update Employee" to save changes
4. Confirm the update in the dialog that appears

#### **Deleting Employee Records**
1. Select an employee from the table
2. Click "Delete Employee"
3. Confirm deletion in the detailed confirmation dialog
4. Record will be permanently removed from the system

### 📊 **Payroll Features**
- **Month Selection**: Choose any month and year for salary calculation
- **Detailed Breakdown**: View comprehensive salary information including:
  - Hours worked and overtime calculations
  - All allowances and benefits
  - Government contribution deductions
  - Final net pay calculation
- **Professional Reports**: Clean, formatted display of all payroll information

## Technical Details

### 📁 **Data Storage**
The system uses TSV (Tab-Separated Values) files for data persistence:
- **Employee Details.tsv**: Master employee information including personal data and pay rates
- **Employee Attendance Record.tsv**: Time tracking and attendance records
- **Government Contribution Files**: Pag-IBIG, PhilHealth, SSS, and tax withholding rate tables

### 🏗️ **Architecture**
- **Model-View-Controller (MVC) Pattern**: Separation of data models, user interface, and business logic
- **Modular Design**: Organized into packages for data loading, calculations, GUI components, and models
- **Event-Driven GUI**: Responsive interface with proper event handling and user feedback
- **Data Validation**: Comprehensive input validation and error handling throughout the system

### 🔒 **Data Integrity**
- **Validation Rules**: Required field checking, numeric validation, and business rule enforcement
- **Immutable Models**: Employee data models designed for data integrity
- **File Synchronization**: Automatic saving and loading to maintain data consistency
- **Error Handling**: Graceful error recovery with informative user messages

## System Requirements

- **Java Runtime Environment**: JDK 8 or higher
- **Memory**: Minimum 512MB RAM (1GB recommended)
- **Storage**: 100MB available disk space
- **Display**: 1024x768 minimum screen resolution (1200x800 recommended)
- **Operating System**: Windows, macOS, or Linux with Java support

## Change Log

### Version 3.0 - Complete Employee Management System
- ✅ Full CRUD operations for employee records
- ✅ Professional table-based interface with sorting capabilities
- ✅ Comprehensive employee editing with validation
- ✅ Modal dialogs for employee details and creation
- ✅ Real-time data synchronization with TSV files
- ✅ Enhanced payroll calculation with detailed breakdowns
- ✅ Modern UI/UX design with tooltips and proper spacing

### Version 2.0 - GUI Enhancement
- ✅ Graphical user interface implementation
- ✅ Employee search and payroll calculation
- ✅ Month/year selection for payroll periods
- ✅ Detailed salary breakdown display

### Version 1.0 - Console Application
- ✅ Basic payroll calculation functionality
- ✅ Console-based user interface
- ✅ Government contribution calculations

---

**MotorPH Employee Management System** - A complete solution for employee data management and payroll processing. 🚀