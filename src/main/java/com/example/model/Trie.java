package com.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trie {
    private class TrieNode {
        TrieNode[] children;
        List<Occurrence> occurrences;

        public TrieNode() {
            this.children = new TrieNode[26]; // 26 letters in the alphabet
            this.occurrences = new ArrayList<>();
        }

        public int charToIndex(char c) {
            return c - 'a';
        }
    }

   
        private TrieNode root;
    
        public Trie() {
            root = new TrieNode();
        }
    
        public void insert(String word, String filename, int pageIndex, int position) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (Character.isLetter(c)) {
                    c = Character.toLowerCase(c);
                    int index = node.charToIndex(c);
                    if (node.children[index] == null) {
                        node.children[index] = new TrieNode();
                    }
                    node = node.children[index];
                }
            }
            node.occurrences.add(new Occurrence(pageIndex, position, filename));
        }
    
    public List<Occurrence> search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toLowerCase(c);
                int index = node.charToIndex(c);
                if (node.children[index] == null) {
                    return Collections.emptyList();
                }
                node = node.children[index];
            }
        }
        return node.occurrences;
    }
}
