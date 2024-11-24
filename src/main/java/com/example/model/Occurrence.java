package com.example.model;

public class Occurrence {
    private int pageIndex;
    private int position;
    private String filename;

    public Occurrence(int pageIndex, int position, String filename) {
        this.pageIndex = pageIndex;
        this.position = position;
        this.filename = filename;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Occurrence{" +
                "pageIndex=" + pageIndex +
                ", position=" + position +
                ", filename='" + filename + '\'' +
                '}';
    }
}
