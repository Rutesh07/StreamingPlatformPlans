package com.example.model;

public class AVLTree {
    private class Node {
        String word;
        int frequency;
        int height;
        Node left, right;

        Node(String word) {
            this.word = word;
            this.frequency = 1;
            this.height = 1;
        }
    }

    private Node root;

    public void insert(String word) {
        root = insert(root, word);
    }

    private Node insert(Node node, String word) {
        if (node == null) {
            return new Node(word);
        }

        int cmp = word.compareTo(node.word);
        if (cmp < 0) {
            node.left = insert(node.left, word);
        } else if (cmp > 0) {
            node.right = insert(node.right, word);
        } else {
            node.frequency++;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private Node balance(Node node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        } else if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }

        return node;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));
        return newRoot;
    }

    public int getFrequency(String word) {
        Node node = search(root, word);
        return node != null ? node.frequency : 0;
    }

    private Node search(Node node, String word) {
        if (node == null) {
            return null;
        }

        int cmp = word.compareTo(node.word);
        if (cmp < 0) {
            return search(node.left, word);
        } else if (cmp > 0) {
            return search(node.right, word);
        } else {
            return node;
        }
    }
}
