package com.example.model;

public class PageContent {
    private String title;
    private String content;
    private int frequency;

    public PageContent(String title, String content) {
        this.title = title;
        this.content = content;
        this.frequency = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
