package com.dt.webcrawler.utility;


import com.dt.webcrawler.model.UrlTree;
import com.dt.webcrawler.model.UrlTreeStrucInfo;
import com.dt.webcrawler.request.WebCrawlerRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class RequestConsumer implements Callable<UrlTree> {

    Logger logger = LoggerFactory.getLogger(RequestConsumer.class);
    private WebCrawlerRequest request;

    public RequestConsumer(WebCrawlerRequest request) {
        this.request = request;
    }

    @Override
    public UrlTree call() throws Exception {
        return generateUrlTreeData(getUrlTreeStrucInfo(request),request.getRequestId());
    }

    public List<UrlTreeStrucInfo> getUrlTreeStrucInfo(WebCrawlerRequest request) {
        logger.info("Generating web Crawler response for request id:"+request.getRequestId());
        UrlTreeStrucInfo pageTree = new UrlTreeStrucInfo();
        int depth = request.getDepth();
        List<UrlTreeStrucInfo> urlTreeStrucInfos = request.getUrlTreeStrucInfos();
        int requestId = request.getRequestId();
        if (depth < 0) {
            return urlTreeStrucInfos;
        } else {
            try {
                Document document = Jsoup.connect(request.getUrl()).get();
                Elements linksOnPage = document.select("a[href]");
                final Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
                pageTree.setLink(request.getUrl());
                pageTree.setTitle(document.title());
                pageTree.setImageCount(images.size());
                urlTreeStrucInfos.add(pageTree);
                linksOnPage.forEach(link -> {
                    WebCrawlerRequest webCrawlerRequest = new WebCrawlerRequest(link.attr("abs:href"),
                            depth - 1, requestId, urlTreeStrucInfos);
                    getUrlTreeStrucInfo(webCrawlerRequest);
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return urlTreeStrucInfos;
        }
    }

    private UrlTree generateUrlTreeData(List<UrlTreeStrucInfo> urlTreeStrucInfos, int requestId) {
        UrlTree pg = new UrlTree();
        pg.setDetails(urlTreeStrucInfos);
        pg.setTotalLinks(urlTreeStrucInfos.size());
        pg.setTotalImages(getImageCount(urlTreeStrucInfos));
        pg.setStatus(WebCrawlerConstants.Status_processed);
        pg.setRequestId(requestId);
        return pg;
    }

    private static int getImageCount(List<UrlTreeStrucInfo> list) {
        int count = 0;
        for (UrlTreeStrucInfo pg : list) {
            count = count + pg.getImageCount();
        }
        return count;

    }
}
