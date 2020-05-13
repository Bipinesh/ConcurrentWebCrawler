package com.dt.webcrawler.Singleton;

import com.dt.webcrawler.model.UrlTree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class FutureMap {
    private static ConcurrentHashMap<Integer,Future<UrlTree>> futureMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, Future<UrlTree>> getInstance() {
        return futureMap;
    }

    private FutureMap() {
    }
}
