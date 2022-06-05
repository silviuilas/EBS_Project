package com.example.broker;

import com.example.broker.broker.BrokerRunnable;
import com.example.broker.publisher.PublisherRunnabale;
import com.example.broker.subscriber.SubscriberRunnable;

import static com.example.broker.helper.CustomLogger.*;


public class Application {

    public static final int NUMBER_OF_BROKERS = 2;
    public static final int NUMBER_OF_SUBSCRIBERS = 3;
    public static final int NUMBER_OF_PUBLISHERS = 2;

    public static void main(String[] argv) {
        System.out.println("Main thread is - " + Thread.currentThread().getName());

        long startTime = System.nanoTime();

        for (int i = 0; i < NUMBER_OF_BROKERS; i++) {
            Thread thread = new Thread(new BrokerRunnable());
            thread.start();
        }

        for (int i = 0; i < NUMBER_OF_SUBSCRIBERS; i++) {
            Thread thread = new Thread(new SubscriberRunnable());
            thread.start();
        }

        for (int i = 0; i < NUMBER_OF_PUBLISHERS; i++) {
            Thread thread = new Thread(new PublisherRunnabale());
            thread.start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println(" [Shutting Down] In shutdown hook");
                System.out.println(" [Shutting Down] Nr of nrOfSubscriptionSent " + nrOfSubscriptionSent);
                System.out.println(" [Shutting Down] Nr of nrOfSubscriptionReceived " + nrOfSubscriptionReceived);
                System.out.println(" [Shutting Down] Nr of nrOfPublicationSent " + nrOfPublicationSent);
                System.out.println(" [Shutting Down] Nr of nrOfPublicationReceived " + (nrOfPublicationReceived.intValue() / NUMBER_OF_BROKERS));
                System.out.println(" [Shutting Down] Nr of nrOfMatchesDone " + nrOfMatchesDone);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println(" [Shutting Down] Took " + duration / 1000000 / 1000 + " seconds");
            }
        }, "Shutdown-thread"));
    }
}
