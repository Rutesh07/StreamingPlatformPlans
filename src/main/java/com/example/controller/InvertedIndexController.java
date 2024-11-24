package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Occurrence;
import com.example.service.InvertedIndexService;

@RestController
@RequestMapping("/api/inverted-index")
public class InvertedIndexController {

    private final InvertedIndexService invertedIndexService;

    @Autowired
    public InvertedIndexController(InvertedIndexService invertedIndexService) {
        this.invertedIndexService = invertedIndexService;
    }

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String word) {
        Map<String, Object> response = new HashMap<>();
        
        if (word == null || word.trim().isEmpty()) {
            response.put("error", "Search term cannot be null or empty.");
            return response; // Return early if the word is invalid
        }

        word = word.toLowerCase(); // Normalize the search word

        // Search the word in the Inverted Index Service
        List<Occurrence> invertedIndexResult = invertedIndexService.searchInvertedIndex(word);
        if (invertedIndexResult == null || invertedIndexResult.isEmpty()) {
            response.put("invertedIndex", "No results found for the word: " + word);
        } else {
            response.put("invertedIndex", invertedIndexResult);
        }

        // Get frequency of the word from AVL Tree
        int frequency = invertedIndexService.getFrequency(word);
        response.put("frequency count", frequency);

        // Log and return the response
        System.out.println("Response: " + response);
        return response;
    }
}
