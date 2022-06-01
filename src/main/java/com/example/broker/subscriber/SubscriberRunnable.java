package com.example.broker.subscriber;

import com.example.broker.helper.RandomString;
import com.example.broker.pubsub.AtomicSubscription;
import com.example.broker.pubsub.Subscription;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

public class SubscriberRunnable implements Runnable {

    RandomString gen = new RandomString(8, ThreadLocalRandom.current());
    String name;

    public static void main(String[] args) {
        Thread receiveLogsThread = new Thread(new SubscriberRunnable());
        receiveLogsThread.start();
    }

    @Override
    public void run() {
        name = gen.nextString();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            SubscriberListener subscriberListener = new SubscriberListener(connection);
            subscriberListener.listenTo(name);

            Subscription subscription = createSubscriberWithRouteKey(name);
            SubscriberSender subscriberSender = new SubscriberSender(connection);
            subscriberSender.subscribe(subscription);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


    private Subscription createSubscriberWithRouteKey(String routeKey) {
        Subscription subscription = new Subscription(routeKey);
        subscription.getAtomicSubscriptions().add(new AtomicSubscription("price", "<=", 4));
        return subscription;
    }
}