package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class SearchHistoryService {

    private List<String> searchHistory = new ArrayList<>(); // You can replace this with a database if needed

    // Sample method to add words to the search history
    public void addSearchHistory(String word) {
        if (!searchHistory.contains(word)) {
            searchHistory.add(word);
        }
    }

    // Method to get autocomplete suggestions
    public List<String> getAutocompleteSuggestions(String query) {
        return searchHistory.stream()
                .filter(word -> word.toLowerCase().startsWith(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
