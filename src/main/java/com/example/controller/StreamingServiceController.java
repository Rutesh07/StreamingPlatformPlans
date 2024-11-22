package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.StreamingServicePlan;
import com.example.service.BestPlanService;
import com.example.service.SearchHistoryService;

@RestController
public class StreamingServiceController {

    private final BestPlanService bestPlanService;

    @Autowired

    
    public StreamingServiceController(BestPlanService bestPlanService) {
        this.bestPlanService = bestPlanService;
    }
    private SearchHistoryService searchHistoryService;

    // Endpoint to fetch the best 2 plans based on the lowest price
    @GetMapping("/api/best-plans")
    public List<StreamingServicePlan> getBestPlans() {
        return bestPlanService.getBestPlans(); // Fetch the two lowest priced plans
    }
    @GetMapping("/api/autocomplete")
    public List<String> getAutocompleteSuggestions(@RequestParam String query) {
        // Get search history and return suggestions that match the query
        return searchHistoryService.getAutocompleteSuggestions(query);
    }
}
