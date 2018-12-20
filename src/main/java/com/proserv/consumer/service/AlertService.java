package com.proserv.consumer.service;

import com.proserv.consumer.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Value("${logwatch.url}") String logwatchRoot;

    public void SendAlert(Alert alert) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(logwatchRoot + "/alert", alert, Alert.class);
        logger.info("Alert Sent: " + alert);
    }
}
