package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.StreamingServicePlan;
import com.example.service.CsvService;

@RestController
@RequestMapping("/api")
public class CsvController {

    @Autowired
    private CsvService csvService;

    // API endpoint to get plans filtered by platform
    @GetMapping("/plans")
    public List<StreamingServicePlan> getPlans(@RequestParam String platform) {
        return csvService.getPlans(platform); // Call service to get plans for the specified platform
    }

    @GetMapping("/search")
    public List<StreamingServicePlan> searchPlans(@RequestParam String query) {
        return csvService.searchPlans(query);
    }


    
 


   


}
