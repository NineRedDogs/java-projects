package com.admiral.rnd.claims.messaging;

public final class SingleConsumerMain {

    public static void main(String[] args) {

        String groupId = "group01";
        int numberOfThread = 3;

        // Start group of Notification Consumer Thread
        NotificationConsumer consumers =
                new NotificationConsumer(CaseProducerSingleton.BROKERS, groupId, CaseProducerSingleton.TOPIC);

        consumers.execute(numberOfThread);

        try {
            Thread.sleep(100000);
        } catch (InterruptedException ie) {

        }
        consumers.shutdown();
        System.out.println("##############################################");
        System.out.println("##############################################");
        System.out.println("   CLOSING DOWN Consumers ......");
        System.out.println("##############################################");
        System.out.println("##############################################");
    }
}
