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

    @GetMapping("/plans")
    public List<StreamingServicePlan> getPlans(@RequestParam String platform) {
        return csvService.getPlans(platform);  // Return filtered plans by platform
    }
}
