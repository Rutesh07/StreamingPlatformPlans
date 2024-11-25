package com.example.controller;

import com.example.service.FrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map.Entry;

@RestController
@RequestMapping("/api/frequency")
public class FrequencyController {

    @Autowired
    private FrequencyService frequencyService;

    @GetMapping("/count")
    public List<Entry<String, Integer>> getFrequencyCount(
            @RequestParam String filePath,
            @RequestParam(defaultValue = "10") int topN) {
        // Call the service to calculate frequency
        return frequencyService.calculateFrequency(filePath, topN);
    }
}
