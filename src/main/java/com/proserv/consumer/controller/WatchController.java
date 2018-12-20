package com.proserv.consumer.controller;

import com.proserv.consumer.model.Watch;
import com.proserv.consumer.service.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WatchController {
    private static final Logger logger = LoggerFactory.getLogger(WatchController.class);

    @Autowired private WatchService watchService;

    @RequestMapping(path = "/watch", method = RequestMethod.POST)
    public void addWatch(@RequestBody Watch watch) {
        watchService.getCurrentWatches().getWatches().add(watch);
        logger.info("Added watch: '" + watch.getWatchText()+ "'");
    }
}
