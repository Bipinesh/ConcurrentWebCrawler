package com.dt.webcrawler.config;

import com.dt.webcrawler.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    CrawlerService crawlerService;

    Logger logger = LoggerFactory.getLogger(StartupApplicationListener.class);
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadOnStartUp();
    }

    private void loadOnStartUp() {
        logger.info("Application load on startup called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                crawlerService.startProcessing();
            }
        }).start();
    }
}
