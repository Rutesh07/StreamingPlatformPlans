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
        return frequencyService.calculateFrequency(filePath, topN);
    }

    @PostMapping("/track")
    public int trackSearchFrequency(@RequestParam String searchTerm) {
        return frequencyService.trackSearchFrequency(searchTerm);
    }

    @GetMapping("/search-frequency")
    public List<Entry<String, Integer>> getSearchFrequencies() {
        return frequencyService.getSearchFrequencies();
    }
}
