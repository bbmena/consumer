package com.proserv.consumer;

import com.proserv.consumer.service.SparkConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication implements CommandLineRunner {

    @Autowired private SparkConsumerService sparkConsumerService;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        sparkConsumerService.run();
    }

}

