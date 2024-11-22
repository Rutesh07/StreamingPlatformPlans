package com.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.StreamingServicePlan;

@Service
public class CsvService {

    // Read the CSV and return a list of StreamingServicePlan objects
    public List<StreamingServicePlan> readCsv() {
        List<StreamingServicePlan> plans = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("StreamingServices.csv")))) {
    
            if (br == null) {
                System.out.println("File not found!");
                return plans;
            }
    
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
    
                String[] fields = line.split(",");
                StreamingServicePlan plan = new StreamingServicePlan();
    
                plan.setServiceName(fields[0].trim());
                plan.setPlanName(fields[1].trim());
                plan.setPrice(removeDollarSymbol(fields[2].trim()));
                plan.setAnnualPrice(fields[3].trim());
                plan.setFeatures(fields[4].trim());
    
                // Handle "N/A" or invalid numbers gracefully
                try {
                    plan.setSimultaneousStream(fields[5].trim().equalsIgnoreCase("N/A") ? 0 : Integer.parseInt(fields[5].trim()));
                } catch (NumberFormatException e) {
                    plan.setSimultaneousStream(0);  // Default to 0 if parsing fails
                }
    
                plan.setDownload(fields[6].trim());
                plan.setAdFreeStreaming(fields[7].trim());
    
                plans.add(plan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        System.out.println("Plans: " + plans);  // Debug print
        return plans;
    }

    // Helper method to remove dollar signs from price fields
    private String removeDollarSymbol(String value) {
        return value.replace("$", "").trim();
    }
    
    // Implement the getPlans method to filter by platform
    public List<StreamingServicePlan> getPlans(String platform) {
        List<StreamingServicePlan> plans = readCsv();  // Get all plans from CSV

        // Filter plans based on the platform (service name)
        List<StreamingServicePlan> filteredPlans = new ArrayList<>();
        for (StreamingServicePlan plan : plans) {
            if (plan.getServiceName().equalsIgnoreCase(platform)) {
                filteredPlans.add(plan);
            }
        }
        return filteredPlans;  // Return filtered plans based on the platform
    }
}
