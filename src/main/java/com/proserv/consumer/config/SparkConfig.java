package com.proserv.consumer.config;

import org.apache.spark.SparkConf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class SparkConfig {

    @Value("${app.name:jigsaw}")
    private String appName;

    @Value("${master.uri:local}")
    private String masterUri;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(appName)
                .setMaster(masterUri);
//                .set("spark.executor.memory","1g");
    }

}
