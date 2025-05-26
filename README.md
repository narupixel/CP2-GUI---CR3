# Payroll System Project

This project is a payroll system developed in Java that includes a graphical user interface (GUI) for managing employee information and calculating monthly salaries. The system allows users to search for employees by their employee number, select a month and year, and view detailed payroll information including hours worked, allowances, government-mandated contributions, and net pay.

## Project Structure

The project is organized as follows:

```
payroll-system
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Main.java                            # Alternative entry point
│   │   │   ├── dataLoader
│   │   │   │   ├── LoadEmployeeData.java            # Loads employee profiles
│   │   │   │   └── LoadTimeSheet.java               # Loads time logs for employees
│   │   │   ├── governmentContributions
│   │   │   │   ├── CalculatePagibig.java            # Pag-IBIG contribution calculations
│   │   │   │   ├── CalculatePhilhealth.java         # PhilHealth contribution calculations
│   │   │   │   ├── CalculateSss.java                # SSS contribution calculations
│   │   │   │   └── CalculateWithholdingTax.java     # Tax withholding calculations
│   │   │   ├── gui
│   │   │   │   └── PayrollGUI.java                  # Main GUI for the payroll system
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

- **Employee Search**: Search for employees by their employee number.
- **Date Selection**: Select month and year for payroll calculation.
- **Detailed Payroll Information**:
  - Basic employee details (name, ID, birthday)
  - Total hours worked in the selected month
  - Overtime hours calculation
  - Allowances (rice subsidy, phone allowance, clothing allowance)
  - Government-mandated contributions (Pag-IBIG, PhilHealth, SSS, withholding tax)
  - Gross and net monthly pay calculation

## Setup Instructions

1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
   cd payroll-system
   ```

2. **Build the Project**: 
   Use your preferred Java build tool (e.g., Maven, Gradle) to build the project.

3. **Run the Application**: 
   Execute the PayrollGUI class to start the payroll system with the graphical interface:
   ```bash
   java -cp target/classes gui.PayrollGUI
   ```

## Usage Guidelines

1. **Launch the application** to access the GUI.
2. **Enter an employee number** in the search field (e.g., 10001, 10002).
3. **Select a month and year** from the dropdown menus.
4. **Click the Calculate button** or press Enter while in the Employee search field to compute and display payroll information.
5. **View the results** in the scrollable panel that appears below the search area.

## Resource Files

The system relies on several TSV (Tab-Separated Values) files in the resources directory:
- `Employee Details.tsv`: Contains basic employee information and pay rates
- `Employee Attendance Record.tsv`: Contains the attendance records and hours worked
- `Pag-ibig Contribution.tsv`, `Philhealth Contribution.tsv`, `SSS Contribution Schedule.tsv`, and `Witholding Tax.tsv`: Contain the contribution rates for various government-mandated deductions

Ensure these files are present in the resources directory before running the application.