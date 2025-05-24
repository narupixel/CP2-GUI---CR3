package models;

public class EmployeeProfile {
    // Properties based on Employee Details.tsv
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private String status;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate;
    private double hourlyRate;

    // Constructor
    public EmployeeProfile(String employeeNumber, String lastName, String firstName, String birthday, String address,
                          String phoneNumber, String sssNumber, String philhealthNumber, String tinNumber, String pagibigNumber,
                          String status, String position, String immediateSupervisor, double basicSalary, double riceSubsidy,
                          double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.status = status;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }

    // Getters and setters for all properties (generate as needed)
    // Example:
    public String getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }

    // You can add setters if you need to modify these properties

    // Method to display employee details
    public String displayEmployeeInfo() {
        return "Employee Number: " + employeeNumber + "\n" +
               "Name: " + firstName + " " + lastName + "\n" +
               "Birthday: " + birthday + "\n" +
               "Address: " + address + "\n" +
               "Phone Number: " + phoneNumber + "\n" +
               "SSS #: " + sssNumber + "\n" +
               "Philhealth #: " + philhealthNumber + "\n" +
               "TIN #: " + tinNumber + "\n" +
               "Pag-ibig #: " + pagibigNumber + "\n" +
               "Status: " + status + "\n" +
               "Position: " + position + "\n" +
               "Immediate Supervisor: " + immediateSupervisor + "\n" +
               "Basic Salary: " + basicSalary + "\n" +
               "Rice Subsidy: " + riceSubsidy + "\n" +
               "Phone Allowance: " + phoneAllowance + "\n" +
               "Clothing Allowance: " + clothingAllowance + "\n" +
               "Gross Semi-monthly Rate: " + grossSemiMonthlyRate + "\n" +
               "Hourly Rate: " + hourlyRate;
    }
}