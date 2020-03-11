package com.admiral.rnd.claims.messaging;

import io.micronaut.context.annotation.Property;

public enum Orchestrator {
    INSTANCE;

    private static final String CASE_GROUP_ID = "case-group";
    private static final int NUM_CONSUMER_THREADS = 3;
    
    @Property(name = "kafka.bootstrap.servers")
    String kafkaServers;
    

    private Orchestrator() {
        System.out.println("Created orchestrator");
    }

    public void start() {
        System.out.println("AJG26-1 - config : " + kafkaServers);
        
        // for now just start the consumers ....
        // Start group of Notification Consumer Thread
        NotificationConsumer consumers =
                new NotificationConsumer(CaseProducerSingleton.BROKERS, CASE_GROUP_ID, CaseProducerSingleton.TOPIC);

        consumers.execute(NUM_CONSUMER_THREADS);

        try {
            Thread.sleep(10*60*1000);
        } catch (InterruptedException ie) {

        }
        consumers.shutdown();
        System.out.println("##############################################");
        System.out.println("##############################################");
        System.out.println("   CLOSING DOWN Consumers ......");
        System.out.println("##############################################");
        System.out.println("##############################################");

    }
    
    
    /**
    TO DO
    1. implementing a consumer pattern, where the work to fetch messages from a 
       topic and the work to process them is then handed off to worker tasks (Scheulder Tasks)
       
       see https://howtoprogram.xyz/2016/05/29/create-multi-threaded-apache-kafka-consumer/
       
    2. understand where we fit out consumer into consumer groups.
       i.e. consumer in the web socket vs consumer in the orchestrator
       
       */
    

}
