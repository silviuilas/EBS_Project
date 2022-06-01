package com.example.broker.runners;

import com.example.broker.Constants;
import com.example.broker.SubscriptionManager;
import com.example.broker.brokers.PublisherBroker;
import com.example.broker.brokers.SubscriberBroker;
import com.example.broker.pubsub.Publishing;
import com.example.broker.pubsub.Subscriber;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.example.broker.Constants.*;
import static com.example.broker.Constants.PUBLISHING_EXCHANGE_NAME;

public class BrokerRunnable implements Runnable {
    SubscriptionManager subscriptionManager = new SubscriptionManager();

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            SubscriberBroker subscriberBroker = new SubscriberBroker(connection, subscriptionManager);
            subscriberBroker.run();
            PublisherBroker publisherBroker = new PublisherBroker(connection, subscriptionManager);
            publisherBroker.run();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
