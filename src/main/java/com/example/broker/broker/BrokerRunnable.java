package com.example.broker.broker;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BrokerRunnable implements Runnable {
    SubscriptionManager subscriptionManager = new SubscriptionManager();

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
            SubscriberBrokerListener subscriberBrokerListener = new SubscriberBrokerListener(connection, subscriptionManager);
            subscriberBrokerListener.run();
            PublisherBrokerListener publisherBrokerListener = new PublisherBrokerListener(connection, subscriptionManager);
            publisherBrokerListener.run();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
