package com.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.model.StreamingServicePlan;

@Service
public class CsvService {
    
    // Read the CSV file and return the list of all plans
    public List<StreamingServicePlan> readCsv() {
        List<StreamingServicePlan> plans = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("StreamingServices.csv")))) {

            if (br == null) {
                System.out.println("File not found in resources folder!");
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
                if (fields.length < 4) {
                    System.out.println("Invalid row: " + line);
                    continue; // Skip invalid rows
                }

                StreamingServicePlan plan = new StreamingServicePlan();
                plan.setServiceName(fields[0].trim());
                plan.setPlanName(fields[1].trim());
                plan.setPrice(Double.parseDouble(fields[2].replace("$", "").trim())); // Handle price format
                plan.setFeatures(fields[3].trim());
                plans.add(plan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plans;
    }

    // This method will return plans filtered by platform
    public List<StreamingServicePlan> getPlans(String platform) {
        List<StreamingServicePlan> plans = readCsv();
        return plans.stream()
                .filter(plan -> plan.getServiceName().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }
}
