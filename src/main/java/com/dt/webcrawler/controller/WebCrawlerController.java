package com.dt.webcrawler.controller;

import com.dt.webcrawler.Singleton.FutureMap;
import com.dt.webcrawler.Singleton.WebCrawlerReqQueue;
import com.dt.webcrawler.config.StartupApplicationListener;
import com.dt.webcrawler.model.UrlTree;
import com.dt.webcrawler.model.UrlTreeStrucInfo;
import com.dt.webcrawler.request.WebCrawlerRequest;
import com.dt.webcrawler.service.CrawlerService;
import com.dt.webcrawler.utility.RequestSubmitter;
import com.dt.webcrawler.utility.WebCrawlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class WebCrawlerController {

    Logger logger = LoggerFactory.getLogger(WebCrawlerController.class);

    private static AtomicInteger counter = new AtomicInteger(0);

    public int getNextUniqueIndex() {
        int requestIgGen = counter.incrementAndGet();
        logger.info("Request ID generated:"+requestIgGen);
        return requestIgGen;
    }

    @PostMapping(value="/submitRequest")
    public ResponseEntity<String> submitRequest(@RequestBody WebCrawlerRequest request){
        logger.info("Web Crwaler request for processing received");
        WebCrawlerRequest webCrawlerRequest = new WebCrawlerRequest(request.getUrl(),request.getDepth(),
                getNextUniqueIndex(),
                new ArrayList<UrlTreeStrucInfo>());
        BlockingQueue<WebCrawlerRequest> queue = WebCrawlerReqQueue.getInstance();
        if(queue.size()==100)
            return new ResponseEntity<String>(WebCrawlerConstants.RequestSubmittionFailed,HttpStatus.OK);
        new Thread(new RequestSubmitter(queue,webCrawlerRequest)).start();
        return new ResponseEntity<String>(WebCrawlerConstants.RequestSubmittionSuccess+webCrawlerRequest.getRequestId(),HttpStatus.OK);
    }

    @GetMapping(value="/fetchResult")
    public ResponseEntity<UrlTree> getRequestResult(@RequestParam(value = "requestId", required = true) final Integer requestId){
        ConcurrentHashMap<Integer, Future<UrlTree>> map = FutureMap.getInstance();
        if(map.contains(requestId)){
           Future<UrlTree> future =  map.get(requestId);
           if(future.isDone()){
               try {
                   return new ResponseEntity<UrlTree>(future.get(),HttpStatus.OK);
               } catch (InterruptedException e) {
                   e.printStackTrace();
                   UrlTree urlTree = new UrlTree();
                   urlTree.setRequestId(requestId);
                   urlTree.setStatus(WebCrawlerConstants.Status_failed);
                   return new ResponseEntity<UrlTree>(urlTree,HttpStatus.OK);
               } catch (ExecutionException e) {
                   e.printStackTrace();
                   UrlTree urlTree = new UrlTree();
                   urlTree.setRequestId(requestId);
                   urlTree.setStatus(WebCrawlerConstants.Status_proceesingException);
                   return new ResponseEntity<UrlTree>(urlTree,HttpStatus.OK);
               }
           }
           else{
               UrlTree urlTree = new UrlTree();
               urlTree.setRequestId(requestId);
               urlTree.setStatus(WebCrawlerConstants.Status_inprocess);
               return new ResponseEntity<UrlTree>(urlTree,HttpStatus.OK);
           }
        }else {
            UrlTree urlTree = new UrlTree();
            urlTree.setRequestId(requestId);
            urlTree.setStatus(WebCrawlerConstants.Status_notfound);
            return new ResponseEntity<UrlTree>(urlTree,HttpStatus.NOT_FOUND);
        }
    }


}
