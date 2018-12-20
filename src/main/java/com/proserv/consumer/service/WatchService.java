package com.proserv.consumer.service;

import com.proserv.consumer.model.WatchList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WatchService {

    private final String logwatchRoot;

    private WatchList currentWatches;

    @Autowired WatchService(@Value("${logwatch.url}") String logwatchRoot){
        this.logwatchRoot = logwatchRoot;
        this.currentWatches = getWatches();
    }

    public WatchList getCurrentWatches(){
        return this.currentWatches;
    }

    private WatchList getWatches() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(logwatchRoot + "/watch", WatchList.class);
    }
}
