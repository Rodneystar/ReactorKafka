package com.jdog.ReactorKafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
public class KafkaOut implements CommandLineRunner {

    @Autowired
    KafkaSender<Integer, String> kafkaSender;

    @Override
    public void run(String... args) throws Exception {
        Flux<SenderRecord<Integer, String, Integer>> outFlux = Flux.range(1, 10).map(i -> SenderRecord
                .create(new ProducerRecord<Integer, String>("test", 0, i, "record number: " + i), i));
        kafkaSender.send(outFlux)
            .doOnNext( r -> System.out.println("sent message: " + r.correlationMetadata()))
            .subscribe();

    }


}
