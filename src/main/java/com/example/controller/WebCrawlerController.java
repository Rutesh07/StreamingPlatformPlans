package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebCrawlerController {

    @GetMapping("/api/crawl")
    public List<String> crawlWebsite(@RequestParam String url) {
        List<String> links = new ArrayList<>();
        try {
            // Ensure URL is valid
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("URL cannot be empty");
            }

            // Fetch the HTML content of the URL
            Document doc = Jsoup.connect(url).get();  
            Elements linkElements = doc.select("a[href]");  // Extract all links

            // Extract and store absolute URLs
            for (Element link : linkElements) {
                String absUrl = link.absUrl("href");
                if (absUrl != null && !absUrl.isEmpty()) {
                    links.add(absUrl);  
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching URL: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid input: " + e.getMessage());
        }

        return links;  // Return the list of links as JSON
    }
}
