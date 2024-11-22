package com.example.model;

public class StreamingServicePlan {
    private String serviceName;
    private String planName;
    private String price;
    private String annualPrice;
    private String features;
    private int simultaneousStream;
    private String download;
    private String adFreeStreaming;

    // Getters and setters
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAnnualPrice() {
        return annualPrice;
    }

    public void setAnnualPrice(String annualPrice) {
        this.annualPrice = annualPrice;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public int getSimultaneousStream() {
        return simultaneousStream;
    }

    public void setSimultaneousStream(int simultaneousStream) {
        this.simultaneousStream = simultaneousStream;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getAdFreeStreaming() {
        return adFreeStreaming;
    }

    public void setAdFreeStreaming(String adFreeStreaming) {
        this.adFreeStreaming = adFreeStreaming;
    }

    @Override
    public String toString() {
        return "StreamingServicePlan{" +
                "serviceName='" + serviceName + '\'' +
                ", planName='" + planName + '\'' +
                ", price='" + price + '\'' +
                ", annualPrice='" + annualPrice + '\'' +
                ", features='" + features + '\'' +
                ", simultaneousStream=" + simultaneousStream +
                ", download='" + download + '\'' +
                ", adFreeStreaming='" + adFreeStreaming + '\'' +
                '}';
    }
}
