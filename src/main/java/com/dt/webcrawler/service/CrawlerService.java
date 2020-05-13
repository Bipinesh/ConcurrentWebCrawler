package com.dt.webcrawler.service;

import com.dt.webcrawler.Singleton.FutureMap;
import com.dt.webcrawler.Singleton.WebCrawlerReqQueue;
import com.dt.webcrawler.model.UrlTree;
import com.dt.webcrawler.request.WebCrawlerRequest;
import com.dt.webcrawler.utility.RequestConsumer;
import com.dt.webcrawler.utility.WebCrawlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class CrawlerService {

	Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	public void startProcessing(){
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		ConcurrentHashMap<Integer, Future<UrlTree>> map = FutureMap.getInstance();

		try {
			while(true) {
				WebCrawlerRequest webCrawlerRequest = WebCrawlerReqQueue.getInstance().take();
				if(webCrawlerRequest!=null){
					webCrawlerRequest.setStatus(WebCrawlerConstants.Status_submitted);
					logger.info("Submitting for Processing,request id:"+webCrawlerRequest.getRequestId());
					Future<UrlTree> urlTreeFuture = executorService.submit( new RequestConsumer(webCrawlerRequest));
					map.put(webCrawlerRequest.getRequestId(),urlTreeFuture);

				}
				else{
					Thread.sleep(2000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
