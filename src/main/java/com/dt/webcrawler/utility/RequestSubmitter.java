package com.dt.webcrawler.utility;

import com.dt.webcrawler.request.WebCrawlerRequest;

import java.util.concurrent.BlockingQueue;

public class RequestSubmitter implements Runnable {

    private BlockingQueue<WebCrawlerRequest> webCrawlerRequestBlockingQueue;
    private WebCrawlerRequest request;

    public RequestSubmitter(BlockingQueue<WebCrawlerRequest> webCrawlerRequestBlockingQueue, WebCrawlerRequest request) {
        this.webCrawlerRequestBlockingQueue = webCrawlerRequestBlockingQueue;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            request.setStatus(WebCrawlerConstants.Status_submitted);
            webCrawlerRequestBlockingQueue.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
