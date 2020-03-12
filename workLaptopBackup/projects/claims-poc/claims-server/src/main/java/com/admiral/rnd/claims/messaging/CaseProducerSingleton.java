package com.admiral.rnd.claims.messaging;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public enum CaseProducerSingleton {
    INSTANCE;

    private static final String TOPIC_HOST = "localhost";
    private static final int TOPIC_PORT = 9092;
    public static final String BROKERS = TOPIC_HOST + ":" + TOPIC_PORT;
    public static final String TOPIC = "cases";
    private final Producer<String, String> producer;
    
    
    private CaseProducerSingleton() {
        Properties props = new Properties();
        props.put("bootstrap.servers", BROKERS); // can switch to multiple brokers, use a comma separated list string
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);
    }
    
    public void postEvent(final String key, final String event) {
        Future<RecordMetadata> f = producer.send(new ProducerRecord<String, String>(TOPIC, key, event));
        
        // return future ???
        //return f;
        
        // when to close the producer ?
        //producer.close();
    }

}
