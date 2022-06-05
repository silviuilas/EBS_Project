package com.example.broker.broker;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BrokerRunnable implements Runnable {
    SubscriptionManager subscriptionManager;

    public static void main(String[] args) {
        Thread receiveLogsThread = new Thread(new BrokerRunnable());
        receiveLogsThread.start();
        Thread receiveLogsThread2 = new Thread(new BrokerRunnable());
        receiveLogsThread2.start();
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        try {
            connection = factory.newConnection();
            subscriptionManager = new SubscriptionManager(factory);
            SubscriberBrokerListener subscriberBrokerListener = new SubscriberBrokerListener(connection, subscriptionManager);
            Thread subscriberBrokerListenerThread = new Thread(() -> {
                try {
                    subscriberBrokerListener.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            subscriberBrokerListenerThread.start();

            PublisherBrokerListener publisherBrokerListener = new PublisherBrokerListener(connection, subscriptionManager);
            Thread publisherBrokerListenerThread = new Thread(() -> {
                try {
                    publisherBrokerListener.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            publisherBrokerListenerThread.start();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
