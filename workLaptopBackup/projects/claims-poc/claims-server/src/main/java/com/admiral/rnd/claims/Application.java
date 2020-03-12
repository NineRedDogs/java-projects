package com.admiral.rnd.claims;

import com.admiral.rnd.claims.messaging.Orchestrator;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
        Orchestrator.INSTANCE.start();
    }
}