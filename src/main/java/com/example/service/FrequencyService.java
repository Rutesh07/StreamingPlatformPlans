package com.example.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class FrequencyService {

    public List<Entry<String, Integer>> calculateFrequency(String filePath, int topN) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Read the CSV file and process the text
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] rowData;

            while ((rowData = reader.readNext()) != null) {
                if (rowData.length < 2)
                    continue; // Skip invalid lines
                String textContent = rowData[0]; // Assuming text content is in the first column
                countWords(textContent, frequencyMap);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Handle file read errors
        }

        // Sort the words by frequency
        List<Entry<String, Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Sort by descending frequency

        // Get the top N words
        return sortedList.subList(0, Math.min(topN, sortedList.size()));
    }

    private void countWords(String text, Map<String, Integer> frequencyMap) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            word = word.trim().toLowerCase(); // Normalize the word
            if (!word.isEmpty()) {
                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            }
        }
    }
}
