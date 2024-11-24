package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.PageContent;
import com.example.service.CsvService;

@RestController
public class PageRankController {

    private final CsvService csvService;

    public PageRankController(CsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping("/api/rank")
    public List<PageContent> rankPages(@RequestParam String query) {
        return csvService.rankPages(query);
    }
}
