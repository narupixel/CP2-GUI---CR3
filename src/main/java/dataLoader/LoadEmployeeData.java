package dataLoader;

import models.EmployeeProfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for loading employee profile data from a specified file path
 * and populating a list of EmployeeProfile objects.
 */
public class LoadEmployeeData {

    public static List<EmployeeProfile> loadFromFile(String filePath) {
        List<EmployeeProfile> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length < 19) continue; // Skip malformed lines

                EmployeeProfile employee = new EmployeeProfile(
                        fields[0].trim(), // employeeNumber
                        fields[1].trim(), // lastName
                        fields[2].trim(), // firstName
                        fields[3].trim(), // birthday
                        fields[4].trim(), // address
                        fields[5].trim(), // phoneNumber
                        fields[6].trim(), // sssNumber
                        fields[7].trim(), // philhealthNumber
                        fields[8].trim(), // tinNumber
                        fields[9].trim(), // pagibigNumber
                        fields[10].trim(), // status
                        fields[11].trim(), // position
                        fields[12].trim(), // immediateSupervisor
                        Double.parseDouble(fields[13].replace(",", "").trim()), // basicSalary
                        Double.parseDouble(fields[14].replace(",", "").trim()), // riceSubsidy
                        Double.parseDouble(fields[15].replace(",", "").trim()), // phoneAllowance
                        Double.parseDouble(fields[16].replace(",", "").trim()), // clothingAllowance
                        Double.parseDouble(fields[17].replace(",", "").trim()), // grossSemiMonthlyRate
                        Double.parseDouble(fields[18].replace(",", "").trim())  // hourlyRate
                );
                employees.add(employee);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading employee data: " + e.getMessage());
        }
        return employees;
    }
}