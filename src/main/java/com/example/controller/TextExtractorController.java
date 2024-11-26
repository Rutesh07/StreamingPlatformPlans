package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.TextExtractorService;

@RestController
@RequestMapping("/TextExtractorController")
public class TextExtractorController {

    private final TextExtractorService textExtractorService;

    @Autowired
    public TextExtractorController(TextExtractorService textExtractorService) {
        this.textExtractorService = textExtractorService;
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractText(
            @RequestParam("domain") String domain,
            @RequestParam("file") MultipartFile file) {
        try {
            String extractedText = textExtractorService.extractText(domain, file);
            return ResponseEntity.ok(extractedText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to extract text");
        }
    }
}
