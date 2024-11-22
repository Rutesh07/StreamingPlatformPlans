package com.example.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.StreamingServicePlan;

@Service
public class BestPlanService {

    private final CsvService csvService;

    public BestPlanService(CsvService csvService) {
        this.csvService = csvService;
    }

    // Get the two best plans with the lowest price
    public List<StreamingServicePlan> getBestPlans() {
        List<StreamingServicePlan> plans = csvService.readCsv();  // Get all plans from CSV

        // Sort plans by price in ascending order
        plans.sort(Comparator.comparingDouble(plan -> {
            try {
                // Remove any non-numeric characters from the price (like "$")
                return Double.parseDouble(plan.getPrice().replaceAll("[^\\d.]", ""));
            } catch (NumberFormatException e) {
                return Double.MAX_VALUE; // If price is invalid, push it to the end
            }
        }));

        // Return the top 2 lowest priced plans
        return plans.size() > 2 ? plans.subList(0, 2) : plans;
    }
}
