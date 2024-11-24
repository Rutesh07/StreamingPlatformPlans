package com.example.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.AVLTree;
import com.example.model.Occurrence;
import com.example.model.Trie;

@Service
public class InvertedIndexService {
    private final Trie trie = new Trie();
    private final AVLTree avlTree = new AVLTree();

    public InvertedIndexService() {
        initializeInvertedIndex();
    }

    private void initializeInvertedIndex() {
        File dir = new File("src/main/text_pages"); // Use relative path based on the app root

        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Directory 'text_pages' does not exist or is not accessible.");
            return; // Exit if the directory is invalid
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.err.println("No .txt files found in the 'text_pages' directory.");
            return; // Exit if no .txt files are found
        }

        for (int pageIndex = 0; pageIndex < files.length; pageIndex++) {
            File file = files[pageIndex];
            try {
                String content = readFile(file.getAbsolutePath());
                String[] words = content.split("\\W+");

                for (int position = 0; position < words.length; position++) {
                    String word = words[position].toLowerCase();
                    if (!word.isEmpty()) {
                        trie.insert(word, file.getName(), pageIndex + 1, position);
                        avlTree.insert(word);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString();
    }

    public List<Occurrence> searchInvertedIndex(String word) {
        return trie.search(word);
    }

    public int getFrequency(String word) {
        return avlTree.getFrequency(word);
    }
}
