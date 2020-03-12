package com.admiral.rnd.claims.messaging;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CaseProducerThread implements Runnable {

    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final String caseId;
    private final String eventData;

    public CaseProducerThread(String brokers, String topic, String caseId, String eventData) {
        Properties prop = createProducerConfig(brokers);
        this.producer = new KafkaProducer<String, String>(prop);
        this.topic = topic;
        this.caseId = caseId;
        this.eventData = eventData;
    }

    private static Properties createProducerConfig(String brokers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    @Override
    public void run() {
        producer.send(new ProducerRecord<String, String>(topic, caseId, eventData), new Callback() {

            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
                System.out.println(
                        "posted Case [" + caseId + "] Event [" + eventData + ", Offset [" + metadata.offset() + "]");
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // closes producer
        producer.close();

    }


    // util methods to simplify invocation
    public static void postEvent(String caseId, String eventData) {
        CaseProducerThread producerThread =
                new CaseProducerThread(CaseProducerSingleton.BROKERS, CaseProducerSingleton.TOPIC, caseId, eventData);
        Thread prodT = new Thread(producerThread);
        prodT.start();
    }

}
