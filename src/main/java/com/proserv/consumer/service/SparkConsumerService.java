package com.proserv.consumer.service;

import com.proserv.consumer.config.KafkaConsumerConfig;
import com.proserv.consumer.model.Alert;
import com.proserv.consumer.model.WatchList;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class SparkConsumerService {

    private final SparkConf sparkConf;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final Collection<String> topics;

    @Autowired private WatchService watchService;
    @Autowired private AlertService alertService;

    @Autowired public SparkConsumerService(SparkConf sparkConf,
                                           KafkaConsumerConfig kafkaConsumerConfig,
                                           @Value("${kafka.default-topic}") String[] topics) {
        this.sparkConf = sparkConf;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.topics = Arrays.asList(topics);
    }

    public void run(){

        WatchList watches = watchService.getCurrentWatches();

        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<String> lines = stream.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());

        lines
                .count()
                .map(count -> "Log Lines: (" + count + " lines) " + "Watches: " + watches.toString())
                .print();

        lines
                .flatMap(x -> Arrays.asList(x).iterator())
                .mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2)
                .mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap())
                .foreachRDD(rdd -> {
                    List<Tuple2<Integer, String>> sorted;
                    JavaPairRDD<Integer, String> counts = rdd.sortByKey(false);
                    sorted = counts.collect();
                    sorted.forEach( record -> {
                        watches.getWatches().forEach(watch -> {
                            if(record._2.contains(watch.getWatchText())){
                                alertService.SendAlert(new Alert("'" + watch.getWatchText() + "' occurred  " + record._1 + " times"));
                            }
                        });
                    });
                });


        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
