package com.admiral.rnd.claims.messaging;

import javax.inject.Singleton;

import io.micronaut.context.annotation.Property;

@Singleton
public class Noddy {
    
    @Property(name = "kafka.bootstrap.servers")
    String kafkaServers;

    /**
     * 
     */
    public Noddy() {
        System.out.println("AJG26-3 - config : " + kafkaServers);

    }
    


    


}
