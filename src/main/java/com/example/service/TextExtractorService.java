package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TextExtractorService {

    /**
     * Extracts text from the uploaded file and processes it to categorize data like
     * emails, phone numbers, URLs, and dates.
     *
     * @param domain The domain for which the text is being extracted.
     * @param file   The uploaded file containing text.
     * @return Structured output of extracted data.
     */
    public String extractText(String domain, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The uploaded file is empty.");
        }

        StringBuilder rawText = new StringBuilder();
        List<String> emails = new ArrayList<>();
        List<String> validPhones = new ArrayList<>();
        List<String> invalidPhones = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        // Patterns for extraction
        Pattern emailPattern = Pattern.compile("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}");
        Pattern phonePattern = Pattern.compile("\\d{10}");
        Pattern urlPattern = Pattern.compile("https?://[\\w.-]+(/[\\w./?=%-]*)?");
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                rawText.append(line).append("\n");

                // Match and categorize data
                Matcher emailMatcher = emailPattern.matcher(line);
                while (emailMatcher.find()) {
                    emails.add(emailMatcher.group());
                }

                Matcher phoneMatcher = phonePattern.matcher(line);
                while (phoneMatcher.find()) {
                    String phone = phoneMatcher.group();
                    if (phone.startsWith("123") || phone.length() != 10) {
                        invalidPhones.add(phone);
                    } else {
                        validPhones.add(phone);
                    }
                }

                Matcher urlMatcher = urlPattern.matcher(line);
                while (urlMatcher.find()) {
                    urls.add(urlMatcher.group());
                }

                Matcher dateMatcher = datePattern.matcher(line);
                while (dateMatcher.find()) {
                    dates.add(dateMatcher.group());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the uploaded file.", e);
        }

        // Structure the extracted data
        StringBuilder structuredOutput = new StringBuilder();
        structuredOutput.append("Extracted Text for Domain: ").append(domain).append("\n\n");
        structuredOutput.append("Emails Validated:\n");
        for (String email : emails) {
            structuredOutput.append(email).append("\n");
        }

        // structuredOutput.append("\nPhone Numbers:\n");
        // structuredOutput.append("Valid:\n");
        // for (String phone : validPhones) {
        // structuredOutput.append(phone).append("\n");
        // }
        // structuredOutput.append("Invalid:\n");
        // for (String phone : invalidPhones) {
        // structuredOutput.append(phone).append("\n");
        // }

        // structuredOutput.append("\nURLs:\n");
        // for (String url : urls) {
        // structuredOutput.append(url).append("\n");
        // }

        // structuredOutput.append("\nDates:\n");
        // for (String date : dates) {
        // structuredOutput.append(date).append("\n");
        // }

        return structuredOutput.toString();
    }
}
