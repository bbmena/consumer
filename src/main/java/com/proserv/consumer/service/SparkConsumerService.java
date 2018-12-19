package com.proserv.consumer.service;

import com.proserv.consumer.config.KafkaConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class SparkConsumerService {

    private final SparkConf sparkConf;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final Collection<String> topics;

    @Autowired public SparkConsumerService(SparkConf sparkConf,
                                           KafkaConsumerConfig kafkaConsumerConfig,
                                           @Value("${kafka.default-topic}") String[] topics) {
        this.sparkConf = sparkConf;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.topics = Arrays.asList(topics);
    }

    @Bean
    public void run(){

        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<String> lines = stream.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());

        lines
                .count()
                .map(count -> "Log Lines (" + count + " lines):")
                .print();

        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
