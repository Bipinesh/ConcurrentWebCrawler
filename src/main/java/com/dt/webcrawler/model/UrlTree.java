package com.dt.webcrawler.model;

import java.util.List;

public class UrlTree {

    private int totalImages;
    private int totalLinks;
    private List<UrlTreeStrucInfo> details;
    private String status;
    private Integer requestId;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }

    public int getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(int totalLinks) {
        this.totalLinks = totalLinks;
    }

    public List<UrlTreeStrucInfo> getDetails() {
        return details;
    }

    public void setDetails(List<UrlTreeStrucInfo> details) {
        this.details = details;
    }
}
