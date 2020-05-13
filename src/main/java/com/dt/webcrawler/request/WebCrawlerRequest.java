package com.dt.webcrawler.request;

import com.dt.webcrawler.model.UrlTreeStrucInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawlerRequest {
    @NotEmpty(message = "Url cannot be null")
    String url;
    @Min(value=0, message = "depth cannot be less than 0")
    Integer depth;
    int requestId;

    @JsonIgnore
    String status;
    @JsonIgnore
    List<UrlTreeStrucInfo> urlTreeStrucInfos;

    public WebCrawlerRequest(@NotEmpty(message = "Url cannot be null") String url, @Min(value = 0, message = "depth cannot be less than 0") Integer depth,
                             int requestId, List<UrlTreeStrucInfo> urlTreeStrucInfos) {
        this.url = url;
        this.depth = depth;
        this.requestId = requestId;
        this.urlTreeStrucInfos = urlTreeStrucInfos;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public List<UrlTreeStrucInfo> getUrlTreeStrucInfos() {
        return urlTreeStrucInfos;
    }

    public void setUrlTreeStrucInfos(List<UrlTreeStrucInfo> urlTreeStrucInfos) {
        this.urlTreeStrucInfos = urlTreeStrucInfos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}