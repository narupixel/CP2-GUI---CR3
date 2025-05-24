# Payroll System Project

This project is a payroll system developed in Java that includes a graphical user interface (GUI) for managing employee information and calculating salaries. The system allows users to input employee details, track hours worked, and compute gross and net weekly salaries after deductions.

## Project Structure

The project is organized as follows:

```
payroll-system
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Main.java               # Entry point of the application
│   │   │   ├── gui
│   │   │   │   └── PayrollGUI.java     # GUI components for the payroll system
│   │   │   ├── model
│   │   │   │   └── Employee.java        # Employee class definition
│   │   │   ├── service
│   │   │   │   └── PayrollCalculator.java # Payroll calculation logic
│   │   │   └── util
│   │   │       └── SalaryUtils.java     # Utility methods for salary calculations
│   │   └── resources
│   │       └── config.properties         # Configuration settings for the payroll system
├── .gitignore                             # Files and directories to ignore in version control
└── README.md                              # Documentation for the project
```

## Features

- **Employee Management**: Input and display employee information including employee number, name, and birthday.
- **Salary Calculations**: Calculate gross weekly salary, net weekly salary after deductions, and allowances.
- **Time Tracking**: Input hours worked and compute total hours for payroll processing.
- **Configuration Management**: Use a properties file to manage configuration settings such as deduction rates and allowance amounts.

## Setup Instructions

1. **Clone the Repository**: 
   ```bash
   git clone <repository-url>
   cd payroll-system
   ```

2. **Build the Project**: 
   Use your preferred Java build tool (e.g., Maven, Gradle) to build the project.

3. **Run the Application**: 
   Execute the `Main.java` file to start the payroll system.

## Usage Guidelines

- Launch the application to access the GUI.
- Enter employee details and hours worked in the provided fields.
- Click on the calculate button to compute salaries and view results.

## Additional Notes

- Ensure that the `config.properties` file is properly configured with the necessary deduction rates and allowances before running the application.
- This project is designed to be extensible; feel free to add more features or modify existing ones as needed.