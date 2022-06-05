package com.example.broker;

import com.example.broker.broker.BrokerRunnable;
import com.example.broker.publisher.PublisherRunnabale;
import com.example.broker.subscriber.SubscriberRunnable;

public class Application {

    public static final int NUMBER_OF_BROKERS = 2;
    public static final int NUMBER_OF_SUBSCRIBERS = 3;
    public static final int NUMBER_OF_PUBLISHERS = 2;

    public static void main(String[] argv) {
        System.out.println("Main thread is - " + Thread.currentThread().getName());

        for(int i = 0; i< NUMBER_OF_BROKERS; i++){
            Thread thread = new Thread(new BrokerRunnable());
            thread.start();
        }

        for(int i = 0; i< NUMBER_OF_SUBSCRIBERS; i++){
            Thread thread = new Thread(new SubscriberRunnable());
            thread.start();
        }

        for(int i = 0; i< NUMBER_OF_PUBLISHERS; i++){
            Thread thread = new Thread(new PublisherRunnabale());
            thread.start();
        }
    }
}
