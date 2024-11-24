package com.example.model;

import java.util.ArrayList;
import java.util.List;

public  class TrieNode {
    TrieNode[] children;
    List<Occurrence> occurrences;

    public TrieNode() {
        this.children = new TrieNode[26]; // 26 letters in the alphabet
        this.occurrences = new ArrayList<>();
    }

    public TrieNode[] getChildren() {
        return children;
    }

    public List<Occurrence> getOccurrences() {
        return occurrences;
    }

    public void addOccurrence(Occurrence occurrence) {
        this.occurrences.add(occurrence);
    }

    public int charToIndex(char c) {
        return c - 'a';
    }
}
