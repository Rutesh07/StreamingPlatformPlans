package com.example.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.model.PageContent;
import com.example.model.StreamingServicePlan;

@Service
public class CsvService {

    private static final String CSV_FILE_PATH = "src/main/resources/StreamingServices.csv";
    private List<StreamingServicePlan> plans;

    public CsvService() {
        // Load the plans on startup
        this.plans = readCsv();
    }

    // Read the CSV and return a list of StreamingServicePlan objects
    List<StreamingServicePlan> readCsv() {
        List<StreamingServicePlan> plans = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("StreamingServices.csv")))) {

            if (br == null) {
                System.out.println("File not found!");
                return plans;
            }

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");
                StreamingServicePlan plan = new StreamingServicePlan();

                plan.setServiceName(fields[0].trim());
                plan.setPlanName(fields[1].trim());
                plan.setPrice(removeDollarSymbol(fields[2].trim()));
                plan.setAnnualPrice(fields[3].trim());
                plan.setFeatures(fields[4].trim());

                // Handle "N/A" or invalid numbers gracefully
                try {
                    plan.setSimultaneousStream(
                            fields[5].trim().equalsIgnoreCase("N/A") ? 0 : Integer.parseInt(fields[5].trim()));
                } catch (NumberFormatException e) {
                    plan.setSimultaneousStream(0); // Default to 0 if parsing fails
                }

                plan.setDownload(fields[6].trim());
                plan.setAdFreeStreaming(fields[7].trim());

                plans.add(plan);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading CSV file", e);
        }

        return plans;
    }

    // Helper method to remove dollar signs from price fields
    private String removeDollarSymbol(String value) {
        return value.replace("$", "").trim();
    }

    // Get plans filtered by the platform (service name)
    public List<StreamingServicePlan> getPlans(String platform) {
        return plans.stream()
                .filter(plan -> plan.getServiceName().equalsIgnoreCase(platform))
                .collect(Collectors.toList());
    }

    // Get autocomplete suggestions for plan names based on a query
    public List<String> getAutocompleteSuggestions(String query) {
        return plans.stream()
                .map(StreamingServicePlan::getPlanName)
                .filter(planName -> planName.toLowerCase().contains(query.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }

    // Search plans based on a query (case-insensitive search in serviceName, planName, and features)
    public List<StreamingServicePlan> searchPlans(String query) {
        String lowerCaseQuery = query != null ? query.toLowerCase() : "";
        return plans.stream()
            .filter(plan -> plan.getServiceName().toLowerCase().contains(lowerCaseQuery) ||
                    plan.getPlanName().toLowerCase().contains(lowerCaseQuery) ||
                    plan.getFeatures().toLowerCase().contains(lowerCaseQuery))
            .collect(Collectors.toList());
    }
    

    // Get top 10 suggestions based on the query for autocomplete
    public List<String> getSuggestions(String query) {
        return plans.stream()
                .map(StreamingServicePlan::getPlanName)
                .filter(planName -> planName.toLowerCase().contains(query.toLowerCase()))
                .distinct()
                .limit(10) // Limit to 10 suggestions
                .collect(Collectors.toList());
    }

    // Add word completion logic based on user input
    public List<String> completeWord(String query) {
        return plans.stream()
                .map(StreamingServicePlan::getPlanName)
                .filter(planName -> planName.toLowerCase().startsWith(query.toLowerCase())) // Start with the query
                .distinct()
                .collect(Collectors.toList());
    }

    // Rank pages based on the frequency of keywords
    public List<PageContent> rankPages(String query) {
        List<PageContent> webPages = loadPageContentFromCsv();
        Set<String> uniqueUrls = new HashSet<>();

        String[] keywords = query.toLowerCase().split("\\s+");

        // Calculate frequency for each page based on keywords
        for (PageContent page : webPages) {
            String normalizedContent = page.getContent().toLowerCase();
            for (String keyword : keywords) {
                page.setFrequency(page.getFrequency() + countOccurrences(normalizedContent, keyword));
            }
        }

        // Filter and sort pages by frequency in descending order
        return webPages.stream()
                .filter(page -> page.getFrequency() > 0) // Keep pages with frequency > 0
                .sorted(Comparator.comparingInt(PageContent::getFrequency).reversed())
                .collect(Collectors.toList());
    }

    // Load page content from CSV file
    private List<PageContent> loadPageContentFromCsv() {
        List<PageContent> webPages = new ArrayList<>();
        Set<String> uniqueUrls = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Crawled_Website_Data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 2);
                if (values.length == 2) {
                    String title = values[0].trim();
                    String content = values[1].trim();

                    if (uniqueUrls.add(title)) {
                        webPages.add(new PageContent(title, content));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading merged final CSV file", e);
        }

        return webPages;
    }

    // Boyer-Moore algorithm for counting occurrences of a keyword in a text
    private int countOccurrences(String text, String pattern) {
        Map<Character, Integer> badCharTable = buildBadCharTable(pattern);
        int occurrences = 0;
        int m = pattern.length();
        int n = text.length();

        int shift = 0;
        while (shift <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }

            if (j < 0) {
                occurrences++;
                shift += (shift + m < n) ? m - badCharTable.getOrDefault(text.charAt(shift + m), -1) : 1;
            } else {
                shift += Math.max(1, j - badCharTable.getOrDefault(text.charAt(shift + j), -1));
            }
        }

        return occurrences;
    }

    // Helper method to build the bad character table for the Boyer-Moore algorithm
    private Map<Character, Integer> buildBadCharTable(String pattern) {
        Map<Character, Integer> badCharTable = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badCharTable.put(pattern.charAt(i), i);
        }
        return badCharTable;
    }
}
