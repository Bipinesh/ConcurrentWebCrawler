package com.dt.webcrawler.Singleton;

import com.dt.webcrawler.request.WebCrawlerRequest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WebCrawlerReqQueue {
    private static BlockingQueue<WebCrawlerRequest> queue = new ArrayBlockingQueue<>(100);

    public static BlockingQueue<WebCrawlerRequest> getInstance() {
        return queue;
    }

    private WebCrawlerReqQueue() {
    }
}
